-- Tạo bảng activity_logs để lưu trữ lịch sử hoạt động
CREATE TABLE activity_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    username VARCHAR(50),
    action_type ENUM('CREATE', 'UPDATE', 'DELETE'),
    entity_type ENUM('STUDENT', 'LECTURE', 'CLASS', 'DEPARTMENT', 'ADMIN'),
    entity_name VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Tạo index để tối ưu hiệu suất truy vấn
CREATE INDEX idx_activity_logs_created_at ON activity_logs(created_at);
CREATE INDEX idx_activity_logs_username ON activity_logs(username);
CREATE INDEX idx_activity_logs_entity_type ON activity_logs(entity_type);
CREATE INDEX idx_activity_logs_action_type ON activity_logs(action_type);

-- Thêm comment cho bảng
ALTER TABLE activity_logs COMMENT = 'Bảng lưu trữ lịch sử hoạt động của người dùng trong hệ thống'; 