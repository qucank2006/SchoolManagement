package com.example.dacs1.DAO;

import com.example.dacs1.Model.Class;
import com.example.dacs1.Utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {
    public List<Class> getAllClasses() {
        List<Class> classes = new ArrayList<>();
        String sql = "SELECT * FROM classes";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Class c = new Class(
                        rs.getString("class_name"),
                        rs.getString("course_year"),
                        rs.getString("department"),
                        rs.getString("major"),
                        rs.getString("lecture_name")
                );
                classes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    public boolean addClass(Class c) {
        String sql = "INSERT INTO classes (class_name, course_year, department, major, lecture_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getClassName());
            ps.setString(2, c.getCourse());
            ps.setString(3, c.getDepartment());
            ps.setString(4, c.getMajor());
            ps.setString(5, c.getHeadLecture());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateClass(Class c) {
        String sql = "UPDATE classes SET course_year=?, department=?, major=?, lecture_name=? WHERE class_name=?";
        try (Connection conn = DatabaseConnector.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCourse());
            ps.setString(2, c.getDepartment());
            ps.setString(3, c.getMajor());
            ps.setString(4, c.getHeadLecture());
            ps.setString(5, c.getClassName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteClass(String className) {
        String sql = "DELETE FROM classes WHERE class_name=?";
        try (Connection conn = DatabaseConnector.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getAllClassNames() {
        List<String> classNames = new ArrayList<>();
        String sql = "SELECT class_name FROM classes";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                classNames.add(rs.getString("class_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    public List<Class> getClassesByHeadLecture(String headLectureName) {
        List<Class> classes = new ArrayList<>();
        String sql = "SELECT * FROM classes WHERE lecture_name = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, headLectureName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Class c = new Class(
                        rs.getString("class_name"),
                        rs.getString("course_year"),
                        rs.getString("department"),
                        rs.getString("major"),
                        rs.getString("lecture_name")
                );
                classes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    public int getClassCount() {
        String sql = "SELECT COUNT(*) FROM classes";
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
