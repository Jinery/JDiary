package com.kychnoo.jdiary.OtherClasses;

public class Test {

    private int id;
    private String name;
    private int questionsCount;
    private int points;
    private String className;
    private boolean isPassed;

    public Test(int id, String name, int questionsCount, int points, String className, boolean isPassed) {
        this.id = id;
        this.name = name;
        this.questionsCount = questionsCount;
        this.points = points;
        this.className = className;
        this.isPassed = isPassed;
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

    public boolean isPassed() {
        return isPassed;
    }
}
