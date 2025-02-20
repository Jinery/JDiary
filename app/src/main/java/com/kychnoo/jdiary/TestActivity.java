package com.kychnoo.jdiary;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.OthetClasses.Answer;
import com.kychnoo.jdiary.OthetClasses.Question;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private int testId;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;

    private String userPhone;

    private DatabaseHelper databaseHelper;

    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        databaseHelper = new DatabaseHelper(this);
        testId = getIntent().getIntExtra("test_id", -1);
        userPhone = getIntent().getStringExtra("user_phone");

        questionList = loadQuestions(testId);
        if(questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "Нет вопросов для этого теста.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadCurrentQuestion();
    }

    private List<Question> loadQuestions(int testId) {
        List<Question> questions = new ArrayList<>();
        Cursor questionsCursor = databaseHelper.getQuestionsByTestId(testId);
        if (questionsCursor != null) {
            while (questionsCursor.moveToNext()) {
                int questionId = questionsCursor.getInt(questionsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUESTION_ID));
                String questionText = questionsCursor.getString(questionsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUESTION_TEXT));
                List<Answer> answers = loadAnswers(questionId);
                questions.add(new Question(questionId, questionText, answers));
            }
            questionsCursor.close();
        }
        return questions;
    }

    private List<Answer> loadAnswers(int questionId) {
        List<Answer> answers = new ArrayList<>();
        Cursor answersCursor = databaseHelper.getAnswersByQuestionId(questionId);
        if (answersCursor != null) {
            while (answersCursor.moveToNext()) {
                int answerId = answersCursor.getInt(answersCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ANSWER_ID));
                String answerText = answersCursor.getString(answersCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ANSWER_TEXT));
                int isCorrect = answersCursor.getInt(answersCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ANSWER_CORRECT));
                answers.add(new Answer(answerId, answerText, isCorrect == 1));
            }
            answersCursor.close();
        }
        return answers;
    }

    private void loadCurrentQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question question = questionList.get(currentQuestionIndex);
            TextView tvQuestion = findViewById(R.id.tvQuestion);
            RadioGroup rgAnswers = findViewById(R.id.rgAnswers);
            Button btnNext = findViewById(R.id.btnNext);

            tvQuestion.setText(question.getText());
            rgAnswers.removeAllViews();

            for (Answer answer : question.getAnswers()) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(answer.getText());
                radioButton.setId(View.generateViewId());
                rgAnswers.addView(radioButton);
            }

            btnNext.setOnClickListener(v -> {
                if (rgAnswers.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "Выберите ответ", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedButton = findViewById(rgAnswers.getCheckedRadioButtonId());
                String selectedAnswerText = selectedButton.getText().toString();

                for (Answer answer : question.getAnswers()) {
                    if (answer.getText().equals(selectedAnswerText)) {
                        if (answer.isCorrect()) {
                            correctAnswersCount++;
                        }
                        break;
                    }
                }

                currentQuestionIndex++;
                if (currentQuestionIndex < questionList.size()) {
                    loadCurrentQuestion();
                } else {
                    finishTest();
                }
            });
        } else {
            finishTest();
        }
    }

    private void finishTest() {
        int totalQuestions = questionList.size();
        int points = calculatePoints(totalQuestions);
        databaseHelper.addTestResult(userPhone, testId, points);
        Toast.makeText(this, "Тест завершен! Вы набрали " + points + " баллов.", Toast.LENGTH_LONG).show();
        finish();
    }

    private int calculatePoints(int totalQuestions) {
        return (int) ((correctAnswersCount / (double) totalQuestions) * 100);
    }
}