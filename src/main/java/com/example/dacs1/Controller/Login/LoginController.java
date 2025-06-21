package com.example.dacs1.Controller.Login;

import com.example.dacs1.DAO.UserDAO;
import com.example.dacs1.Model.User;
import com.example.dacs1.Task.LoginTask;
import com.example.dacs1.Task.TaskManager;
import com.example.dacs1.Utils.AlertUtils;
import com.example.dacs1.Utils.UserSession;
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
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Hyperlink forgotpass_hyperlink;

    @FXML
    private Button login_btn;

    @FXML
    private TextField loginField, showPassField;

    @FXML
    private PasswordField passField;

    @FXML
    private CheckBox showPassword;

    @FXML
    private ProgressBar loginProgress;

    private final UserDAO userDAO = new UserDAO();
    private final AlertUtils alert = new AlertUtils();

    private void handleShowPassword(ActionEvent event) {
        boolean show = showPassword.isSelected();
        showPassField.setVisible(show);
        showPassField.setManaged(show);
        passField.setVisible(!show);
        passField.setManaged(!show);
    }

    private void handleLogin(ActionEvent event) {
        String username = loginField.getText().trim();
        String password = passField.isVisible() ? passField.getText().trim() : showPassField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            alert.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin đăng nhập!");
            return;
        }

        // Hiển thị progress bar và vô hiệu hóa nút đăng nhập
        loginProgress.setVisible(true);
        login_btn.setDisable(true);

        // Tạo và thực thi task đăng nhập
        LoginTask loginTask = new LoginTask(username, password);
        
        TaskManager.executeTask(
            loginTask,
            loginProgress,
            // Xử lý khi đăng nhập thành công
            () -> {
                User user = loginTask.getValue();
                login_btn.setDisable(false);
                
                if (user != null) {
                    // Store user info in session
                    UserSession.getInstance().createSession(user.getUsername(), user.getRole(), user.getFullName());

                    String role = user.getRole();
                    alert.showSuccessAlert("Đăng nhập thành công!", "Chào mừng " + user.getFullName() + " [" + role + "]!");
                    switchScene(event, role);
                } else {
                    alert.showErrorAlert("Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng!");
                }
            },
            // Xử lý khi đăng nhập thất bại
            () -> {
                login_btn.setDisable(false);
                alert.showErrorAlert("Lỗi", "Có lỗi xảy ra khi đăng nhập!");
            }
        );
    }

    private void handleForgotPassword(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/ForgotPassword.fxml"));
            Stage stage = (Stage) forgotpass_hyperlink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quên mật khẩu");
            stage.show();
        } catch (IOException e) {
            alert.showErrorAlert("Lỗi", "Không thể mở trang quên mật khẩu!");
            e.printStackTrace();
        }
    }

    private void switchScene(ActionEvent event, String role) {
        String fxmlFile;
        switch (role) {
            case "Student":
                fxmlFile = "/View/StudentForm.fxml";
                break;
            case "Lecture":
                fxmlFile = "/View/LectureForm.fxml";
                break;
            case "Admin":
                fxmlFile = "/View/AdminForm.fxml";
                break;
            default:
                alert.showErrorAlert("Lỗi", "Vai trò không hợp lệ!");
                return;
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Hệ Thống Quản Lý - " + role);
            stage.show();
        } catch (IOException e) {
            alert.showErrorAlert("Lỗi", "Không thể tải giao diện " + fxmlFile + " !");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPassField.textProperty().bindBidirectional(passField.textProperty());
        showPassField.setVisible(false);
        showPassField.setManaged(false);

        showPassword.setOnAction(this::handleShowPassword);

        login_btn.setOnAction(this::handleLogin);
        forgotpass_hyperlink.setOnAction(this::handleForgotPassword);
    }
}
