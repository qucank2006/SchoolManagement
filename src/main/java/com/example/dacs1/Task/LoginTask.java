package com.example.dacs1.Task;

import com.example.dacs1.DAO.UserDAO;
import com.example.dacs1.Model.User;
import javafx.concurrent.Task;

public class LoginTask extends Task<User> {
    private final String username;
    private final String password;
    private final UserDAO userDAO;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
        this.userDAO = new UserDAO();
    }

    @Override
    protected User call() {
        // Giả lập thời gian xử lý để hiển thị progress
        for (int i = 0; i <= 100; i += 20) {
            updateProgress(i, 100);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return userDAO.validateLogin(username, password);
    }
} 