package com.katza.myapplication_firebase;

import java.util.Base64;

public class Student {
    private String fullName,id;
    private String base64Image;
    private int grade;

    public Student() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return id + ", " + fullName + ", " + grade + "\n";
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
