package com.example.dacs1.DAO;

import com.example.dacs1.Model.Admin;
import com.example.dacs1.Utils.DatabaseConnector;
import com.example.dacs1.Utils.HashPassword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM admins";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Admin admin = new Admin(
                    rs.getString("admin_code"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    public boolean addAdmin(Admin admin, String username, String password) {
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Tạo tài khoản user
            String userSql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
            int userId;
            try (PreparedStatement ps = conn.prepareStatement(userSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setString(2, admin.getEmail());
                ps.setString(3, HashPassword.hashPassword(password));
                ps.setString(4, "Admin");
                
                if (ps.executeUpdate() > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    } else {
                        throw new SQLException("Không thể lấy ID của user mới tạo");
                    }
                } else {
                    throw new SQLException("Không thể tạo user");
                }
            }

            // 2. Thêm admin với user_id
            String adminSql = "INSERT INTO admins (admin_code, full_name, email, phone, user_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(adminSql)) {
                ps.setString(1, admin.getAdminCode());
                ps.setString(2, admin.getFullName());
                ps.setString(3, admin.getEmail());
                ps.setString(4, admin.getPhoneNum());
                ps.setInt(5, userId);
                
                if (ps.executeUpdate() > 0) {
                    conn.commit(); // Commit transaction nếu thành công
                    return true;
                } else {
                    throw new SQLException("Không thể thêm admin");
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto commit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean updateAdmin(Admin admin) {
        String sql = "UPDATE admins SET full_name=?, email=?, phone=? WHERE admin_code=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getFullName());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getPhoneNum());
            ps.setString(4, admin.getAdminCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAdmin(String adminCode) {
        String sql = "DELETE FROM admins WHERE admin_code=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, adminCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM admins WHERE email=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int createUserAndGetId(String username, String email, String password, String role) {
        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);
            
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateAdminUserId(String adminCode, int userId) {
        String sql = "UPDATE admins SET user_id = ? WHERE admin_code = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, adminCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
