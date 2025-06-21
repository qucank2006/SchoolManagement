package com.example.dacs1.DAO;

import com.example.dacs1.Model.ActivityLog;
import com.example.dacs1.Utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDAO {
    
    public boolean addActivityLog(ActivityLog activityLog) {
        String sql = "INSERT INTO activity_logs (user_id, username, action_type, entity_type, entity_name, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Set user_id to NULL if it's 0 or invalid
            if (activityLog.getUserId() <= 0) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, activityLog.getUserId());
            }
            
            ps.setString(2, activityLog.getUsername());
            ps.setString(3, activityLog.getActionType());
            ps.setString(4, activityLog.getEntityType());
            ps.setString(5, activityLog.getEntityName());
            ps.setString(6, activityLog.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ActivityLog> getRecentActivityLogs(int limit) {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM activity_logs ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ActivityLog log = new ActivityLog(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("action_type"),
                    rs.getString("entity_type"),
                    rs.getString("entity_name"),
                    rs.getString("description"),
                    rs.getTimestamp("created_at")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public List<ActivityLog> getActivityLogsByUser(String username) {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM activity_logs WHERE username = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ActivityLog log = new ActivityLog(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("action_type"),
                    rs.getString("entity_type"),
                    rs.getString("entity_name"),
                    rs.getString("description"),
                    rs.getTimestamp("created_at")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public List<ActivityLog> getActivityLogsByEntity(String entityType) {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM activity_logs WHERE entity_type = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entityType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ActivityLog log = new ActivityLog(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("action_type"),
                    rs.getString("entity_type"),
                    rs.getString("entity_name"),
                    rs.getString("description"),
                    rs.getTimestamp("created_at")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public List<ActivityLog> getAllActivityLogs() {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM activity_logs ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ActivityLog log = new ActivityLog(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("action_type"),
                    rs.getString("entity_type"),
                    rs.getString("entity_name"),
                    rs.getString("description"),
                    rs.getTimestamp("created_at")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
} 