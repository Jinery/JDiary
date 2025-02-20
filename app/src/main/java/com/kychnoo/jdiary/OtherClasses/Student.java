package com.kychnoo.jdiary.OtherClasses;

public class Student {
    private String username;
    private String descriptionText;

    private int experiencePoints;

    public Student(String username, String descriptionText, int experiencePoints) {
        this.username = username;
        this.descriptionText = descriptionText;
        this.experiencePoints = experiencePoints;
    }

    public String getUsername() {

        return username;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }
}
