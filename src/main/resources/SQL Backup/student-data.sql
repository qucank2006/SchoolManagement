CREATE TABLE users
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50)  NOT NULL UNIQUE,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     ENUM('Lecture', 'Admin') NOT NULL
);

CREATE TABLE departments
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    department_code VARCHAR(20)  NOT NULL UNIQUE,
    department_name VARCHAR(100) NOT NULL,
    abbreviate_name VARCHAR(20)  NOT NULL UNIQUE,
    dean            VARCHAR(100),
    phone           VARCHAR(20),
    email           VARCHAR(100)
);

CREATE TABLE lectures
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    lecture_code  VARCHAR(20)  NOT NULL UNIQUE,
    full_name     VARCHAR(100) NOT NULL UNIQUE,
    date_of_birth DATE,
    department    VARCHAR(20)  NOT NULL,
    degree        VARCHAR(50),
    email         VARCHAR(100) NOT NULL,
    phone         VARCHAR(20),
    address       VARCHAR(255),
    image_path    VARCHAR(255),
    user_id       INT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (department) REFERENCES departments (abbreviate_name)
);

CREATE TABLE classes
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    class_name    VARCHAR(100) NOT NULL UNIQUE,
    course_year   VARCHAR(10)  NOT NULL,
    department    VARCHAR(100) NOT NULL,
    major         VARCHAR(100) NOT NULL,
    lecture_name  VARCHAR(100) NOT NULL,
    student_count INT DEFAULT 0,
    FOREIGN KEY (department) REFERENCES departments (department_name),
    FOREIGN KEY (lecture_name) REFERENCES lectures (full_name)
);

CREATE TABLE students
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    student_code  VARCHAR(20)  NOT NULL UNIQUE,
    full_name     VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    email         VARCHAR(100) NOT NULL,
    phone         VARCHAR(20),
    address       VARCHAR(255),
    class         VARCHAR(100) NOT NULL,
    gender        VARCHAR(10),
    image_path    VARCHAR(255),
    FOREIGN KEY (class) REFERENCES classes (class_name)
);

CREATE TABLE admins
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    admin_code VARCHAR(20)  NOT NULL UNIQUE,
    full_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    phone      VARCHAR(20),
    user_id    INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE chat_messages (
    id                INT PRIMARY KEY AUTO_INCREMENT,
    sender_name       VARCHAR(100) NOT NULL,
    recipient_name    VARCHAR(100) NOT NULL,
    message_content   TEXT NOT NULL,
    sent_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_delivered      BOOLEAN DEFAULT FALSE
);
CREATE TABLE activity_logs (
    id               INT PRIMARY KEY AUTO_INCREMENT,
    user_id          INT,
    username         VARCHAR(50),
    action_type      ENUM('CREATE', 'UPDATE', 'DELETE'),
    entity_type      ENUM('STUDENT', 'LECTURE', 'CLASS', 'DEPARTMENT', 'ADMIN'),
    entity_name      VARCHAR(100),
    description      TEXT,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);