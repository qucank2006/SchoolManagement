package com.example.dacs1.Controller.Admin;

import com.example.dacs1.Controller.ChatViewController;
import com.example.dacs1.Model.*;
import com.example.dacs1.Model.Class;
import com.example.dacs1.Model.ActivityLog;
import com.example.dacs1.Utils.AlertUtils;
import com.example.dacs1.DAO.DepartmentDAO;
import com.example.dacs1.DAO.LectureDAO;
import com.example.dacs1.DAO.ClassDAO;
import com.example.dacs1.DAO.AdminDAO;
import com.example.dacs1.DAO.StudentDAO;
import com.example.dacs1.DAO.ActivityLogDAO;
import com.example.dacs1.Utils.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

public class AdminController implements Initializable {
    private final AlertUtils alert = new AlertUtils();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final LectureDAO lectureDAO = new LectureDAO();
    private final ClassDAO classDAO = new ClassDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final ActivityLogDAO activityLogDAO = new ActivityLogDAO();

    // Khai báo các nút điều hướng
    @FXML private Label name_nav;
    @FXML private Button admin_nav;
    @FXML private Button class_nav;
    @FXML private Button department_nav;
    @FXML private Button chat_nav;
    @FXML private Button home_nav;
    @FXML private Button lecture_nav;
    @FXML private Button student_nav;
    @FXML private Button logout_Btn;
    // Khai báo các form
    @FXML private AnchorPane adminForm;
    @FXML private AnchorPane classForm;
    @FXML private AnchorPane departmentForm;
    @FXML private AnchorPane homeForm;
    @FXML private AnchorPane lectureForm;
    @FXML private AnchorPane studentForm;

    // Khai báo table, button cho giảng viên
    @FXML private TableView<Lecture> lectureTableView;
    @FXML private TableColumn<Lecture, String> lectureCode_col;
    @FXML private TableColumn<Lecture, String> lectureName_col;
    @FXML private TableColumn<Lecture, String> lectureDegree_col;
    @FXML private TableColumn<Lecture, String> lectureDepartment_col;
    @FXML private Button lectureForm_Addbtn;
    @FXML private Button lectureForm_Fixbtn;
    @FXML private Button lectureForm_Deletebtn;
    @FXML private Button lectureForm_Clearbtn;
    @FXML private Label lectureCode_label, lectureName_label, lectureBirth_label, lectureDegree_label;
    @FXML private Label lectureDepartment_label, lecturePhoneNum_label, lectureEmail_label, lectureAddress_label;
    @FXML private ImageView AdminLectureImage;
    @FXML private TextField LectureForm_search;
    // Khai báo admin
    @FXML private TableView<Admin> AdminForm_tableView;
    @FXML private TableColumn<Admin, String> AdminForm_codeCol;
    @FXML private TableColumn<Admin, String> AdminForm_nameCol;
    @FXML private TableColumn<Admin, String> AdminForm_emailCol;
    @FXML private TableColumn<Admin, String> AdminForm_phoneCol;
    @FXML private Button AdminForm_addBtn;
    @FXML private Button AdminForm_clearBtn;
    @FXML private Button AdminForm_deleteBtn;
    @FXML private Button AdminForm_fixBtn;
    @FXML private TextField AdminForm_codeField;
    @FXML private TextField AdminForm_nameField;
    @FXML private TextField AdminForm_emailField;
    @FXML private TextField AdminForm_phoneField;
    @FXML private TextField AdminForm_search;
    // khai báo table, textfield, button cho khoa
    @FXML private TableView<Department> departmentTableView;
    @FXML private TableColumn<Department, String> departmentCode_col;
    @FXML private TableColumn<Department, String> departmentName_col;
    @FXML private TableColumn<Department, String> departmentAbbreviateName_col;
    @FXML private TableColumn<Department, String> dean_col;
    @FXML private TableColumn<Department, String> phoneNum_col;
    @FXML private TableColumn<Department, String> email_col;
    @FXML private TextField departmentCode_textField;
    @FXML private TextField departmentName_textField;
    @FXML private TextField departmentAbbreviateName_textfield;
    @FXML private TextField dean_Textfield;
    @FXML private TextField phoneNum_textField;
    @FXML private TextField email_textField;
    @FXML private Button DepartmentForm_Addbtn;
    @FXML private Button DepartmentForm_Fixbtn;
    @FXML private Button DepartmentForm_Deletebtn;
    @FXML private Button DepartmentForm_Clearbtn;
    //Khai báo biến cho lớp
    @FXML private Button ClassForm_addBtn;
    @FXML private Button ClassForm_fixBtn;
    @FXML private Button ClassForm_deleteBtn;
    @FXML private Button ClassForm_clearBtn;
    @FXML private TextField ClassForm_Search;
    @FXML private TextField ClassForm_classField;
    @FXML private TextField ClassForm_courseField;
    @FXML private TextField ClassForm_majorField;
    @FXML private ComboBox<String> ClassForm_departmentBox;
    @FXML private ComboBox<String> ClassForm_HeadLectureBox;
    @FXML private TableView<Class> ClassForm_tableView;
    @FXML private TableColumn<Class, String> ClassForm_classCol;
    @FXML private TableColumn<Class, String> ClassForm_courseCol;
    @FXML private TableColumn<Class, String> ClassForm_departmentCol;
    @FXML private TableColumn<Class, String> ClassForm_majorForm;
    @FXML private TableColumn<Class, String> ClassForm_headMasterCol;
    // Khai báo table, button cho sinh viên
    @FXML private TableView<Student> StudentForm_tableView;
    @FXML private TableColumn<Student, String> StudentForm_stuCodeCol;
    @FXML private TableColumn<Student, String> StudentForm_stuNameCol;
    @FXML private TableColumn<Student, Date> StudentForm_stuBirthCol;
    @FXML private TableColumn<Student, String> StudentForm_stuGenderCol;
    @FXML private TableColumn<Student, String> StudentForm_stuPhoneCol;
    @FXML private TableColumn<Student, String> StudentForm_stuEmailCol;
    @FXML private TableColumn<Student, String> StudentForm_stuAddressCol;
    @FXML private ComboBox<String> StudentForm_classBox;
    @FXML private Label StudentForm_headLectureLabel;
    @FXML private TextField StudentForm_search;
    @FXML private Button StudentForm_addBtn;
    @FXML private Button StudentForm_fixBtn;
    @FXML private Button StudentForm_deleteBtn;
    @FXML private Button StudentForm_clearBtn;

