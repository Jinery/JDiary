package com.kychnoo.jdiary.OthetClasses;

public class Test {

    private int id;
    private String name;
    private int questionsCount;
    private int points;
    private String className;

    public Test(int id, String name, int questionsCount, int points, String className) {
        this.id = id;
        this.name = name;
        this.questionsCount = questionsCount;
        this.points = points;
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuestionsCount() {
        return questionsCount;
    }

    public int getPoints() {
        return points;
    }

    public String getClassName() {
        return className;
    }

}
