package com.example.dacs1.Model;

import java.sql.Timestamp;

public class ActivityLog {
    private int id;
    private int userId;
    private String username;
    private String actionType; // CREATE, UPDATE, DELETE
    private String entityType; // STUDENT, LECTURE, CLASS, DEPARTMENT, ADMIN
    private String entityName;
    private String description;
    private Timestamp createdAt;

    public ActivityLog() {
    }

    public ActivityLog(int id, int userId, String username, String actionType, String entityType, 
                      String entityName, String description, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.actionType = actionType;
        this.entityType = entityType;
        this.entityName = entityName;
        this.description = description;
        this.createdAt = createdAt;
    }

    public ActivityLog(int userId, String username, String actionType, String entityType, 
                      String entityName, String description) {
        this.userId = userId;
        this.username = username;
        this.actionType = actionType;
        this.entityType = entityType;
        this.entityName = entityName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", actionType='" + actionType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityName='" + entityName + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 