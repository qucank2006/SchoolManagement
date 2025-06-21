package com.example.dacs1.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import com.example.dacs1.DAO.ChatMessageDAO;
import com.example.dacs1.Model.ChatMessage;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String userName;
    private Map<String, ClientHandler> clients;
    private final ChatMessageDAO chatMessageDAO = new ChatMessageDAO();

    public ClientHandler(Socket socket, Map<String, ClientHandler> clients) {
        this.clientSocket = socket;
        this.clients = clients;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Nhận tên user đầu tiên
            this.userName = in.readLine();
            if (userName == null || userName.isEmpty() || clients.containsKey(userName)) {
                sendMessage("ERROR: Tên người dùng không hợp lệ hoặc đã tồn tại!");
                shutdown();
                return;
            }
            ChatServer.addUser(userName, this);

            // Gửi các tin nhắn offline
            sendOfflineMessages();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("TO:")) {
                    // Tin nhắn riêng: TO:username:message
                    String[] parts = inputLine.split(":", 3);
                    if (parts.length == 3) {
                        String recipient = parts[1];
                        String message = parts[2];
                        ChatServer.sendPrivateMessage(recipient, message, userName);
                    }
                } else if (inputLine.startsWith("ALL:")) {
                    // Tin nhắn chung: ALL:message
                    String message = inputLine.substring(4);
                    ChatServer.broadcastMessage(message, userName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private void sendOfflineMessages() {
        List<ChatMessage> offlineMessages = chatMessageDAO.getUndeliveredMessagesForUser(this.userName);
        if (!offlineMessages.isEmpty()) {
            for (ChatMessage msg : offlineMessages) {
                // Định dạng lại tin nhắn cho đẹp
                String formattedMessage = "[" + msg.getSenderName() + " -> bạn (offline)]: " + msg.getMessageContent();
                sendMessage(formattedMessage);
            }
            // Đánh dấu các tin nhắn đã gửi
            List<Integer> messageIds = offlineMessages.stream().map(ChatMessage::getId).collect(Collectors.toList());
            chatMessageDAO.markMessagesAsDelivered(messageIds);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void shutdown() {
        try {
            if (userName != null) {
                ChatServer.removeUser(userName);
            }
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }
} 