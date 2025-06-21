package com.example.dacs1.Controller.Admin;

import com.example.dacs1.DAO.StudentDAO;
import com.example.dacs1.DAO.ActivityLogDAO;
import com.example.dacs1.Model.Student;
import com.example.dacs1.Model.ActivityLog;
import com.example.dacs1.Utils.AlertUtils;
import com.example.dacs1.Utils.UserSession;
import com.example.dacs1.Controller.ListData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class AddStudentController implements Initializable {
    private final AlertUtils alert = new AlertUtils();
    private final StudentDAO studentDAO = new StudentDAO();
    private final ActivityLogDAO activityLogDAO = new ActivityLogDAO();
    private Image image;
    private String imagePath;
    public static boolean isEditMode = false;
    public static Student selectedStudent = null;
    public static String currentClassName = null;

    @FXML private AnchorPane main_form;
    @FXML private TextField studentAddress;
    @FXML private DatePicker studentBirth;
    @FXML private TextField studentCode;
    @FXML private ComboBox<String> studentGender;
    @FXML private TextField studentName;
    @FXML private TextField studentPhoneNum;
    @FXML private TextField studentEmail;
    @FXML private ImageView student_ImageView;
    @FXML private Button student_addBtn;
    @FXML private Button student_importBtn;
    @FXML private Button student_updateBtn;

    private boolean validateInputs() {
        if (studentCode.getText().isEmpty() ||
            studentName.getText().isEmpty() ||
            studentBirth.getValue() == null ||
            studentGender.getValue() == null ||
            studentPhoneNum.getText().isEmpty() ||
            studentEmail.getText().isEmpty() ||
            studentAddress.getText().isEmpty()) {
            alert.showErrorAlert("Lỗi", "Hãy điền đầy đủ thông tin sinh viên");
            return false;
        }
        return true;
    }

    @FXML
    private void handleAddStudent() {
        if (validateInputs()) {
            if (studentDAO.isStudentCodeExists(studentCode.getText())) {
                alert.showErrorAlert("Lỗi", "Mã sinh viên đã tồn tại");
                return;
            }

            Student student = new Student(
                0,
                studentCode.getText(),
                studentName.getText(),
                Date.valueOf(studentBirth.getValue()),
                studentEmail.getText(),
                studentPhoneNum.getText(),
                studentAddress.getText()
            );
            student.setImagePath(imagePath);
            student.setGender(studentGender.getValue());

            if (studentDAO.addStudent(student, currentClassName)) {
                // Ghi log hoạt động
                logActivity("CREATE", student.getStudentCode(), 
                    "Thêm sinh viên: " + student.getFullName() + " vào lớp " + currentClassName);
                
                alert.showSuccessAlert("Thành công", "Thêm sinh viên thành công");
                Stage stage = (Stage) student_addBtn.getScene().getWindow();
                stage.close();
            } else {
                alert.showErrorAlert("Lỗi", "Không thể thêm sinh viên");
            }
        }
    }

    @FXML
    private void handleUpdateStudent() {
        if (validateInputs()) {
            Student student = new Student(
                selectedStudent.getId(),
                selectedStudent.getStudentCode(),
                studentName.getText(),
                Date.valueOf(studentBirth.getValue()),
                studentEmail.getText(),
                studentPhoneNum.getText(),
                studentAddress.getText()
            );
            student.setImagePath(imagePath);
            student.setGender(studentGender.getValue());

            if (studentDAO.updateStudent(student)) {
                // Ghi log hoạt động
                logActivity("UPDATE", student.getStudentCode(), 
                    "Cập nhật thông tin sinh viên: " + student.getFullName());
                
                alert.showSuccessAlert("Thành công", "Cập nhật sinh viên thành công");
                Stage stage = (Stage) student_updateBtn.getScene().getWindow();
                stage.close();
            } else {
                alert.showErrorAlert("Lỗi", "Không thể cập nhật sinh viên");
            }
        }
    }

    @FXML
    private void handleImportImage() {
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = open.showOpenDialog(student_importBtn.getScene().getWindow());
        if (file != null) {
            try {
                imagePath = file.getAbsolutePath();
                image = new Image(file.toURI().toString(), 119, 145, false, true);
                if (image.isError()) {
                    alert.showErrorAlert("Lỗi", "Không thể tải ảnh");
                    return;
                }
                student_ImageView.setImage(image);
            } catch (Exception e) {
                alert.showErrorAlert("Lỗi", "Không thể xử lý ảnh: " + e.getMessage());
            }
        }
    }

    private void setStudentFormData(Student student) {
        if (student == null) return;
        studentCode.setText(student.getStudentCode());
        studentName.setText(student.getFullName());
        if (student.getDateOfBirth() != null)
            studentBirth.setValue(student.getDateOfBirth().toLocalDate());
        studentEmail.setText(student.getEmail());
        studentPhoneNum.setText(student.getPhone());
        studentAddress.setText(student.getAddress());
        studentGender.setValue(student.getGender());
        
        if (student.getImagePath() != null && !student.getImagePath().isEmpty()) {
            try {
                File imageFile = new File(student.getImagePath());
                if (imageFile.exists()) {
                    imagePath = student.getImagePath();
                    image = new Image(imageFile.toURI().toString(), 119, 145, false, true);
                    if (!image.isError()) {
                        student_ImageView.setImage(image);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load gender data
        studentGender.getItems().setAll(ListData.gender);
        
        student_importBtn.setOnAction(event -> handleImportImage());
        student_addBtn.setOnAction(event -> handleAddStudent());
        student_updateBtn.setOnAction(event -> handleUpdateStudent());
        
        student_addBtn.setVisible(!isEditMode);
        student_updateBtn.setVisible(isEditMode);
        
        if (isEditMode && selectedStudent != null) {
            setStudentFormData(selectedStudent);
        }
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
                    "STUDENT",
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
}