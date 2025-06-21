package com.example.dacs1.Utils;

public class UserSession {

    private static UserSession instance;

    private String userName;    // Tên đăng nhập (vd: admin01)
    private String userRole;
    private String fullName;    // Tên đầy đủ (vd: Lê Đức Hải)

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void createSession(String userName, String userRole, String fullName) {
        this.userName = userName;
        this.userRole = userRole;
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getFullName() {
        return fullName;
    }

    public void cleanUserSession() {
        userName = null;
        userRole = null;
        fullName = null;
    }
} 