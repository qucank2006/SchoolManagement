package com.example.dacs1.Task;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

public class TaskManager {
    
    public static <T> void executeTask(Task<T> task, ProgressBar progressBar, Runnable onSuccess, Runnable onFailure) {
        // Bind progress bar với task
        if (progressBar != null) {
            progressBar.progressProperty().bind(task.progressProperty());
        }

        // Xử lý kết quả
        task.setOnSucceeded(e -> {
            if (progressBar != null) {
                progressBar.setVisible(false);
            }
            if (onSuccess != null) {
                onSuccess.run();
            }
        });

        task.setOnFailed(e -> {
            if (progressBar != null) {
                progressBar.setVisible(false);
            }
            if (onFailure != null) {
                onFailure.run();
            }
        });

        // Chạy task trong thread riêng
        new Thread(task).start();
    }
} 