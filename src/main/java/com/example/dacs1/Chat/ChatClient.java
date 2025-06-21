package com.example.dacs1.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChatClient {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Consumer<String> onMessageReceived;
    private Consumer<List<String>> onUserListReceived;
    private volatile boolean running = true;
    private String userName;

    public ChatClient(String userName, Consumer<String> onMessageReceived, Consumer<List<String>> onUserListReceived) throws IOException {
        this.userName = userName;
        this.onMessageReceived = onMessageReceived;
        this.onUserListReceived = onUserListReceived;
        this.socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void start() {
        out.println(userName);
        Thread readThread = new Thread(() -> {
            try {
                String serverMessage;
                while (running && (serverMessage = in.readLine()) != null) {
                    if (serverMessage.startsWith("USERLIST:")) {
                        String[] users = serverMessage.substring(9).split(",");
                        List<String> userList = new ArrayList<>();
                        for (String user : users) {
                            if (!user.isEmpty()) userList.add(user);
                        }
                        onUserListReceived.accept(userList);
                    } else {
                        onMessageReceived.accept(serverMessage);
                    }
                }
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                    onMessageReceived.accept("Connection to server lost.");
                }
            }
        });
        readThread.start();
    }

    public void sendMessage(String recipient, String message) {
        if (recipient.equals("Tất cả")) {
            out.println("ALL:" + message);
        } else {
            out.println("TO:" + recipient + ":" + message);
        }
    }

    public void shutdown() {
        running = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 