    // Khai báo các label count cho homeForm
    @FXML private Label studentCount;
    @FXML private Label LectureCount;
    @FXML private Label classCount;
    @FXML private Label apartmentCount;

    // Khai báo bảng Activity Log
    @FXML private TableView<ActivityLog> activityLogTableView;
    @FXML private TableColumn<ActivityLog, Timestamp> activityLogTimeCol;
    @FXML private TableColumn<ActivityLog, String> activityLogUserCol;
    @FXML private TableColumn<ActivityLog, String> activityLogActionCol;
    @FXML private TableColumn<ActivityLog, String> activityLogEntityCol;
    @FXML private TableColumn<ActivityLog, String> activityLogEntityNameCol;
    @FXML private TableColumn<ActivityLog, String> activityLogDescriptionCol;

    private Department selectedDepartment;
    private final ObservableList<Department> departmentList = FXCollections.observableArrayList();
    private final ObservableList<Lecture> lectureList = FXCollections.observableArrayList();
    private final ObservableList<Class> classList = FXCollections.observableArrayList();
    private final ObservableList<Admin> adminList = FXCollections.observableArrayList();
    private Admin selectedAdmin;
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();
    private Student selectedStudent;

    private final ObservableList<ActivityLog> activityLogList = FXCollections.observableArrayList();

    private void switchForm(AnchorPane form) {
        homeForm.setVisible(false);
        adminForm.setVisible(false);
        classForm.setVisible(false);
        departmentForm.setVisible(false);
        lectureForm.setVisible(false);
        studentForm.setVisible(false);
        form.setVisible(true);
    }
    // Xử lý sự kiện điều hướng
    private void handleNavigation() {
        home_nav.setOnAction(event -> {
            switchForm(homeForm);
            updateDashboardCounts();
            loadActivityLogs(); // Load dữ liệu Activity Log khi chuyển về trang chủ
        });
        admin_nav.setOnAction(event -> switchForm(adminForm));
        class_nav.setOnAction(event -> switchForm(classForm));
        department_nav.setOnAction(event -> {
            switchForm(departmentForm);
            setupDepartmentTable();
            loadDepartmentData();
        });
        lecture_nav.setOnAction(event -> {
            switchForm(lectureForm);
            setupLectureTable();
            loadLectureData();
        });
        student_nav.setOnAction(event -> {
            switchForm(studentForm);
            // Luôn khởi tạo lại form sinh viên để cập nhật danh sách lớp trong ComboBox
            initializeStudentForm();
        });
    }
    // Đăng xuất
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

    /**
     * Phương thức trung tâm để cập nhật sĩ số và làm mới các bảng liên quan.
     */
    private void updateAndRefreshTables() {
        // 1. Làm mới bảng hiển thị danh sách lớp học
        refreshClassTable();

        // 2. Làm mới bảng sinh viên cho lớp hiện tại đang được chọn (nếu có)
        String selectedClass = StudentForm_classBox.getValue();
        if (selectedClass != null && !selectedClass.isEmpty()) {
            loadStudentData(selectedClass);
        } else {
            // Nếu không có lớp nào được chọn, hãy xóa sạch bảng sinh viên
            studentList.clear();
        }
    }

