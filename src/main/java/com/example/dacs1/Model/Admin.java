package com.example.dacs1.Model;

public class Admin {
    private String adminCode;
    private String fullName;
    private String email;
    private String phoneNum;

    public Admin(String adminCode, String fullName, String email, String phoneNum) {
        this.adminCode = adminCode;
        this.fullName = fullName;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
