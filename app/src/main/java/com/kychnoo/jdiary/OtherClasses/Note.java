package com.kychnoo.jdiary.OtherClasses;

public class Note {

    private int id;
    private String title;
    private String content;
    private String date;

    public Note(int id, String title, String content, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
