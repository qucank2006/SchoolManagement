package com.example.dacs1.Task;

import com.example.dacs1.DAO.UserDAO;
import javafx.concurrent.Task;

public class UpdatePasswordTask extends Task<Boolean> {
    private final String email;
    private final String newPassword;
    private final UserDAO userDAO;

    public UpdatePasswordTask(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
        this.userDAO = new UserDAO();
    }

    @Override
    protected Boolean call() {
        // Giả lập thời gian xử lý
        for (int i = 0; i <= 100; i += 20) {
            updateProgress(i, 100);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return userDAO.updatePassword(email, newPassword);
    }
} 