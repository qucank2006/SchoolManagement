package com.example.dacs1.Task;

import com.example.dacs1.Utils.EmailSender;
import javafx.concurrent.Task;

public class SendEmailTask extends Task<Boolean> {
    private final String recipient;
    private final String code;

    public SendEmailTask(String recipient, String code) {
        this.recipient = recipient;
        this.code = code;
    }

    @Override
    protected Boolean call() {
        // Giả lập thời gian gửi email
        for (int i = 0; i <= 100; i += 10) {
            updateProgress(i, 100);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return EmailSender.sendVerificationEmail(recipient, code);
    }
} 