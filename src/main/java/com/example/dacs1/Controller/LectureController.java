package com.example.dacs1.Controller;

import com.example.dacs1.DAO.LectureDAO;
import com.example.dacs1.DAO.ClassDAO;
import com.example.dacs1.DAO.StudentDAO;
import com.example.dacs1.Model.Lecture;
import com.example.dacs1.Model.Class;
import com.example.dacs1.Model.Student;
import com.example.dacs1.Utils.AlertUtils;
import com.example.dacs1.Utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.dacs1.Controller.ChatViewController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LectureController implements Initializable {

    @FXML private ComboBox<String> LectureForm_classBox;
    @FXML private TextField LectureForm_search;
    @FXML private TableColumn<Student, String> LectureForm_stuCodeCol;
    @FXML private TableColumn<Student, String> LectureForm_stuBirthCol;
    @FXML private TableColumn<Student, String> LectureForm_stuEmailCol;
    @FXML private TableColumn<Student, String> LectureForm_stuGenderCol;
    @FXML private TableColumn<Student, String> LectureForm_stuNameCol;
    @FXML private TableColumn<Student, String> LectureForm_stuPhoneCol;
    @FXML private TableColumn<Student, String> LectureForm_stuAddressCol;
    @FXML private TableView<Student> StudentForm_tableView;
    @FXML private Button chat_nav;
    @FXML private AnchorPane headLectureClassForm;
    @FXML private Button headLecture_nav;
    @FXML private AnchorPane infoForm;
    @FXML private Button info_nav;
    @FXML private Label lectureAddress;
    @FXML private Label lectureBirth;
    @FXML private Label lectureDegree;
    @FXML private Label lectureDepartment;
    @FXML private Label lectureEmail;
    @FXML private Label lectureId;
    @FXML private ImageView lectureImageView;
    @FXML private Label lectureName;
    @FXML private Label lecturePhoneNum;
    @FXML private Button logout_Btn;
    @FXML private Label name_nav;

    private final AlertUtils alert = new AlertUtils();
    private final LectureDAO lectureDAO = new LectureDAO();
    private final ClassDAO classDAO = new ClassDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();
    private Lecture currentLecture;

    private void switchForm(AnchorPane form){
        headLectureClassForm.setVisible(false);
        infoForm.setVisible(false);
        form.setVisible(true);
    }
    
    //điều hướng
    private void handleNavigation() {
        info_nav.setOnAction(e -> {
            switchForm(infoForm);
            loadLectureInfo(); // Load thông tin giảng viên khi chuyển sang tab thông tin
        });
        headLecture_nav.setOnAction(e -> {
            switchForm(headLectureClassForm);
            loadHeadLectureClasses(); // Load danh sách lớp chủ nhiệm
        });
        chat_nav.setOnAction(e -> handleOpenChat());
    }
    
    // Xử lý mở chat
    private void handleOpenChat() {
        try {
            // Lấy thông tin giảng viên hiện tại
            String fullName = UserSession.getInstance().getFullName();
            if (fullName == null || fullName.isEmpty()) {
                alert.showErrorAlert("Lỗi", "Không thể lấy thông tin người dùng!");
                return;
            }
            
            // Load ChatView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ChatView.fxml"));
            Parent root = loader.load();
            
            // Lấy controller và khởi tạo
            ChatViewController chatController = loader.getController();
            chatController.initialize(fullName);
            
            // Tạo stage mới cho chat
            Stage chatStage = new Stage();
            chatStage.setTitle("Chat - " + fullName);
            chatStage.setScene(new Scene(root));
            chatStage.setResizable(true);
            chatStage.setMinWidth(800);
            chatStage.setMinHeight(600);
            
            // Xử lý khi đóng cửa sổ chat
            chatStage.setOnCloseRequest(event -> {
                if (chatController != null) {
                    chatController.shutdown();
                }
            });
            
            chatStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể mở chat!");
        }
    }
    
    // Load danh sách lớp mà giảng viên chủ nhiệm
    private void loadHeadLectureClasses() {
        try {
            String username = UserSession.getInstance().getUserName();
            if (username != null && currentLecture != null) {
                // Lấy danh sách lớp mà giảng viên này chủ nhiệm
                List<Class> classes = classDAO.getClassesByHeadLecture(currentLecture.getLectureName());
                
                ObservableList<String> classNames = FXCollections.observableArrayList();
                for (Class c : classes) {
                    classNames.add(c.getClassName());
                }
                
                LectureForm_classBox.setItems(classNames);
                
                // Nếu có lớp, chọn lớp đầu tiên
                if (!classNames.isEmpty()) {
                    LectureForm_classBox.setValue(classNames.get(0));
                    loadStudentsByClass(classNames.get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể tải danh sách lớp!");
        }
    }
    
    // Load danh sách sinh viên theo lớp
    private void loadStudentsByClass(String className) {
        try {
            studentList.clear();
            List<Student> students = studentDAO.getStudentsByClass(className);
            studentList.addAll(students);
            StudentForm_tableView.setItems(studentList);
        } catch (Exception e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể tải danh sách sinh viên!");
        }
    }
    
    // Setup bảng sinh viên
    private void setupStudentTable() {
        LectureForm_stuCodeCol.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        LectureForm_stuNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        LectureForm_stuBirthCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        LectureForm_stuGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        LectureForm_stuPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        LectureForm_stuEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        LectureForm_stuAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
    }
    
    // Setup tìm kiếm sinh viên
    private void setupStudentSearch() {
        LectureForm_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                StudentForm_tableView.setItems(studentList);
                return;
            }
            
            String searchText = newValue.toLowerCase();
            ObservableList<Student> filteredList = FXCollections.observableArrayList();
            
            for (Student student : studentList) {
                if (student.getStudentCode().toLowerCase().contains(searchText) ||
                    student.getFullName().toLowerCase().contains(searchText) ||
                    student.getEmail().toLowerCase().contains(searchText) ||
                    student.getPhone().toLowerCase().contains(searchText)) {
                    filteredList.add(student);
                }
            }
            
            StudentForm_tableView.setItems(filteredList);
        });
    }
    
    // Load thông tin giảng viên từ database
    private void loadLectureInfo() {
        try {
            String username = UserSession.getInstance().getUserName();
            if (username != null) {
                // Lấy thông tin giảng viên từ database dựa trên username
                currentLecture = lectureDAO.getLectureByUsername(username);
                if (currentLecture != null) {
                    // Hiển thị thông tin lên các label
                    lectureId.setText(currentLecture.getLectureCode());
                    lectureName.setText(currentLecture.getLectureName());
                    lectureBirth.setText(currentLecture.getDateOfBirth().toString());
                    lectureDepartment.setText(currentLecture.getDepartment());
                    lectureDegree.setText(currentLecture.getDegree());
                    lecturePhoneNum.setText(currentLecture.getPhoneNum());
                    lectureEmail.setText(currentLecture.getEmail());
                    lectureAddress.setText(currentLecture.getAddress());
                    
                    // Cập nhật tên hiển thị trên navigation
                    name_nav.setText(currentLecture.getLectureName());
                    
                    // Load ảnh nếu có
                    if (currentLecture.getImagePath() != null && !currentLecture.getImagePath().isEmpty()) {
                        try {
                            Image image = new Image("file:" + currentLecture.getImagePath());
                            lectureImageView.setImage(image);
                        } catch (Exception e) {
                            // Nếu không load được ảnh, sử dụng ảnh mặc định
                            System.out.println("Không thể load ảnh: " + e.getMessage());
                        }
                    }
                } else {
                    alert.showErrorAlert("Lỗi", "Không tìm thấy thông tin giảng viên!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể tải thông tin giảng viên!");
        }
    }
    
    //đăng xuất
    private void handleLogout() {
        boolean isConfirmed = alert.showConfirmationAlert("Xác nhận", "Bạn có muốn đăng xuất không?");
        if (isConfirmed) {
            try {
                // Clear user session
                UserSession.getInstance().cleanUserSession();

                Parent root = FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml"));
                Stage stage = (Stage) logout_Btn.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Đăng nhập");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                alert.showErrorAlert("Lỗi", "Đăng xuất thất bại");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logout_Btn.setOnAction(event -> handleLogout());
        handleNavigation();
        
        // Setup bảng sinh viên và tìm kiếm
        setupStudentTable();
        setupStudentSearch();
        
        // Setup sự kiện chọn lớp
        LectureForm_classBox.setOnAction(event -> {
            String selectedClass = LectureForm_classBox.getValue();
            if (selectedClass != null) {
                loadStudentsByClass(selectedClass);
            }
        });
        
        // Hiển thị tên giảng viên từ UserSession ngay khi khởi tạo
        String fullName = UserSession.getInstance().getFullName();
        if (fullName != null && !fullName.isEmpty()) {
            name_nav.setText(fullName);
        }
        
        // Load thông tin giảng viên khi khởi tạo và hiển thị tab thông tin
        loadLectureInfo();
        switchForm(infoForm);
    }
}