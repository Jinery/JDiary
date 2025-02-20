package com.kychnoo.jdiary.OtherClasses;

public class Grade {

    private int id;
    private int score;

    private String userPhone;
    private String gradeText;
    private String date;

    public Grade(int id, String userPhone, String gradeText, String date, int score) {
        this.id = id;
        this.userPhone = userPhone;
        this.gradeText = gradeText;
        this.date = date;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getGradeText() {
        return gradeText;
    }

    public String getDate() {
        return date;
    }

}
