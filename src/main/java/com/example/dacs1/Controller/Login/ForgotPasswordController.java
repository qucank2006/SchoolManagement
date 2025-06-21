package com.example.dacs1.Controller.Login;

import com.example.dacs1.DAO.UserDAO;
import com.example.dacs1.Task.SendEmailTask;
import com.example.dacs1.Task.TaskManager;
import com.example.dacs1.Task.UpdatePasswordTask;
import com.example.dacs1.Utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {

    @FXML
    private Button applyBtn;

    @FXML
    private TextField codeField, emailField;

    @FXML
    private PasswordField newPassField, comfirmPassField;

    @FXML
    private Button sendCodeBtn;

    @FXML
    private Hyperlink returnLogin_hyperlink;

    @FXML
    private ProgressBar progressBar;

    private final UserDAO userDAO = new UserDAO();
    private final AlertUtils alert = new AlertUtils();
    private String verificationCode;
    private String userEmail;

    public void handleSendCode(ActionEvent event) {
        userEmail = emailField.getText().trim();

        if (userEmail.isEmpty() || !userEmail.contains("@")) {
            alert.showErrorAlert("Lỗi", "Vui lòng nhập email hợp lệ!");
            return;
        }

        if (!userDAO.isEmailExists(userEmail)) {
            alert.showErrorAlert("Lỗi", "Email không tồn tại trong hệ thống!");
            return;
        }

        verificationCode = generateCode();
        progressBar.setVisible(true);
        sendCodeBtn.setDisable(true);

        SendEmailTask emailTask = new SendEmailTask(userEmail, verificationCode);
        
        TaskManager.executeTask(
            emailTask,
            progressBar,
            // Xử lý khi gửi email thành công
            () -> {
                boolean isSent = emailTask.getValue();
                sendCodeBtn.setDisable(false);
                if (isSent) {
                    alert.showSuccessAlert("Thành công", "Mã xác nhận đã được gửi tới email của bạn!");
                } else {
                    alert.showErrorAlert("Lỗi", "Gửi email thất bại, vui lòng thử lại!");
                }
            },
            // Xử lý khi gửi email thất bại
            () -> {
                sendCodeBtn.setDisable(false);
                alert.showErrorAlert("Lỗi", "Có lỗi khi gửi email!");
            }
        );
    }

    public void handleResetPassword(ActionEvent event) {
        String enteredCode = codeField.getText().trim();
        String newPassword = newPassField.getText().trim();
        String confirmPassword = comfirmPassField.getText().trim();

        if (enteredCode.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            alert.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!enteredCode.equals(verificationCode)) {
            alert.showErrorAlert("Lỗi", "Mã xác nhận không chính xác!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            alert.showErrorAlert("Lỗi", "Mật khẩu nhập lại không khớp!");
            return;
        }

        progressBar.setVisible(true);
        applyBtn.setDisable(true);

        UpdatePasswordTask updateTask = new UpdatePasswordTask(userEmail, newPassword);
        
        TaskManager.executeTask(
            updateTask,
            progressBar,
            // Xử lý khi cập nhật mật khẩu thành công
            () -> {
                boolean isUpdated = updateTask.getValue();
                applyBtn.setDisable(false);
                if (isUpdated) {
                    alert.showSuccessAlert("Thành công", "Mật khẩu đã được đặt lại thành công!");
                    switchToLoginScene();
                } else {
                    alert.showErrorAlert("Lỗi", "Cập nhật mật khẩu thất bại!");
                }
            },
            // Xử lý khi cập nhật mật khẩu thất bại
            () -> {
                applyBtn.setDisable(false);
                alert.showErrorAlert("Lỗi", "Có lỗi khi cập nhật mật khẩu!");
            }
        );
    }

    @FXML
    public void handleReturnToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml"));
            Stage stage = (Stage) returnLogin_hyperlink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void switchToLoginScene() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml"));
            Scene loginScene = new Scene(loginRoot);

            Stage currentStage = (Stage) returnLogin_hyperlink.getScene().getWindow();
            currentStage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateCode() {

        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applyBtn.setOnAction(this::handleResetPassword);
        sendCodeBtn.setOnAction(this::handleSendCode);
        returnLogin_hyperlink.setOnAction(this::handleReturnToLogin);
    }
}
