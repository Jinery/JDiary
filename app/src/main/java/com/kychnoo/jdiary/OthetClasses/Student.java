package com.kychnoo.jdiary.OthetClasses;

public class Student {
    private String username;
    private String descriptionText;

    public Student(String username, String descriptionText) {
        this.username = username;
        this.descriptionText = descriptionText;
    }

    public String getUsername() {
        return username;
    }

    public String getDescriptionText() {
        return descriptionText;
    }
}
