package com.example.dacs1.DAO;

import com.example.dacs1.Model.Department;
import com.example.dacs1.Utils.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Department d = new Department(
                    rs.getString("department_code"),
                    rs.getString("department_name"),
                    rs.getString("abbreviate_name"),
                    rs.getString("dean"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                departments.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public boolean addDepartment(Department d) {
        String sql = "INSERT INTO departments (department_code, department_name, abbreviate_name, dean, phone, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getDepartmentCode());
            ps.setString(2, d.getDepartmentName());
            ps.setString(3, d.getDepartmentAbbreviateName());
            ps.setString(4, d.getDean());
            ps.setString(5, d.getPhoneNum());
            ps.setString(6, d.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDepartment(Department d) {
        String sql = "UPDATE departments SET department_name=?, abbreviate_name=?, dean=?, phone=?, email=? WHERE department_code=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getDepartmentName());
            ps.setString(2, d.getDepartmentAbbreviateName());
            ps.setString(3, d.getDean());
            ps.setString(4, d.getPhoneNum());
            ps.setString(5, d.getEmail());
            ps.setString(6, d.getDepartmentCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDepartment(String departmentCode) {
        String sql = "DELETE FROM departments WHERE department_code=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, departmentCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getAllAbbreviateNames() {
        List<String> abbreviateNames = new ArrayList<>();
        String sql = "SELECT abbreviate_name FROM departments";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                abbreviateNames.add(rs.getString("abbreviate_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return abbreviateNames;
    }

    public int getDepartmentCount() {
        String sql = "SELECT COUNT(*) FROM departments";
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
