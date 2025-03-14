package com.kychnoo.jdiary;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kychnoo.jdiary.Achievements.AchievementsHelper;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.OtherClasses.Answer;
import com.kychnoo.jdiary.OtherClasses.Question;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {

    private int testId;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;

    private String userPhone;

    private DatabaseHelper databaseHelper;

    private List<Question> questionList;

    private ProgressBar pbProgress;

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
            pbProgress = findViewById(R.id.pbProgress);

            pbProgress.setMax(questionList.size());
            pbProgress.setProgress(currentQuestionIndex);

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
        double percentage =  (correctAnswersCount / (double) totalQuestions) * 100;
        int score = calculateGrade(percentage);
        String exitText = "Выполнен тест: " + databaseHelper.getTestNameById(testId);
        databaseHelper.addTestResult(userPhone, testId, (int)percentage);

        int experiencePoints = (int)calculatePoints(totalQuestions);
        databaseHelper.updateUserExperiencePoints(userPhone, experiencePoints);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        databaseHelper.addGrade(userPhone, exitText, currentDate, score);

        AchievementsHelper.passedTest(userPhone);

        Toast.makeText(this, "Тест завершен! Вы набрали " + experiencePoints + " баллов.", Toast.LENGTH_LONG).show();
        finish();
    }

    private int calculateGrade(double percentage) {
        if (percentage >= 90) {
            return 5;
        }
        else if (percentage >= 75) {
            return 4;
        }
        else if (percentage >= 50) {
            return 3;
        }
        else {
            return 2;
        }
    }

    private double calculatePoints(int totalQuestions) {
        int maxPoints = databaseHelper.getTestPointsById(testId);
        return (double)maxPoints / ((double)totalQuestions / correctAnswersCount);
    }
}