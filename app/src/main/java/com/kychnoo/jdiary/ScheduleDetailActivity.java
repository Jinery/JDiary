package com.kychnoo.jdiary;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Notifications.NotificationHelper;

public class ScheduleDetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_detail);

        tableLayout = findViewById(R.id.tableLayout);
        databaseHelper = new DatabaseHelper(this);

        long scheduleId = getIntent().getLongExtra("scheduleId", -1);
        if (scheduleId != -1) {
            loadScheduleDetails(scheduleId);
        } else {
            NotificationHelper.show(this, "Расписание не найдено", NotificationHelper.NotificationColor.ERROR, 2000);
            finish();
        }

    }

    private void loadScheduleDetails(long scheduleId) {
        Cursor lessonsCursor = databaseHelper.getLessonsByScheduleId(scheduleId);
        if (lessonsCursor.moveToFirst()) {
            int lessonNumber = 1;
            do {
                String subject = lessonsCursor.getString(lessonsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LESSON_SUBJECT));
                String homework = lessonsCursor.getString(lessonsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LESSON_HOMEWORK));

                TableRow row = new TableRow(this);
                TextView numberTextView = new TextView(this);
                numberTextView.setText(String.valueOf(lessonNumber));
                numberTextView.setPadding(16, 16, 16, 16);

                TextView subjectTextView = new TextView(this);
                subjectTextView.setText(subject);
                subjectTextView.setPadding(16, 16, 16, 16);

                TextView homeworkTextView = new TextView(this);
                homeworkTextView.setText(homework);
                homeworkTextView.setPadding(16, 16, 16, 16);

                row.addView(numberTextView);
                row.addView(subjectTextView);
                row.addView(homeworkTextView);

                tableLayout.addView(row);
                lessonNumber++;
            } while (lessonsCursor.moveToNext());
        }
        lessonsCursor.close();
    }
}