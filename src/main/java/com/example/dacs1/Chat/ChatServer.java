package com.example.dacs1.Chat;

import com.example.dacs1.DAO.ChatMessageDAO;
import com.example.dacs1.Model.ChatMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {

    private static final int PORT = 12345;
    // Map userName -> ClientHandler
    private static final Map<String, ClientHandler> clients = Collections.synchronizedMap(new HashMap<>());
    private static final ChatMessageDAO chatMessageDAO = new ChatMessageDAO();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gửi tin nhắn riêng, có xử lý offline
    public static void sendPrivateMessage(String recipient, String messageContent, String sender) {
        ClientHandler recipientHandler = clients.get(recipient);

        if (recipientHandler != null) {
            // Người nhận đang online -> gửi trực tiếp
            recipientHandler.sendMessage("[" + sender + " -> bạn]: " + messageContent);
            // Lưu tin nhắn vào DB
            chatMessageDAO.addMessage(new ChatMessage(sender, recipient, messageContent));
        } else {
            // Người nhận offline -> chỉ lưu vào DB
            chatMessageDAO.addMessage(new ChatMessage(sender, recipient, messageContent));
            // System.out.println("User " + recipient + " is offline. Message from " + sender + " stored in DB.");
        }
    }

    // Gửi tin nhắn cho tất cả user (trừ sender)
    public static void broadcastMessage(String message, String sender) {
        synchronized (clients) {
            for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
                if (!entry.getKey().equals(sender)) {
                    entry.getValue().sendMessage("[" + sender + " -> tất cả]: " + message);
                }
                // Lưu tin nhắn broadcast vào DB
                chatMessageDAO.addMessage(new ChatMessage(sender, "all", message));
            }
        }
    }

    // Gửi danh sách user online cho tất cả client
    public static void broadcastUserList() {
        StringBuilder sb = new StringBuilder();
        sb.append("USERLIST:");
        synchronized (clients) {
            for (String user : clients.keySet()) {
                sb.append(user).append(",");
            }
        }
        String userListMsg = sb.toString();
        for (ClientHandler client : clients.values()) {
            client.sendMessage(userListMsg);
        }
    }

    // Thêm user vào map
    public static void addUser(String userName, ClientHandler handler) {
        clients.put(userName, handler);
        broadcastUserList();
    }

    // Xóa user khỏi map
    public static void removeUser(String userName) {
        clients.remove(userName);
        broadcastUserList();
    }
} 