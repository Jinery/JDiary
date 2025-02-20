package com.kychnoo.jdiary.OtherClasses;

public class Schedule {

    private long id;

    private int lessonsCount;

    private String date;
    private String className;

    public Schedule(long id, String date, int lessonsCount, String className) {

        this.id = id;
        this.lessonsCount = lessonsCount;
        this.date = date;
        this.className = className;

    }

    public long getId() {
        return id;
    }

    public int getLessonsCount() {
        return lessonsCount;
    }

    public String getDate() {
        return date;
    }

    public String getClassName() {
        return className;
    }
}
