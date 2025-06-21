package com.example.dacs1.Model;

public class Department {
    private String departmentCode;
    private String departmentName;
    private String departmentAbbreviateName;
    private String dean;
    private String phoneNum;
    private String email;

    public Department(String departmentCode, String departmentName, String departmentAbbreviateName, 
                     String dean, String phoneNum, String email) {
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.departmentAbbreviateName = departmentAbbreviateName;
        this.dean = dean;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentAbbreviateName() {
        return departmentAbbreviateName;
    }

    public void setDepartmentAbbreviateName(String departmentAbbreviateName) {
        this.departmentAbbreviateName = departmentAbbreviateName;
    }

    public String getDean() {
        return dean;
    }

    public void setDean(String dean) {
        this.dean = dean;
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
}
