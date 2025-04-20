package com.kychnoo.jdiary;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.kychnoo.jdiary.Achievements.AchievementsHelper;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Notifications.NotificationHelper;
import com.kychnoo.jdiary.OtherClasses.Answer;
import com.kychnoo.jdiary.OtherClasses.Question;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

    private MaterialRadioButton selectedRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        databaseHelper = new DatabaseHelper(this);
        testId = getIntent().getIntExtra("test_id", -1);
        userPhone = getIntent().getStringExtra("user_phone");

        if (savedInstanceState != null) {
            questionList = savedInstanceState.getParcelableArrayList("questions");
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex");
        } else {
            questionList = loadQuestions(testId);
            Collections.shuffle(questionList);
            currentQuestionIndex = 0;
        }


        if(questionList == null || questionList.isEmpty()) {
            NotificationHelper.show(this, "Нет вопросов для этого теста.", NotificationHelper.NotificationColor.INFO, 1000);
            finish();
            return;
        }

        loadCurrentQuestion();
        setupNextButton();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("questions", new ArrayList<>(questionList));
        outState.putInt("currentQuestionIndex", currentQuestionIndex);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        questionList = savedInstanceState.getParcelableArrayList("questions");
        currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex");
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

            pbProgress.setMax(questionList.size() * 10);

            new Thread(() -> {
                for(int pg = pbProgress.getProgress(); pg < currentQuestionIndex * 10; pg++) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    int finalPg = pg;
                    runOnUiThread(() -> {
                        pbProgress.setProgress(finalPg);
                    });
                }
                runOnUiThread(() -> {
                    pbProgress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_blue)));
                });
            }).start();

            tvQuestion.setText(question.getText());
            rgAnswers.removeAllViews();

            List<Answer> randomizedAnswers = new ArrayList<>(question.getAnswers());
            Collections.shuffle(randomizedAnswers);

            for (Answer answer : randomizedAnswers) {
                MaterialRadioButton radioButton = new MaterialRadioButton(this);
                radioButton.setText(answer.getText());
                radioButton.setId(View.generateViewId());
                radioButton.setElevation(4);
                radioButton.setTextColor(ContextCompat.getColor(this, R.color.soft_black));
                radioButton.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_blue)));

                rgAnswers.addView(radioButton);
            }

            rgAnswers.setOnCheckedChangeListener((group, checkedId) -> {
                MaterialRadioButton selectedButton = findViewById(checkedId);
                if (selectedButton != null) {
                    selectedRadioButton = selectedButton;
                    selectedButton.animate()
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(200)
                            .withEndAction(() -> selectedButton.animate().scaleX(1f).scaleY(1f).start())
                            .start();
                }
            });

            btnNext.setOnClickListener(v -> {
                if (rgAnswers.getCheckedRadioButtonId() == -1) {
                    NotificationHelper.show(this, "Выберите ответ", NotificationHelper.NotificationColor.WARNING, 1000);
                    return;
                }
                for (int i = 0; i < rgAnswers.getChildCount(); i++) {
                    View child = rgAnswers.getChildAt(i);
                    if (child instanceof RadioButton) {
                        child.setClickable(false);
                    }
                }

                RadioButton selectedButton = findViewById(rgAnswers.getCheckedRadioButtonId());
                String selectedAnswerText = selectedButton.getText().toString();

                boolean isCorrect = false;
                String explanation = "";

                for (Answer answer : question.getAnswers()) {
                    if (answer.getText().equals(selectedAnswerText)) {
                        isCorrect = answer.isCorrect();
                        explanation = isCorrect ? "Отлично!" : "Увы, это неверный ответ.";
                        break;
                    }
                }

                if (isCorrect) {
                    runOnUiThread(() -> {
                        pbProgress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    });
                    correctAnswersCount++;
                } else {
                    runOnUiThread(() -> {
                        pbProgress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    });
                }

                currentQuestionIndex++;

                showBottomMenu(isCorrect, explanation);
            });
        } else {
            finishTest();
        }
    }

    private void setupNextButton() {
        Button btnNextQuestion = findViewById(R.id.btnNextQuestion);
        btnNextQuestion.setOnClickListener(v -> {
            hideBottomMenu();

            if (currentQuestionIndex < questionList.size()) {
                loadCurrentQuestion();
            } else {
                finishTest();
            }
        });
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

        NotificationHelper.show(this, "Тест завершен! Вы набрали " + experiencePoints + " баллов.", NotificationHelper.NotificationColor.INFO, 1000);
        finish();
    }

    private void showBottomMenu(boolean isCorrect, String explanation) {
        MaterialCardView bottomMenu = findViewById(R.id.bottomMenu);
        TextView tvResult = findViewById(R.id.tvResult);
        TextView tvExplanation = findViewById(R.id.tvExplanation);
        Button btnNextQuestion = findViewById(R.id.btnNextQuestion);

        float cornerSize = 16 * this.getResources().getDisplayMetrics().density;

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, cornerSize)
                .setTopRightCorner(CornerFamily.ROUNDED, cornerSize)
                .setBottomLeftCorner(CornerFamily.ROUNDED, 0)
                .setBottomRightCorner(CornerFamily.ROUNDED, 0)
                .build();

        bottomMenu.setShapeAppearanceModel(shapeAppearanceModel);

        tvResult.setText(isCorrect ? "Верно" : "Неверно");
        int bottomMenuColorIndex = ContextCompat.getColor(this, isCorrect ? R.color.green : R.color.red);
        bottomMenu.setStrokeColor(bottomMenuColorIndex);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            bottomMenu.setOutlineSpotShadowColor(bottomMenuColorIndex);
        }

        tvResult.setTextColor(bottomMenuColorIndex);
        selectedRadioButton.setButtonTintList(ColorStateList.valueOf(bottomMenuColorIndex));
        btnNextQuestion.setBackgroundColor(bottomMenuColorIndex);
        tvExplanation.setText(explanation);

        bottomMenu.setVisibility(View.VISIBLE);
        bottomMenu.animate()
                .translationY(0)
                .setDuration(200)
                .start();
    }

    private void hideBottomMenu() {
        MaterialCardView bottomMenu = findViewById(R.id.bottomMenu);

        bottomMenu.animate()
                .translationY(bottomMenu.getHeight())
                .setDuration(200)
                .withEndAction(() -> bottomMenu.setVisibility(View.GONE))
                .start();
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