package com.example.dacs1.Controller;

import com.example.dacs1.Chat.ChatClient;
import com.example.dacs1.DAO.LectureDAO;
import com.example.dacs1.DAO.AdminDAO;
import com.example.dacs1.DAO.ChatMessageDAO;
import com.example.dacs1.Model.Lecture;
import com.example.dacs1.Model.Admin;
import com.example.dacs1.Model.ChatMessage;
import com.example.dacs1.Utils.UserSession;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class ChatViewController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;
    @FXML
    private ListView<String> userListView;

    private ChatClient chatClient;
    private String currentUserFullName;
    private ObservableList<String> userDisplayList = FXCollections.observableArrayList();
    private String selectedUser = "Tất cả";
    
    private final ChatMessageDAO chatMessageDAO = new ChatMessageDAO();

    private final LectureDAO lectureDAO = new LectureDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private List<String> onlineUsers = FXCollections.observableArrayList();

    public void initialize(String fullName) {
        this.currentUserFullName = fullName;
        
        // Lấy danh sách toàn bộ người dùng và hiển thị
        loadAllUsers();
        userListView.setItems(userDisplayList);

        // Thiết lập listener cho việc chọn user
        userListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Lấy tên thật (loại bỏ phần phân quyền)
                selectedUser = newVal.replace(" (Admin)", "").replace(" (Lecture)", "");
                showChatHistory(selectedUser);
            }
        });

        // Chọn mặc định "Tất cả"
        userListView.getSelectionModel().selectFirst();
        
        try {
            this.chatClient = new ChatClient(this.currentUserFullName, this::onMessageReceived, this::onUserListReceived);
            chatClient.start();
        } catch (IOException e) {
            e.printStackTrace();
            chatArea.appendText("Failed to connect to the chat server.\n");
        }
    }

    private void loadAllUsers() {
        // Chỉ lấy danh sách Admin và Lecture, không có Student
        userDisplayList.setAll("Tất cả");
        
        // Lấy danh sách Lecture
        List<String> lectureNames = lectureDAO.getAllLectures().stream()
                .map(lecture -> lecture.getLectureName() + " (Lecture)")
                .collect(Collectors.toList());
        userDisplayList.addAll(FXCollections.observableArrayList(lectureNames));
        
        // Lấy danh sách Admin
        List<String> adminNames = adminDAO.getAllAdmins().stream()
                .map(admin -> admin.getFullName() + " (Admin)")
                .collect(Collectors.toList());
        userDisplayList.addAll(FXCollections.observableArrayList(adminNames));
        
        // Loại bỏ chính mình khỏi danh sách
        if (currentUserFullName != null) {
            userDisplayList.removeIf(name -> name.startsWith(currentUserFullName + " "));
        }
    }

    private void onMessageReceived(String message) {
        Platform.runLater(() -> {
            // Chỉ cần hiển thị tin nhắn mới
            chatArea.appendText(message + "\n");
        });
    }

    private void onUserListReceived(List<String> users) {
        Platform.runLater(() -> {
            this.onlineUsers = users;
            // Không cần cập nhật trạng thái nữa vì đã hiển thị phân quyền
        });
    }

    private void showChatHistory(String user) {
        chatArea.clear();
        if ("Tất cả".equals(user)) {
            // Hiển thị tin nhắn chung (gửi đến "all" hoặc "Tất cả")
            List<ChatMessage> generalHistory = chatMessageDAO.getGeneralMessages();
            for (ChatMessage msg : generalHistory) {
                String formattedMessage;
                if (msg.getSenderName().equals(this.currentUserFullName)) {
                    formattedMessage = "[Bạn -> Tất cả]: " + msg.getMessageContent();
                } else {
                    formattedMessage = "[" + msg.getSenderName() + " -> Tất cả]: " + msg.getMessageContent();
                }
                chatArea.appendText(formattedMessage + "\n");
            }
            return;
        }

        List<ChatMessage> history = chatMessageDAO.getChatHistory(this.currentUserFullName, user);
        for (ChatMessage msg : history) {
            String formattedMessage;
            if (msg.getSenderName().equals(this.currentUserFullName)) {
                formattedMessage = "[Bạn -> " + msg.getRecipientName() + "]: " + msg.getMessageContent();
            } else {
                formattedMessage = "[" + msg.getSenderName() + " -> bạn]: " + msg.getMessageContent();
            }
            chatArea.appendText(formattedMessage + "\n");
        }
    }

    @FXML
    void handleSendButtonAction(ActionEvent event) {
        String message = messageField.getText();
        if (message != null && !message.isEmpty()) {
            if ("Tất cả".equals(selectedUser)) {
                // Gửi tin nhắn chung
                chatClient.sendMessage("Tất cả", message);
                
                // Hiển thị ngay tin nhắn mình vừa gửi
                String formattedMessage = "[Bạn -> Tất cả]: " + message;
                chatArea.appendText(formattedMessage + "\n");
            } else {
                // Gửi tin nhắn riêng tư
                chatClient.sendMessage(selectedUser, message);
                
                // Hiển thị ngay tin nhắn mình vừa gửi
                String formattedMessage = "[Bạn -> " + selectedUser + "]: " + message;
                chatArea.appendText(formattedMessage + "\n");
            }
            
            messageField.clear();
        }
    }

    public void shutdown() {
        if (chatClient != null) {
            chatClient.shutdown();
        }
    }
} 