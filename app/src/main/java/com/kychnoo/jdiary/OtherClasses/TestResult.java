package com.kychnoo.jdiary.OtherClasses;

public class TestResult {
    private String userPhone;
    private int testId;
    private String testName;
    private int totalQuestions;
    private int correctAnswers;
    private double percentage;
    private int score;
    private int experiencePoints;
    private boolean passed;

    public TestResult(String userPhone, int testId, String testName, int totalQuestions, int correctAnswers,
                      double percentage, int score, int experiencePoints, boolean passed) {
        this.userPhone = userPhone;
        this.testId = testId;
        this.testName = testName;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.percentage = percentage;
        this.score = score;
        this.experiencePoints = experiencePoints;
        this.passed = passed;
    }

    public String getUserPhone() { return userPhone; }
    public int getTestId() { return testId; }
    public String getTestName() { return testName; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public double getPercentage() { return percentage; }
    public int getScore() { return score; }
    public int getExperiencePoints() { return experiencePoints; }
    public boolean isPassed() { return passed; }
}
