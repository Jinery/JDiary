package com.kychnoo.jdiary.OthetClasses;

import java.util.List;

public class Question {

    private int id;
    private String text;
    private List<Answer> answers;

    public Question(int id, String text, List<Answer> answers) {
        this.id = id;
        this.text = text;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

}
