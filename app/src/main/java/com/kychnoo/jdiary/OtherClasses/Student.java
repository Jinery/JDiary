package com.kychnoo.jdiary.OtherClasses;

public class Student {
    private String username;
    private String descriptionText;
    private String iconPath;

    private int experiencePoints;

    public Student(String username, String descriptionText, String iconPath, int experiencePoints) {
        this.username = username;
        this.descriptionText = descriptionText;
        this.iconPath = iconPath;
        this.experiencePoints = experiencePoints;
    }

    public String getUsername() {

        return username;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public String getIconPath() {
        return iconPath;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }
}
