package com.kychnoo.jdiary.OthetClasses;

public class Grade {

    private int id;
    private String userPhone;
    private String date;
    private int score;

    public Grade(int id, String userPhone, String date, int score) {
        this.id = id;
        this.userPhone = userPhone;
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

    public String getDate() {
        return date;
    }

}
