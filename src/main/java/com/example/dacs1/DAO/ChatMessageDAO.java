package com.example.dacs1.DAO;

import com.example.dacs1.Model.ChatMessage;
import com.example.dacs1.Utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageDAO {

    // Lưu một tin nhắn mới vào DB
    public void addMessage(ChatMessage message) {
        String query = "INSERT INTO chat_messages (sender_name, recipient_name, message_content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, message.getSenderName());
            pstmt.setString(2, message.getRecipientName());
            pstmt.setString(3, message.getMessageContent());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy các tin nhắn chưa được gửi cho một user
    public List<ChatMessage> getUndeliveredMessagesForUser(String userName) {
        List<ChatMessage> messages = new ArrayList<>();
        String query = "SELECT * FROM chat_messages WHERE recipient_name = ? AND is_delivered = FALSE ORDER BY sent_at ASC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Đánh dấu các tin nhắn là đã gửi
    public void markMessagesAsDelivered(List<Integer> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            return;
        }
        String query = "UPDATE chat_messages SET is_delivered = TRUE WHERE id IN (";
        for (int i = 0; i < messageIds.size(); i++) {
            query += (i == 0 ? "?" : ", ?");
        }
        query += ")";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < messageIds.size(); i++) {
                pstmt.setInt(i + 1, messageIds.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy lịch sử chat giữa 2 người
    public List<ChatMessage> getChatHistory(String user1, String user2) {
        List<ChatMessage> messages = new ArrayList<>();
        String query = "SELECT * FROM chat_messages WHERE " +
                       "(sender_name = ? AND recipient_name = ?) OR " +
                       "(sender_name = ? AND recipient_name = ?) " +
                       "ORDER BY sent_at ASC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user1);
            pstmt.setString(2, user2);
            pstmt.setString(3, user2);
            pstmt.setString(4, user1);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Lấy tin nhắn chung (gửi đến "all" hoặc "Tất cả")
    public List<ChatMessage> getGeneralMessages() {
        List<ChatMessage> messages = new ArrayList<>();
        String query = "SELECT * FROM chat_messages WHERE recipient_name IN ('all', 'Tất cả') ORDER BY sent_at ASC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private ChatMessage extractMessageFromResultSet(ResultSet rs) throws SQLException {
        return new ChatMessage(
                rs.getInt("id"),
                rs.getString("sender_name"),
                rs.getString("recipient_name"),
                rs.getString("message_content"),
                rs.getTimestamp("sent_at"),
                rs.getBoolean("is_delivered")
        );
    }
}
