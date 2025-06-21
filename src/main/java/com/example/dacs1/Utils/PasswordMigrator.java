package com.example.dacs1.Utils;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PasswordMigrator {
    public static void main(String[] args) {
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement selectStmt = con.prepareStatement("SELECT id, password FROM users");
             ResultSet rs = selectStmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String password = rs.getString("password");

                
                if (!password.startsWith("$2a$") && !password.startsWith("$2b$")) {
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                    try (PreparedStatement updateStmt = con.prepareStatement("UPDATE users SET password = ? WHERE id = ?")) {
                        updateStmt.setString(1, hashedPassword);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                        System.out.println("Đã hash mật khẩu cho user ID: " + id);
                    }
                }
            }

            System.out.println("Migrate hoàn tất! ");

        } catch (Exception e) {
            System.err.println("Lỗi migrate mật khẩu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