    /* ========== QUẢN LÝ SINH VIÊN ========== */
    private void initializeStudentForm() {
        setupStudentTable();
        setupStudentSearch();
        ObservableList<String> classNames = FXCollections.observableArrayList();
        for (Class c : classList) {
            classNames.add(c.getClassName());
        }
        StudentForm_classBox.setItems(classNames);
        StudentForm_classBox.setOnAction(event -> {
            String selectedClass = StudentForm_classBox.getValue();
            if (selectedClass != null) {
                loadStudentData(selectedClass);
                updateHeadLecturerInfo(selectedClass);
            }
        });
    }
    private void updateHeadLecturerInfo(String className) {
        for (Class c : classList) {
            if (c.getClassName().equals(className)) {
                String headLecturerName = c.getHeadLecture();
                StudentForm_headLectureLabel.setText(headLecturerName);
                break;
            }
        }
    }
    private void setupStudentTable() {
        StudentForm_stuCodeCol.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        StudentForm_stuNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        StudentForm_stuBirthCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        StudentForm_stuEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        StudentForm_stuPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        StudentForm_stuAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        StudentForm_stuGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }
    private void loadStudentData(String className) {
        studentList.clear();
        studentList.addAll(studentDAO.getStudentsByClass(className));
        StudentForm_tableView.setItems(studentList);
    }
    @FXML
    private void handleAddStudent() {
        if (StudentForm_classBox.getValue() == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn lớp trước khi thêm sinh viên");
            return;
        }
        try {
            AddStudentController.isEditMode = false;
            AddStudentController.selectedStudent = null;
            AddStudentController.currentClassName = StudentForm_classBox.getValue();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddStudent.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> {
                // Cập nhật và làm mới
                updateAndRefreshTables();
                updateDashboardCounts(); // Cập nhật số liệu thống kê
            });
        } catch (IOException e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể mở form thêm sinh viên");
        }
    }
    @FXML
    private void handleEditStudent() {
        if (selectedStudent == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn sinh viên cần sửa");
            return;
        }
        try {
            AddStudentController.isEditMode = true;
            AddStudentController.selectedStudent = selectedStudent;
            AddStudentController.currentClassName = StudentForm_classBox.getValue();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddStudent.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> {
                // Cập nhật và làm mới
                updateAndRefreshTables();
                updateDashboardCounts(); // Cập nhật số liệu thống kê
            });
        } catch (IOException e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể mở form sửa sinh viên");
        }
    }
    @FXML
    private void handleDeleteStudent() {
        if (selectedStudent == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn sinh viên cần xóa");
            return;
        }

        boolean isConfirmed = alert.showConfirmationAlert("Xác nhận", "Bạn có chắc chắn muốn xóa sinh viên này?");
        if (isConfirmed) {
            if (studentDAO.deleteStudent(selectedStudent.getStudentCode())) {
                alert.showSuccessAlert("Thành công", "Xóa sinh viên thành công");
                // Ghi log hoạt động
                logActivity("DELETE", "STUDENT", selectedStudent.getStudentCode(), 
                           "Xóa sinh viên: " + selectedStudent.getFullName());
                // Cập nhật và làm mới
                updateAndRefreshTables();
                updateDashboardCounts(); // Cập nhật số liệu thống kê
            } else {
                alert.showErrorAlert("Lỗi", "Không thể xóa sinh viên");
            }
        }
    }
    @FXML
    private void handleClearStudent() {
        StudentForm_tableView.getSelectionModel().clearSelection();
        selectedStudent = null;
    }
    @FXML
    private void onTableClickedStudent() {
        selectedStudent = StudentForm_tableView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            StudentForm_fixBtn.setDisable(false);
            StudentForm_deleteBtn.setDisable(false);
        } else {
            StudentForm_fixBtn.setDisable(true);
            StudentForm_deleteBtn.setDisable(true);
        }
    }
    private void setupStudentSearch() {
        if (StudentForm_search == null) return;
        StudentForm_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (studentList == null) return;
            if (StudentForm_tableView == null) return;
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
    /* ========== QUẢN LÝ GIẢNG VIÊN ========== */
    private void setupLectureTable() {
        lectureCode_col.setCellValueFactory(new PropertyValueFactory<>("lectureCode"));
        lectureName_col.setCellValueFactory(new PropertyValueFactory<>("lectureName"));
        lectureDegree_col.setCellValueFactory(new PropertyValueFactory<>("degree"));
        lectureDepartment_col.setCellValueFactory(new PropertyValueFactory<>("department"));
    }
    private void setupLectureSearch() {
        if (LectureForm_search == null) return;
        LectureForm_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (lectureList == null) return;
            if (lectureTableView == null) return;
            if (newValue == null || newValue.isEmpty()) {
                lectureTableView.setItems(lectureList);
                return;
            }
            String searchText = newValue.toLowerCase();
            ObservableList<Lecture> filteredList = FXCollections.observableArrayList();
            for (Lecture lecture : lectureList) {
                if (lecture.getLectureCode().toLowerCase().contains(searchText) ||
                        lecture.getLectureName().toLowerCase().contains(searchText) ||
                        lecture.getDegree().toLowerCase().contains(searchText) ||
                        lecture.getDepartment().toLowerCase().contains(searchText)) {
                    filteredList.add(lecture);
                }
            }
            lectureTableView.setItems(filteredList);
        });
    }
    private void loadLectureData() {
        lectureList.clear();
        lectureList.addAll(lectureDAO.getAllLectures());
        if (lectureTableView != null) {
            lectureTableView.setItems(lectureList);
        }
    }
    private void displayLectureInfo(Lecture lecture) {
        showLectureInfo();
        lectureCode_label.setText(lecture.getLectureCode());
        lectureName_label.setText(lecture.getLectureName());
        lectureBirth_label.setText(lecture.getDateOfBirth().toString());
        lectureDegree_label.setText(lecture.getDegree());
        lectureDepartment_label.setText(lecture.getDepartment());
        lecturePhoneNum_label.setText(lecture.getPhoneNum());
        lectureEmail_label.setText(lecture.getEmail());
        lectureAddress_label.setText(lecture.getAddress());

        if (lecture.getImagePath() != null && !lecture.getImagePath().isEmpty()) {
            File imageFile = new File(lecture.getImagePath());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString(), 119, 145, false, true);
                AdminLectureImage.setImage(image);
            } else {
                AdminLectureImage.setImage(null);
            }
        } else {
            AdminLectureImage.setImage(null);
        }
    }
    private void hideLectureInfo() {
        lectureCode_label.setVisible(false);
        lectureName_label.setVisible(false);
        lectureBirth_label.setVisible(false);
        lectureDegree_label.setVisible(false);
        lectureDepartment_label.setVisible(false);
        lecturePhoneNum_label.setVisible(false);
        lectureEmail_label.setVisible(false);
        lectureAddress_label.setVisible(false);
        AdminLectureImage.setImage(null);
    }
    private void showLectureInfo() {
        lectureCode_label.setVisible(true);
        lectureName_label.setVisible(true);
        lectureBirth_label.setVisible(true);
        lectureDegree_label.setVisible(true);
        lectureDepartment_label.setVisible(true);
        lecturePhoneNum_label.setVisible(true);
        lectureEmail_label.setVisible(true);
        lectureAddress_label.setVisible(true);
    }
    @FXML
    public void handleAddLecture() {
        try {
            AddLectureController.isEditMode = false;
            AddLectureController.selectedLecture = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddLecture.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> {
                loadLectureData();
                updateDashboardCounts(); // Cập nhật số liệu thống kê
            });
        } catch (IOException e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể mở form thêm giảng viên");
        }
    }
    @FXML
    public void handleEditLecture() {
        if (lectureTableView.getSelectionModel().getSelectedItem() == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn giảng viên cần sửa");
            return;
        }
        try {
            AddLectureController.isEditMode = true;
            AddLectureController.selectedLecture = lectureTableView.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddLecture.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> {
                loadLectureData();
                updateDashboardCounts(); // Cập nhật số liệu thống kê
            });
        } catch (IOException e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể mở form sửa giảng viên");
        }
    }
    @FXML
    private void handleClearLecture() {
        lectureTableView.getSelectionModel().clearSelection();
        hideLectureInfo();
    }
    @FXML
    private void handleDeleteLecture() {
        Lecture selectedLecture = lectureTableView.getSelectionModel().getSelectedItem();
        if (selectedLecture == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn giảng viên cần xóa");
            return;
        }

        boolean isConfirmed = alert.showConfirmationAlert("Xác nhận", "Bạn có chắc chắn muốn xóa giảng viên này?");
        if (isConfirmed) {
            if (lectureDAO.deleteLecture(selectedLecture.getLectureCode())) {
                alert.showSuccessAlert("Thành công", "Xóa giảng viên thành công");
                // Ghi log hoạt động
                logActivity("DELETE", "LECTURE", selectedLecture.getLectureCode(), 
                           "Xóa giảng viên: " + selectedLecture.getLectureName());
                loadLectureData();
                updateDashboardCounts(); // Cập nhật số liệu thống kê
            } else {
                alert.showErrorAlert("Lỗi", "Không thể xóa giảng viên");
            }
        }
    }
    @FXML
    private void onTableClickedLecture(javafx.scene.input.MouseEvent event) {
        Lecture selectedLecture = lectureTableView.getSelectionModel().getSelectedItem();
        if (selectedLecture != null) {
            AddLectureController.selectedLecture = selectedLecture;
            lectureForm_Fixbtn.setDisable(false);
            lectureForm_Deletebtn.setDisable(false);
            displayLectureInfo(selectedLecture);
        } else {
            lectureForm_Fixbtn.setDisable(true);
            lectureForm_Deletebtn.setDisable(true);
            hideLectureInfo();
        }
    }
    /* ========== QUẢN LÝ ADMIN ========== */
    private void setupAdminTable() {
        if (AdminForm_codeCol != null) {
            AdminForm_codeCol.setCellValueFactory(new PropertyValueFactory<>("adminCode"));
            AdminForm_nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            AdminForm_emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            AdminForm_phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        }
    }
    private void loadAdminData() {
        adminList.clear();
        adminList.addAll(adminDAO.getAllAdmins());
        if (AdminForm_tableView != null) {
            AdminForm_tableView.setItems(adminList);
        }
    }
    private void setupAdminSearch() {
        if (AdminForm_search == null) return;
        AdminForm_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (adminList == null) return;
            if (AdminForm_tableView == null) return;
            if (newValue == null || newValue.isEmpty()) {
                AdminForm_tableView.setItems(adminList);
                return;
            }
            String searchText = newValue.toLowerCase();
            ObservableList<Admin> filteredList = FXCollections.observableArrayList();
            for (Admin admin : adminList) {
                if (admin.getAdminCode().toLowerCase().contains(searchText) ||
                        admin.getFullName().toLowerCase().contains(searchText) ||
                        admin.getEmail().toLowerCase().contains(searchText) ||
                        admin.getPhoneNum().toLowerCase().contains(searchText)) {
                    filteredList.add(admin);
                }
            }
            AdminForm_tableView.setItems(filteredList);
        });
    }
    @FXML
    private void onAddAdmin() {
        String adminCode = AdminForm_codeField.getText();
        String fullName = AdminForm_nameField.getText();
        String email = AdminForm_emailField.getText();
        String phone = AdminForm_phoneField.getText();
        if (adminCode.isEmpty() || fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            alert.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        if (adminDAO.isEmailExists(email)) {
            alert.showErrorAlert("Lỗi", "Email đã tồn tại");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CreateAccount.fxml"));
            Parent root = loader.load();
            CreateAccountController controller = loader.getController();
            controller.setRole("Admin");
            controller.setDefaultEmail(email);
            controller.setEmail(email);
            Admin newAdmin = new Admin(adminCode, fullName, email, phone);
            controller.setAdmin(newAdmin);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Tạo tài khoản Admin");
            stage.show();
            stage.setOnHidden(event -> {
                if (controller.isSuccess()) {
                    String username = controller.getUsername();
                    String password = controller.getPassword();
                    if (adminDAO.addAdmin(newAdmin, username, password)) {
                        alert.showSuccessAlert("Thành công", "Thêm admin thành công");
                        loadAdminData();
                        clearAdminFields();
                    } else {
                        alert.showErrorAlert("Lỗi", "Không thể thêm admin");
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể mở form tạo tài khoản");
        }
    }
    @FXML
    private void onUpdateAdmin() {
        if (selectedAdmin == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn admin cần cập nhật");
            return;
        }
        String fullName = AdminForm_nameField.getText();
        String email = AdminForm_emailField.getText();
        String phone = AdminForm_phoneField.getText();
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            alert.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        selectedAdmin.setFullName(fullName);
        selectedAdmin.setEmail(email);
        selectedAdmin.setPhoneNum(phone);
        if (adminDAO.updateAdmin(selectedAdmin)) {
            alert.showSuccessAlert("Thành công", "Cập nhật admin thành công");
            loadAdminData();
            clearAdminFields();
        } else {
            alert.showErrorAlert("Lỗi", "Không thể cập nhật admin");
        }
    }
    @FXML
    private void onDeleteAdmin() {
        if (selectedAdmin == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn admin cần xóa");
            return;
        }
        boolean isConfirmed = alert.showConfirmationAlert("Xác nhận", "Bạn có chắc chắn muốn xóa admin này?");
        if (isConfirmed) {
            if (adminDAO.deleteAdmin(selectedAdmin.getAdminCode())) {
                alert.showSuccessAlert("Thành công", "Xóa admin thành công");
                // Ghi log hoạt động
                logActivity("DELETE", "ADMIN", selectedAdmin.getAdminCode(), 
                           "Xóa admin: " + selectedAdmin.getFullName());
                loadAdminData();
                clearAdminFields();
            } else {
                alert.showErrorAlert("Lỗi", "Không thể xóa admin");
            }
        }
    }
    @FXML
    private void onClearAdmin() {
        clearAdminFields();
        AdminForm_tableView.getSelectionModel().clearSelection();
    }
    @FXML
    private void onTableClickedAdmin() {
        selectedAdmin = (Admin) AdminForm_tableView.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            AdminForm_codeField.setText(selectedAdmin.getAdminCode());
            AdminForm_nameField.setText(selectedAdmin.getFullName());
            AdminForm_emailField.setText(selectedAdmin.getEmail());
            AdminForm_phoneField.setText(selectedAdmin.getPhoneNum());
            AdminForm_fixBtn.setDisable(false);
            AdminForm_deleteBtn.setDisable(false);
        } else {
            AdminForm_fixBtn.setDisable(true);
            AdminForm_deleteBtn.setDisable(true);
        }
    }
    private void clearAdminFields() {
        AdminForm_codeField.clear();
        AdminForm_nameField.clear();
        AdminForm_emailField.clear();
        AdminForm_phoneField.clear();
        selectedAdmin = null;
    }

    /* ========== QUẢN LÝ KHOA ========== */
    private void setupDepartmentTable() {
        if (departmentCode_col != null) {
            departmentCode_col.setCellValueFactory(new PropertyValueFactory<>("departmentCode"));
            departmentName_col.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
            departmentAbbreviateName_col.setCellValueFactory(new PropertyValueFactory<>("departmentAbbreviateName"));
            dean_col.setCellValueFactory(new PropertyValueFactory<>("dean"));
            phoneNum_col.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
            email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        }
    }
    private void loadDepartmentData() {
        departmentList.clear();
        departmentList.addAll(departmentDAO.getAllDepartments());
        if (departmentTableView != null) {
            departmentTableView.setItems(departmentList);
        }
    }
    @FXML
    private void onAddDepartment() {
        if (!validateDepartmentInputs()) {
            return;
        }
        Department newDepartment = new Department(
                departmentCode_textField.getText(),
                departmentName_textField.getText(),
                departmentAbbreviateName_textfield.getText(),
                dean_Textfield.getText(),
                phoneNum_textField.getText(),
                email_textField.getText()
        );
        if (departmentDAO.addDepartment(newDepartment)) {
            // Ghi log hoạt động
            logActivity("CREATE", "DEPARTMENT", newDepartment.getDepartmentCode(), 
                       "Thêm khoa: " + newDepartment.getDepartmentName());
            
            alert.showSuccessAlert("Thành công", "Thêm khoa thành công");
            loadDepartmentData();
            updateDashboardCounts(); // Cập nhật số liệu thống kê
            clearDepartmentFields();
        } else {
            alert.showErrorAlert("Lỗi", "Không thể thêm khoa");
        }
    }
    @FXML
    private void onUpdateDepartment() {
        if (selectedDepartment == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn khoa cần sửa");
            return;
        }
        if (!validateDepartmentInputs()) {
            return;
        }
        selectedDepartment.setDepartmentName(departmentName_textField.getText());
        selectedDepartment.setDepartmentAbbreviateName(departmentAbbreviateName_textfield.getText());
        selectedDepartment.setDean(dean_Textfield.getText());
        selectedDepartment.setPhoneNum(phoneNum_textField.getText());
        selectedDepartment.setEmail(email_textField.getText());
        if (departmentDAO.updateDepartment(selectedDepartment)) {
            // Ghi log hoạt động
            logActivity("UPDATE", "DEPARTMENT", selectedDepartment.getDepartmentCode(),
                       "Cập nhật khoa: " + selectedDepartment.getDepartmentName());
            alert.showSuccessAlert("Thành công", "Cập nhật khoa thành công");
            loadDepartmentData();
            updateDashboardCounts(); // Cập nhật số liệu thống kê
            clearDepartmentFields();
        } else {
            alert.showErrorAlert("Lỗi", "Không thể cập nhật khoa");
        }
    }
    @FXML
    private void onDeleteDepartment() {
        if (selectedDepartment == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn khoa cần xóa");
            return;
        }
        boolean isConfirmed = alert.showConfirmationAlert("Xác nhận", "Bạn có chắc chắn muốn xóa khoa này?");
        if (isConfirmed) {
            if (departmentDAO.deleteDepartment(selectedDepartment.getDepartmentCode())) {
                alert.showSuccessAlert("Thành công", "Xóa khoa thành công");
                // Ghi log hoạt động
                logActivity("DELETE", "DEPARTMENT", selectedDepartment.getDepartmentCode(), 
                           "Xóa khoa: " + selectedDepartment.getDepartmentName());
                loadDepartmentData();
                updateDashboardCounts(); // Cập nhật số liệu thống kê
                clearDepartmentFields();
            } else {
                alert.showErrorAlert("Lỗi", "Không thể xóa khoa");
            }
        }
    }
    @FXML
    private void onTableClickedDepartment(MouseEvent event) {
        selectedDepartment = departmentTableView.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            departmentCode_textField.setText(selectedDepartment.getDepartmentCode());
            departmentName_textField.setText(selectedDepartment.getDepartmentName());
            departmentAbbreviateName_textfield.setText(selectedDepartment.getDepartmentAbbreviateName());
            dean_Textfield.setText(selectedDepartment.getDean());
            phoneNum_textField.setText(selectedDepartment.getPhoneNum());
            email_textField.setText(selectedDepartment.getEmail());
            DepartmentForm_Fixbtn.setDisable(false);
            DepartmentForm_Deletebtn.setDisable(false);
        } else {
            DepartmentForm_Fixbtn.setDisable(true);
            DepartmentForm_Deletebtn.setDisable(true);
        }
    }
    private void handleClearDepartmentFields() {
        clearDepartmentFields();
    }
    private void clearDepartmentFields() {
        departmentCode_textField.clear();
        departmentName_textField.clear();
        departmentAbbreviateName_textfield.clear();
        dean_Textfield.clear();
        phoneNum_textField.clear();
        email_textField.clear();
        selectedDepartment = null;
        departmentTableView.getSelectionModel().clearSelection();
    }
    private boolean validateDepartmentInputs() {
        if (departmentCode_textField.getText().isEmpty() || departmentName_textField.getText().isEmpty()) {
            alert.showErrorAlert("Lỗi", "Mã khoa và tên khoa không được để trống");
            return false;
        }
        return true;
    }
    /* ========== QUẢN LÝ LỚP ========== */
    @FXML
    public void onAddClass() {
        String className = ClassForm_classField.getText();
        String course = ClassForm_courseField.getText();
        String department = ClassForm_departmentBox.getValue();
        String major = ClassForm_majorField.getText();
        String headLecture = ClassForm_HeadLectureBox.getValue();
        if (className.isEmpty() || course.isEmpty() || department == null ||
                major.isEmpty() || headLecture == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        Class newClass = new Class(className, course, department, major, headLecture);
        if (classDAO.addClass(newClass)) {
            // Ghi log hoạt động
            logActivity("CREATE", "CLASS", newClass.getClassName(),
                       "Thêm lớp: " + newClass.getClassName());
            alert.showSuccessAlert("Thành công", "Thêm lớp thành công");
            updateAndRefreshTables();
            updateDashboardCounts(); // Cập nhật số liệu thống kê
            clearClassFields();
        } else {
            alert.showErrorAlert("Lỗi", "Không thể thêm lớp");
        }
    }
    @FXML
    public void onUpdateClass() {
        String className = ClassForm_classField.getText();
        String course = ClassForm_courseField.getText();
        String department = ClassForm_departmentBox.getValue();
        String major = ClassForm_majorField.getText();
        String headLecture = ClassForm_HeadLectureBox.getValue();
        if (className.isEmpty() || course.isEmpty() || department == null ||
                major.isEmpty() || headLecture == null) {
            alert.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        Class updatedClass = new Class(className, course, department, major, headLecture);
        if (classDAO.updateClass(updatedClass)) {
            // Ghi log hoạt động
            logActivity("UPDATE", "CLASS", updatedClass.getClassName(),
                       "Cập nhật lớp: " + updatedClass.getClassName());
            alert.showSuccessAlert("Thành công", "Cập nhật lớp thành công");
            updateAndRefreshTables();
            updateDashboardCounts(); // Cập nhật số liệu thống kê
            clearClassFields();
        } else {
            alert.showErrorAlert("Lỗi", "Không thể cập nhật lớp");
        }
    }
    @FXML
    public void onDeleteClass() {
        String className = ClassForm_classField.getText();
        if (className.isEmpty()) {
            alert.showErrorAlert("Lỗi", "Vui lòng chọn lớp cần xóa");
            return;
        }
        boolean isConfirmed = alert.showConfirmationAlert("Xác nhận", "Bạn có chắc chắn muốn xóa lớp này?");
        if (isConfirmed) {
            if (classDAO.deleteClass(className)) {
                alert.showSuccessAlert("Thành công", "Xóa lớp thành công");
                // Ghi log hoạt động
                logActivity("DELETE", "CLASS", className, 
                           "Xóa lớp: " + className);
                updateAndRefreshTables();
                updateDashboardCounts(); // Cập nhật số liệu thống kê
                clearClassFields();
            } else {
                alert.showErrorAlert("Lỗi", "Không thể xóa lớp");
            }
        }
    }
    @FXML
    public void onClearClass() {
        clearClassFields();
        ClassForm_tableView.getSelectionModel().clearSelection();
    }
    private void clearClassFields() {
        ClassForm_classField.clear();
        ClassForm_courseField.clear();
        ClassForm_departmentBox.setValue(null);
        ClassForm_majorField.clear();
        ClassForm_HeadLectureBox.setValue(null);
    }
    private void refreshClassTable() {
        if (classList == null || ClassForm_tableView == null) {
            return;
        }
        try {
            classList.clear();
            classList.addAll(classDAO.getAllClasses());
            ClassForm_tableView.setItems(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void onTableClickedClass() {
        Class selectedClass = ClassForm_tableView.getSelectionModel().getSelectedItem();
        if (selectedClass != null) {
            ClassForm_classField.setText(selectedClass.getClassName());
            ClassForm_courseField.setText(selectedClass.getCourse());
            ClassForm_departmentBox.setValue(selectedClass.getDepartment());
            ClassForm_majorField.setText(selectedClass.getMajor());
            ClassForm_HeadLectureBox.setValue(selectedClass.getHeadLecture());
            ClassForm_fixBtn.setDisable(false);
            ClassForm_deleteBtn.setDisable(false);
        } else {
            ClassForm_fixBtn.setDisable(true);
            ClassForm_deleteBtn.setDisable(true);
        }
    }
    private void initializeClassTable() {
        if (ClassForm_classCol != null) {
            ClassForm_classCol.setCellValueFactory(new PropertyValueFactory<>("className"));
            ClassForm_courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
            ClassForm_departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
            ClassForm_majorForm.setCellValueFactory(new PropertyValueFactory<>("major"));
            ClassForm_headMasterCol.setCellValueFactory(new PropertyValueFactory<>("headLecture"));
        }
        if (ClassForm_departmentBox != null) {
            DepartmentDAO departmentDAO = new DepartmentDAO();
            ObservableList<String> departments = FXCollections.observableArrayList();
            for (Department dept : departmentDAO.getAllDepartments()) {
                departments.add(dept.getDepartmentName());
            }
            ClassForm_departmentBox.setItems(departments);
        }
        if (ClassForm_HeadLectureBox != null) {
            LectureDAO lectureDAO = new LectureDAO();
            ObservableList<String> lectures = FXCollections.observableArrayList();
            for (Lecture lecture : lectureDAO.getAllLectures()) {
                lectures.add(lecture.getLectureName());
            }
            ClassForm_HeadLectureBox.setItems(lectures);
        }
        refreshClassTable();
    }
    private void setupClassSearch() {
        if (ClassForm_Search == null) return;

        ClassForm_Search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (classList == null) return;
            if (ClassForm_tableView == null) return;

            if (newValue == null || newValue.isEmpty()) {
                ClassForm_tableView.setItems(classList);
                return;
            }
            String searchText = newValue.toLowerCase();
            ObservableList<Class> filteredList = FXCollections.observableArrayList();
            for (Class c : classList) {
                if (c.getClassName().toLowerCase().contains(searchText) ||
                        c.getCourse().toLowerCase().contains(searchText) ||
                        c.getDepartment().toLowerCase().contains(searchText) ||
                        c.getMajor().toLowerCase().contains(searchText) ||
                        c.getHeadLecture().toLowerCase().contains(searchText)) {
                    filteredList.add(c);
                }
            }
            ClassForm_tableView.setItems(filteredList);
        });
    }
    // ========== MỞ CHAT ========== */
    @FXML
    private void handleOpenChat(ActionEvent event) {
        String userName = UserSession.getInstance().getUserName();
        String userFullName = UserSession.getInstance().getFullName();

        if (userFullName == null || userFullName.isEmpty()) {
            alert.showErrorAlert("Lỗi", "Không thể xác định tên người dùng. Vui lòng đăng nhập lại.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ChatView.fxml"));
            Parent root = loader.load();

            ChatViewController chatController = loader.getController();
            chatController.initialize(userFullName);

            Stage stage = new Stage();
            stage.setTitle("Chat Messenger - " + userFullName);
            stage.setScene(new Scene(root));
            
            stage.setOnCloseRequest(e -> chatController.shutdown());
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể mở cửa sổ chat: " + e.getMessage());
        }
    }
    /* ========== KHỞI TẠO ========== */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showName();
            setupCommonControls();

            setupAdminForm();
            setupLectureForm();
            setupDepartmentForm();
            setupClassForm();
            setupStudentForm();
            setupActivityLogTable();

            setInitialState();

        } catch (Exception e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể khởi tạo form: " + e.getMessage());
        }
    }

    private void setupCommonControls() {
        logout_Btn.setOnAction(event -> handleLogout());
        chat_nav.setOnAction(this::handleOpenChat);
        handleNavigation();
    }

    private void setupAdminForm() {
        setupAdminTable();
        loadAdminData();
        setupAdminSearch();
        AdminForm_addBtn.setOnAction(event -> onAddAdmin());
        AdminForm_fixBtn.setOnAction(event -> onUpdateAdmin());
        AdminForm_deleteBtn.setOnAction(event -> onDeleteAdmin());
        AdminForm_clearBtn.setOnAction(event -> onClearAdmin());
        AdminForm_tableView.setOnMouseClicked(event -> onTableClickedAdmin());
    }

    private void setupLectureForm() {
        setupLectureTable();
        loadLectureData();
        setupLectureSearch();
        lectureForm_Addbtn.setOnAction(event -> handleAddLecture());
        lectureForm_Fixbtn.setOnAction(event -> handleEditLecture());
        lectureForm_Deletebtn.setOnAction(event -> handleDeleteLecture());
        lectureForm_Clearbtn.setOnAction(event -> handleClearLecture());
        lectureTableView.setOnMouseClicked(this::onTableClickedLecture);
    }

    private void setupDepartmentForm() {
        setupDepartmentTable();
        loadDepartmentData();
        DepartmentForm_Addbtn.setOnAction(event -> onAddDepartment());
        DepartmentForm_Fixbtn.setOnAction(event -> onUpdateDepartment());
        DepartmentForm_Deletebtn.setOnAction(event -> onDeleteDepartment());
        DepartmentForm_Clearbtn.setOnAction(event -> handleClearDepartmentFields());
        departmentTableView.setOnMouseClicked(this::onTableClickedDepartment);
    }

    private void setupClassForm() {
        initializeClassTable();
        setupClassSearch();
        ClassForm_addBtn.setOnAction(event -> onAddClass());
        ClassForm_fixBtn.setOnAction(event -> onUpdateClass());
        ClassForm_deleteBtn.setOnAction(event -> onDeleteClass());
        ClassForm_clearBtn.setOnAction(event -> onClearClass());
        ClassForm_tableView.setOnMouseClicked(event -> onTableClickedClass());
    }

    private void setupStudentForm() {
        initializeStudentForm();
        StudentForm_addBtn.setOnAction(event -> handleAddStudent());
        StudentForm_fixBtn.setOnAction(event -> handleEditStudent());
        StudentForm_deleteBtn.setOnAction(event -> handleDeleteStudent());
        StudentForm_clearBtn.setOnAction(event -> handleClearStudent());
        StudentForm_tableView.setOnMouseClicked(event -> onTableClickedStudent());
    }

    private void showName() {
        String fullName = UserSession.getInstance().getFullName();
        if (fullName != null && !fullName.isEmpty()) {
            name_nav.setText(fullName);
        }
    }

    private void setInitialState() {
        switchForm(homeForm);
        updateDashboardCounts();
        loadActivityLogs();

        hideLectureInfo();

        DepartmentForm_Fixbtn.setDisable(true);
        DepartmentForm_Deletebtn.setDisable(true);
        lectureForm_Fixbtn.setDisable(true);
        lectureForm_Deletebtn.setDisable(true);
        AdminForm_fixBtn.setDisable(true);
        AdminForm_deleteBtn.setDisable(true);
        StudentForm_fixBtn.setDisable(true);
        StudentForm_deleteBtn.setDisable(true);
        ClassForm_fixBtn.setDisable(true);
        ClassForm_deleteBtn.setDisable(true);
    }

    // Cập nhật số liệu thống kê trên dashboard
    private void updateDashboardCounts() {
        try {
            int studentCountValue = studentDAO.getStudentCount();
            int lectureCountValue = lectureDAO.getLectureCount();
            int classCountValue = classDAO.getClassCount();
            int departmentCountValue = departmentDAO.getDepartmentCount();

            studentCount.setText(String.valueOf(studentCountValue));
            LectureCount.setText(String.valueOf(lectureCountValue));
            classCount.setText(String.valueOf(classCountValue));
            apartmentCount.setText(String.valueOf(departmentCountValue));
        } catch (Exception e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể cập nhật số liệu thống kê!");
        }
    }

    // Thiết lập bảng Activity Log
    private void setupActivityLogTable() {
        activityLogTimeCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        activityLogUserCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        activityLogActionCol.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        activityLogEntityCol.setCellValueFactory(new PropertyValueFactory<>("entityType"));
        activityLogEntityNameCol.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        activityLogDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    // Load dữ liệu Activity Log
    private void loadActivityLogs() {
        try {
            activityLogList.clear();
            List<ActivityLog> logs = activityLogDAO.getRecentActivityLogs(50); // Lấy 50 log gần nhất
            activityLogList.addAll(logs);
            activityLogTableView.setItems(activityLogList);
        } catch (Exception e) {
            e.printStackTrace();
            alert.showErrorAlert("Lỗi", "Không thể tải dữ liệu hoạt động!");
        }
    }

    // Ghi log hoạt động
    private void logActivity(String actionType, String entityType, String entityName, String description) {
        try {
            String username = UserSession.getInstance().getUserName();
            
            if (username != null) {
                ActivityLog log = new ActivityLog(
                    0, // user_id sẽ được set NULL trong database
                    username,
                    actionType,
                    entityType,
                    entityName,
                    description
                );
                
                if (activityLogDAO.addActivityLog(log)) {
                    // Cập nhật bảng nếu đang ở homeForm
                    if (homeForm.isVisible()) {
                        loadActivityLogs();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Không hiển thị lỗi cho user vì đây chỉ là logging
        }
    }
}