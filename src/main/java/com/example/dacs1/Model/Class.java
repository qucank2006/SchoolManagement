package com.example.dacs1.Model;

public class Class {
    private String className;
    private String course;
    private String department;
    private String major;
    private String headLecture;

    public Class(String className, String course, String department,
                 String major, String headLecture) {
        this.className = className;
        this.course = course;
        this.department = department;
        this.major = major;
        this.headLecture = headLecture;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getHeadLecture() {
        return headLecture;
    }

    public void setHeadLecture(String headLecture) {
        this.headLecture = headLecture;
    }
}
