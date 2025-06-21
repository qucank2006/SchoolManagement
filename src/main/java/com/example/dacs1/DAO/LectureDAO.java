package com.example.dacs1.DAO;

import com.example.dacs1.Model.Lecture;
import com.example.dacs1.Utils.DatabaseConnector;
import com.example.dacs1.Utils.HashPassword;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LectureDAO {
    public List<Lecture> getAllLectures() {
        List<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT * FROM lectures";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String imagePath = rs.getString("image_path");
                Lecture lecture = new Lecture(
                        rs.getInt("id"),
                        rs.getString("lecture_code"),
                        rs.getString("full_name"),
                        rs.getDate("date_of_birth"),
                        rs.getString("degree"),
                        rs.getString("department"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        imagePath
                );
                lectures.add(lecture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lectures;
    }
    public Lecture addLecture(Lecture lecture) {
        String sql = "INSERT INTO lectures (lecture_code, full_name, date_of_birth, department, degree, email, phone, address, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, lecture.getLectureCode());
            ps.setString(2, lecture.getLectureName());
            ps.setDate(3, lecture.getDateOfBirth());
            ps.setString(4, lecture.getDepartment());
            ps.setString(5, lecture.getDegree());
            ps.setString(6, lecture.getEmail());
            ps.setString(7, lecture.getPhoneNum());
            ps.setString(8, lecture.getAddress());
            ps.setString(9, lecture.getImagePath());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        lecture.setId(generatedKeys.getInt(1));
                        return lecture;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean updateLecture(Lecture lecture) {
        String sql = "UPDATE lectures SET full_name=?, date_of_birth=?, degree=?, department=?, phone=?, email=?, address=?, image_path=? WHERE lecture_code=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lecture.getLectureName());
            ps.setDate(2, lecture.getDateOfBirth());
            ps.setString(3, lecture.getDegree());
            ps.setString(4, lecture.getDepartment());
            ps.setString(5, lecture.getPhoneNum());
            ps.setString(6, lecture.getEmail());
            ps.setString(7, lecture.getAddress());
            ps.setString(8, lecture.getImagePath());
            ps.setString(9, lecture.getLectureCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteLecture(String lectureCode) {
        // Lấy user_id của giảng viên trước khi xóa
        int userId = -1;
        String getUserIdSql = "SELECT user_id FROM lectures WHERE lecture_code = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(getUserIdSql)) {
            ps.setString(1, lectureCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Xóa giảng viên
        String deleteLectureSql = "DELETE FROM lectures WHERE lecture_code = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteLectureSql)) {
            ps.setString(1, lectureCode);
            boolean lectureDeleted = ps.executeUpdate() > 0;

            // Nếu xóa giảng viên thành công và có user_id, xóa tài khoản
            if (lectureDeleted && userId != -1) {
                String deleteUserSql = "DELETE FROM users WHERE id = ?";
                try (PreparedStatement userPs = conn.prepareStatement(deleteUserSql)) {
                    userPs.setInt(1, userId);
                    userPs.executeUpdate();
                }
            }
            return lectureDeleted;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getAllLectureNames() {
        List<String> lectureNames = new ArrayList<>();
        String sql = "SELECT full_name FROM lectures";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lectureNames.add(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lectureNames;
    }

    public boolean updateLectureUserId(String lectureCode, int userId) {
        String sql = "UPDATE lectures SET user_id = ? WHERE lecture_code = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, lectureCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createLectureAccount(Lecture lecture, String username, String password) {
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Tạo tài khoản user
            String userSql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
            int userId;
            try (PreparedStatement ps = conn.prepareStatement(userSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setString(2, lecture.getEmail());
                ps.setString(3, HashPassword.hashPassword(password));
                ps.setString(4, "Lecture");
                
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

            // 2. Cập nhật user_id cho giảng viên
            String updateLectureSql = "UPDATE lectures SET user_id = ? WHERE lecture_code = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateLectureSql)) {
                ps.setInt(1, userId);
                ps.setString(2, lecture.getLectureCode());
                
                if (ps.executeUpdate() > 0) {
                    conn.commit(); // Commit transaction nếu thành công
                    return true;
                } else {
                    throw new SQLException("Không thể cập nhật user_id cho giảng viên");
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

    public Lecture getLectureByUsername(String username) {
        String sql = "SELECT l.* FROM lectures l " +
                    "JOIN users u ON l.user_id = u.id " +
                    "WHERE u.username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String imagePath = rs.getString("image_path");
                return new Lecture(
                        rs.getInt("id"),
                        rs.getString("lecture_code"),
                        rs.getString("full_name"),
                        rs.getDate("date_of_birth"),
                        rs.getString("degree"),
                        rs.getString("department"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        imagePath
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getLectureCount() {
        String sql = "SELECT COUNT(*) FROM lectures";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
