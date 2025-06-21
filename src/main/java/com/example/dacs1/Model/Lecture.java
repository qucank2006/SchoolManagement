package com.example.dacs1.Model;

import java.sql.Date;

public class Lecture {
    private int id;
    private String lectureCode;
    private String lectureName;
    private Date dateOfBirth;
    private String degree;
    private String department;
    private String phoneNum;
    private String email;
    private String address;
    private String imagePath;

    public Lecture(int id, String lectureCode, String lectureName, Date dateOfBirth, String degree,
                   String department, String phoneNum, String email, String address) {
        this.id = id;
        this.lectureCode = lectureCode;
        this.lectureName = lectureName;
        this.dateOfBirth = dateOfBirth;
        this.degree = degree;
        this.department = department;
        this.phoneNum = phoneNum;
        this.email = email;
        this.address = address;
    }

    public Lecture(int id, String lectureCode, String lectureName, Date dateOfBirth, String degree,
                   String department, String phoneNum, String email, String address, String imagePath) {
        this.id = id;
        this.lectureCode = lectureCode;
        this.lectureName = lectureName;
        this.dateOfBirth = dateOfBirth;
        this.degree = degree;
        this.department = department;
        this.phoneNum = phoneNum;
        this.email = email;
        this.address = address;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLectureCode() {
        return lectureCode;
    }

    public void setLectureCode(String lectureCode) {
        this.lectureCode = lectureCode;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}