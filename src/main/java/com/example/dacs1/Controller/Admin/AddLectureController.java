package com.example.dacs1.Controller.Admin;

import com.example.dacs1.Controller.ListData;
import com.example.dacs1.DAO.DepartmentDAO;
import com.example.dacs1.DAO.LectureDAO;
import com.example.dacs1.DAO.ActivityLogDAO;
import com.example.dacs1.Model.Lecture;
import com.example.dacs1.Model.ActivityLog;
import com.example.dacs1.Utils.AlertUtils;
import com.example.dacs1.Utils.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddLectureController implements Initializable {

    private final AlertUtils alert = new AlertUtils();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final LectureDAO lectureDAO = new LectureDAO();
    private final ActivityLogDAO activityLogDAO = new ActivityLogDAO();

    private Image image;
    private String imagePath;
    public static boolean isEditMode = false;
    public static boolean shouldReloadLectureTable = false;
    public static Lecture selectedLecture = null;
    public static Stage addLectureStage;

    @FXML  private AnchorPane main_form;
    @FXML  private TextField lectureAddress;
    @FXML  private DatePicker lectureBirth;
    @FXML  private TextField lectureCode;
    @FXML  private ComboBox<String> lectureDegree;
    @FXML  private ComboBox<String> lectureDepartment;
    @FXML  private TextField lectureEmail;
    @FXML  private TextField lectureName;
    @FXML  private TextField lecturePhoneNum;
    @FXML  private ImageView lecture_ImageView;
    @FXML  private Button lecture_addBtn;
    @FXML  private Button lecture_importBtn;
    @FXML  private Button lecture_updateBtn;

    private boolean validateInputs() {
        if (lectureCode.getText().isEmpty() ||
            lectureName.getText().isEmpty() ||
            lectureBirth.getValue() == null ||
            lectureDegree.getValue() == null ||
            lectureDepartment.getValue() == null ||
            lecturePhoneNum.getText().isEmpty() ||
            lectureEmail.getText().isEmpty() ||
            lectureAddress.getText().isEmpty()) {
            alert.showErrorAlert("Lỗi", "Hãy điền đầy đủ thông tin giảng viên");
            return false;
        }
        return true;
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
                    "LECTURE",
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

    @FXML
    private void handleAddLecture() {
        if (validateInputs()) {
            Lecture lecture = new Lecture(
                0,
                lectureCode.getText(),
                lectureName.getText(),
                Date.valueOf(lectureBirth.getValue()),
                lectureDegree.getValue(),
                lectureDepartment.getValue(),
                lecturePhoneNum.getText(),
                lectureEmail.getText(),
                lectureAddress.getText()
            );
            lecture.setImagePath(imagePath);

            LectureDAO lectureDAO = new LectureDAO();
            Lecture addedLecture = lectureDAO.addLecture(lecture);
            
            if (addedLecture != null) {
                // Ghi log hoạt động
                logActivity("CREATE", lecture.getLectureCode(), 
                    "Thêm giảng viên: " + lecture.getLectureName() + " - " + lecture.getDepartment());
                
                alert.showSuccessAlert("Thành công", "Thêm giảng viên thành công");
                
                // Open create account form
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CreateAccount.fxml"));
                    Parent root = loader.load();
                    CreateAccountController controller = loader.getController();
                    controller.setRole("Lecture");
                    controller.setEmail(lecture.getEmail());
                    
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Tạo tài khoản");
                    stage.show();
                    
                    // Xử lý khi form tạo tài khoản đóng lại
                    stage.setOnHidden(event -> {
                        if (controller.isSuccess()) {
                            String username = controller.getUsername();
                            String password = controller.getPassword();
                            if (lectureDAO.createLectureAccount(addedLecture, username, password)) {
                                alert.showSuccessAlert("Thành công", "Tạo tài khoản giảng viên thành công");
                                // Có thể thêm logic refresh table nếu cần
                            } else {
                                alert.showErrorAlert("Lỗi", "Không thể tạo tài khoản giảng viên");
                            }
                        }
                    });
                    
                    // Close current window
                    ((Stage) lecture_addBtn.getScene().getWindow()).close();
                } catch (IOException e) {
                    e.printStackTrace();
                    alert.showErrorAlert("Lỗi", "Không thể mở form tạo tài khoản");
                }
            } else {
                alert.showErrorAlert("Lỗi", "Không thể thêm giảng viên");
            }
        }
    }
    public void handleUpdateLecture() {
        if (lectureCode.getText().isEmpty() ||
            lectureName.getText().isEmpty() ||
            lectureBirth.getValue() == null ||
            lectureDegree.getValue() == null ||
            lectureDepartment.getValue() == null ||
            lecturePhoneNum.getText().isEmpty() ||
            lectureEmail.getText().isEmpty() ||
            lectureAddress.getText().isEmpty()) {
            alert.showErrorAlert("Lỗi", "Hãy điền đầy đủ thông tin giảng viên");
            return;
        }
        Lecture lecture = new Lecture(
            0,
            lectureCode.getText(),
            lectureName.getText(),
            Date.valueOf(lectureBirth.getValue()),
            lectureDegree.getValue(),
            lectureDepartment.getValue(),
            lecturePhoneNum.getText(),
            lectureEmail.getText(),
            lectureAddress.getText(),
            imagePath
        );
        if (lectureDAO.updateLecture(lecture)) {
            // Ghi log hoạt động
            logActivity("UPDATE", lecture.getLectureCode(), 
                "Cập nhật thông tin giảng viên: " + lecture.getLectureName());
            
            alert.showSuccessAlert("Thành công", "Cập nhật giảng viên thành công");
            AddLectureController.shouldReloadLectureTable = true;
            javafx.stage.Stage stage = (javafx.stage.Stage) lecture_updateBtn.getScene().getWindow();
            stage.close();
        } else {
            alert.showErrorAlert("Lỗi", "Không thể cập nhật giảng viên");
        }
    }
    public void handleimportButton(){
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = open.showOpenDialog(lecture_importBtn.getScene().getWindow());
        if (file != null) {
            try {
                imagePath = file.getAbsolutePath();
                image = new Image(file.toURI().toString(), 119, 145, false, true);
                if (image.isError()) {
                    alert.showErrorAlert("Lỗi", "Không thể tải ảnh");
                    return;
                }
                lecture_ImageView.setImage(image);
            } catch (Exception e) {
                alert.showErrorAlert("Lỗi", "Không thể xử lý ảnh: " + e.getMessage());
            }
        }
    }
    public void loadDegree(){
        List<String> listD = new ArrayList<>();
        for (String degree : ListData.degree) {
            listD.add(degree);
        }

        ObservableList listData = FXCollections.observableArrayList(listD);
        lectureDegree.setItems(listData);
    }
    public void loadDepartmentAbbreviateNames() {
        lectureDepartment.getItems().setAll(departmentDAO.getAllAbbreviateNames());
        lectureDepartment.setPromptText("Chọn ...");
    }
    private void setLectureFormData(Lecture lecture) {
        if (lecture == null) return;
        lectureCode.setText(lecture.getLectureCode());
        lectureName.setText(lecture.getLectureName());
        if (lecture.getDateOfBirth() != null)
            lectureBirth.setValue(lecture.getDateOfBirth().toLocalDate());
        lectureDegree.setValue(lecture.getDegree());
        lectureDepartment.setValue(lecture.getDepartment());
        lecturePhoneNum.setText(lecture.getPhoneNum());
        lectureEmail.setText(lecture.getEmail());
        lectureAddress.setText(lecture.getAddress());
        
        if (lecture.getImagePath() != null && !lecture.getImagePath().isEmpty()) {
            try {
                File imageFile = new File(lecture.getImagePath());
                
                if (imageFile.exists()) {
                    imagePath = lecture.getImagePath();
                    image = new Image(imageFile.toURI().toString(), 119, 145, false, true);
                    if (image.isError()) {
                        return;
                    }
                    lecture_ImageView.setImage(image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDegree();
        loadDepartmentAbbreviateNames();
        lecture_importBtn.setOnAction(event -> handleimportButton());
        lecture_addBtn.setOnAction(event -> handleAddLecture());
        lecture_updateBtn.setOnAction(event -> handleUpdateLecture());
        lecture_addBtn.setVisible(!isEditMode);
        lecture_updateBtn.setVisible(isEditMode);
        if (isEditMode && selectedLecture != null) {
            setLectureFormData(selectedLecture);
        }
    }
}
