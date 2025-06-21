package com.example.dacs1.DAO;

import com.example.dacs1.Model.Student;
import com.example.dacs1.Utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private final Connection connection;

    public StudentDAO() {
        this.connection = DatabaseConnector.getConnection();
    }

    public List<Student> getStudentsByClass(String className) {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students WHERE class = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, className);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("id"),
                    rs.getString("student_code"),
                    rs.getString("full_name"),
                    rs.getDate("date_of_birth"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
                );
                student.setImagePath(rs.getString("image_path"));
                String gender = rs.getString("gender");
                student.setGender(gender);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean addStudent(Student student, String className) {
        String query = "INSERT INTO students (student_code, full_name, date_of_birth, email, phone, address, class, gender, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getFullName());
            stmt.setDate(3, student.getDateOfBirth());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getAddress());
            stmt.setString(7, className);
            stmt.setString(8, student.getGender());
            stmt.setString(9, student.getImagePath());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(Student student) {
        String query = "UPDATE students SET full_name = ?, date_of_birth = ?, email = ?, phone = ?, address = ?, gender = ? WHERE student_code = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getFullName());
            stmt.setDate(2, student.getDateOfBirth());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getGender());
            stmt.setString(7, student.getStudentCode());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(String studentCode) {
        String query = "DELETE FROM students WHERE student_code = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isStudentCodeExists(String studentCode) {
        String query = "SELECT COUNT(*) FROM students WHERE student_code = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getDistinctClassNames() {
        List<String> classNames = new ArrayList<>();
        String query = "SELECT DISTINCT class FROM students WHERE class IS NOT NULL AND class != ''";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                classNames.add(rs.getString("class"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    public int getStudentCount() {
        String query = "SELECT COUNT(*) FROM students";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
