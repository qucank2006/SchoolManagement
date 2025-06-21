package com.example.dacs1.Controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.dacs1.DAO.UserDAO;
import com.example.dacs1.DAO.LectureDAO;
import com.example.dacs1.DAO.ActivityLogDAO;
import com.example.dacs1.Model.ActivityLog;
import com.example.dacs1.Utils.AlertUtils;
import com.example.dacs1.Utils.HashPassword;
import com.example.dacs1.Utils.UserSession;
import com.example.dacs1.Model.Admin;

public class CreateAccountController implements Initializable {

    @FXML
    private Button comfirmBtn;

    @FXML
    private PasswordField comfirmPassField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPassField;

    private String role = "Lecture"; // mặc định
    private String defaultEmail = "";
    private String email = "";
    private Admin admin;
    private boolean success = false;
    private final ActivityLogDAO activityLogDAO = new ActivityLogDAO();

    public void setRole(String role) {
        this.role = role;
    }
    public void setDefaultEmail(String email) {
        this.defaultEmail = email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    public boolean isSuccess() {
        return success;
    }
    public String getUsername() {
        return usernameField.getText();
    }
    public String getPassword() {
        return newPassField.getText();
    }

    public void handleComfirm() {
        String username = usernameField.getText();
        String password = newPassField.getText();
        String confirmPassword = comfirmPassField.getText();

        AlertUtils alert = new AlertUtils();
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            alert.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        if (!password.equals(confirmPassword)) {
            alert.showErrorAlert("Lỗi", "Mật khẩu xác nhận không khớp");
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.isEmailExists(email)) {
            alert.showErrorAlert("Lỗi", "Email đã tồn tại");
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            alert.showErrorAlert("Lỗi", "Tên đăng nhập đã tồn tại");
            return;
        }

        success = true;
        ((javafx.stage.Stage) comfirmBtn.getScene().getWindow()).close();

        // Ghi log hoạt động
        logActivity("CREATE", username, "Tạo tài khoản cho " + role + ": " + username);
    }

    // Ghi log hoạt động
    private void logActivity(String actionType, String entityName, String description) {
        try {
            String username = UserSession.getInstance().getUserName();
            if (username != null) {
                ActivityLog log = new ActivityLog(
                    0, // user_id sẽ được set NULL trong database
                    username,
                    actionType,
                    "ACCOUNT",
                    entityName,
                    description
                );
                activityLogDAO.addActivityLog(log);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Không hiển thị lỗi cho user vì đây chỉ là logging
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comfirmBtn.setOnAction(event -> handleComfirm());
        if (!defaultEmail.isEmpty()) {
            emailField.setText(defaultEmail);
        }
    }
}