package com.kychnoo.jdiary.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Main String's.
    private static final String databaseName = "school_database.db";

    //Integer's.
    private static final int databaseVersion = 1;

    //Database Value's.
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CLASS = "class";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_EXPERIENCE_POINTS = "experience_points";

    public static final String TABLE_CLASSES = "classes";
    public static final String COLUMN_CLASS_NAME = "class_name";

    //Test's.
    public static final String TABLE_TESTS = "tests";
    public static final String COLUMN_TEST_ID = "test_id";
    public static final String COLUMN_TEST_NAME = "test_name";
    public static final String COLUMN_TEST_QUESTIONS_COUNT = "questions_count";
    public static final String COLUMN_TEST_POINTS = "points";
    public static final String COLUMN_TEST_CLASS = "test_class";

    //Question's.
    public static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_QUESTION_ID = "question_id";
    public static final String COLUMN_QUESTION_TEXT = "question_text";
    public static final String COLUMN_QUESTION_TEST_ID = "question_test_id";

    //Answer's.
    public static final String TABLE_ANSWERS = "answers";
    public static final String COLUMN_ANSWER_ID = "answer_id";
    public static final String COLUMN_ANSWER_TEXT = "answer_text";
    public static final String COLUMN_ANSWER_CORRECT = "is_correct";
    public static final String COLUMN_ANSWER_QUESTION_ID = "answer_question_id";

    //Test Result's.
    public static final String TABLE_TEST_RESULTS = "test_results";
    public static final String COLUMN_RESULT_ID = "result_id";
    public static final String COLUMN_RESULT_USER_PHONE = "user_phone";
    public static final String COLUMN_RESULT_TEST_ID = "result_test_id";
    public static final String COLUMN_RESULT_POINTS = "result_points";

    //Grades.
    public static final String TABLE_GRADES = "grades";
    public static final String COLUMN_GRADE_ID = "grade_id";
    public static final String COLUMN_GRADE_USER_PHONE = "grade_user_phone";
    public static final String COLUMN_GRADE_TEXT = "grade_text";
    public static final String COLUMN_GRADE_DATE = "grade_date";
    public static final String COLUMN_GRADE_SCORE = "grade_score";

    //Schedule.
    public static final String TABLE_SCHEDULE = "schedule";
    public static final String COLUMN_SCHEDULE_ID = "schedule_id";
    public static final String COLUMN_SCHEDULE_DATE = "schedule_date";
    public static final String COLUMN_SCHEDULE_LESSONS_COUNT = "lessons_count";
    public static final String COLUMN_SCHEDULE_CLASS = "schedule_class";

    //Lessons.
    public static final String TABLE_LESSONS = "lessons";
    public static final String COLUMN_LESSON_ID = "lesson_id";
    public static final String COLUMN_LESSON_SCHEDULE_ID = "lesson_schedule_id";
    public static final String COLUMN_LESSON_SUBJECT = "lesson_subject";
    public static final String COLUMN_LESSON_HOMEWORK = "lesson_homework";

    //Notes.
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_NOTE_ID = "note_id";
    public static final String COLUMN_NOTE_USER_PHONE = "note_user_phone";
    public static final String COLUMN_NOTE_TITLE = "note_title";
    public static final String COLUMN_NOTE_CONTENT = "note_content";
    public static final String COLUMN_NOTE_DATE = "note_date";

    //Achievements.
    public static final String TABLE_ACHIEVEMENTS = "achievements";
    public static final String COLUMN_ACHIEVEMENT_ID = "achievement_id";
    public static final String COLUMN_ACHIEVEMENT_TITLE = "achievement_title";
    public static final String COLUMN_ACHIEVEMENT_CONTENT = "achievement_content";

    //User Achievements.
    public static final String TABLE_USER_ACHIEVEMENTS = "user_achievements";
    public static final String COLUMN_USER_ACHIEVEMENT_ID = "user_achievement_id";
    public static final String COLUMN_USER_ACHIEVEMENT_USER_PHONE = "user_phone";
    public static final String COLUMN_USER_ACHIEVEMENT_ACHIEVEMENT_ID = "achievement_id";

    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PHONE + " TEXT NOT NULL UNIQUE, " +
                COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_CLASS + " TEXT, " +
                COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                COLUMN_DESCRIPTION  + " TEXT, " +
                COLUMN_EXPERIENCE_POINTS + " INTEGER DEFAULT 0)";

        String createClassesTable = "CREATE TABLE " + TABLE_CLASSES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLASS_NAME + " TEXT NOT NULL UNIQUE)";

        String createTestsTable = "CREATE TABLE " + TABLE_TESTS + " (" +
                COLUMN_TEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEST_NAME + " TEXT NOT NULL, " +
                COLUMN_TEST_QUESTIONS_COUNT + " INTEGER NOT NULL, " +
                COLUMN_TEST_POINTS + " INTEGER NOT NULL, " +
                COLUMN_TEST_CLASS + " TEXT NOT NULL)";

        String createQuestionsTable = "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION_TEXT + " TEXT NOT NULL, " +
                COLUMN_QUESTION_TEST_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_QUESTION_TEST_ID + ") REFERENCES " + TABLE_TESTS + "(" + COLUMN_TEST_ID + "))";

        String createAnswersTable = "CREATE TABLE " + TABLE_ANSWERS + " (" +
                COLUMN_ANSWER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ANSWER_TEXT + " TEXT NOT NULL, " +
                COLUMN_ANSWER_CORRECT + " INTEGER NOT NULL, " +
                COLUMN_ANSWER_QUESTION_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_ANSWER_QUESTION_ID + ") REFERENCES " + TABLE_QUESTIONS + "(" + COLUMN_QUESTION_ID + "))";

        String createTestResultsTable = "CREATE TABLE " + TABLE_TEST_RESULTS + " (" +
                COLUMN_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RESULT_USER_PHONE + " TEXT NOT NULL, " +
                COLUMN_RESULT_TEST_ID + " INTEGER NOT NULL, " +
                COLUMN_RESULT_POINTS + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_RESULT_USER_PHONE + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_PHONE + "), " +
                "FOREIGN KEY (" + COLUMN_RESULT_TEST_ID + ") REFERENCES " + TABLE_TESTS + "(" + COLUMN_TEST_ID + "))";

        String createGradesTable = "CREATE TABLE " + TABLE_GRADES + " (" +
                COLUMN_GRADE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GRADE_USER_PHONE + " TEXT NOT NULL, " +
                COLUMN_GRADE_TEXT + " TEXT NOT NULL, " +
                COLUMN_GRADE_DATE + " TEXT NOT NULL, " +
                COLUMN_GRADE_SCORE + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_GRADE_USER_PHONE + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_PHONE + "))";

        String createScheduleTable = "CREATE TABLE " + TABLE_SCHEDULE + " (" +
                COLUMN_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCHEDULE_DATE + " TEXT NOT NULL, " +
                COLUMN_SCHEDULE_LESSONS_COUNT + " INTEGER NOT NULL, " +
                COLUMN_SCHEDULE_CLASS + " TEXT NOT NULL)";

        String createLessonsTable = "CREATE TABLE " + TABLE_LESSONS + " (" +
                COLUMN_LESSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LESSON_SCHEDULE_ID + " INTEGER NOT NULL, " +
                COLUMN_LESSON_SUBJECT + " TEXT NOT NULL, " +
                COLUMN_LESSON_HOMEWORK + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_LESSON_SCHEDULE_ID + ") REFERENCES " + TABLE_SCHEDULE + "(" + COLUMN_SCHEDULE_ID + "))";

        String createNotesTable = "CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE_USER_PHONE + " TEXT NOT NULL, " +
                COLUMN_NOTE_TITLE + " TEXT NOT NULL, " +
                COLUMN_NOTE_CONTENT + " TEXT NOT NULL, " +
                COLUMN_NOTE_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_NOTE_USER_PHONE + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_PHONE + "))";

        String createAchievementsTable = "CREATE TABLE " + TABLE_ACHIEVEMENTS + " (" +
                COLUMN_ACHIEVEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACHIEVEMENT_TITLE + " TEXT NOT NULL, " +
                COLUMN_ACHIEVEMENT_CONTENT + " TEXT NOT NULL)";

        String createUserAchievementsTable = "CREATE TABLE " + TABLE_USER_ACHIEVEMENTS + " (" +
                COLUMN_USER_ACHIEVEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ACHIEVEMENT_USER_PHONE + " TEXT NOT NULL, " +
                COLUMN_USER_ACHIEVEMENT_ACHIEVEMENT_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_USER_ACHIEVEMENT_USER_PHONE + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_PHONE + "), " +
                "FOREIGN KEY (" + COLUMN_USER_ACHIEVEMENT_ACHIEVEMENT_ID + ") REFERENCES " + TABLE_ACHIEVEMENTS + "(" + COLUMN_ACHIEVEMENT_ID + "))";

        database.execSQL(createUsersTable);
        database.execSQL(createClassesTable);
        database.execSQL(createTestsTable);
        database.execSQL(createQuestionsTable);
        database.execSQL(createAnswersTable);
        database.execSQL(createTestResultsTable);
        database.execSQL(createGradesTable);
        database.execSQL(createScheduleTable);
        database.execSQL(createLessonsTable);
        database.execSQL(createNotesTable);
        database.execSQL(createAchievementsTable);
        database.execSQL(createUserAchievementsTable);

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, "5А");
        database.insert(TABLE_CLASSES, null, values);
        values.put(COLUMN_CLASS_NAME, "5Б");
        database.insert(TABLE_CLASSES, null, values);
        values.put(COLUMN_CLASS_NAME, "5В");
        database.insert(TABLE_CLASSES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldBase, int newBase) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_RESULTS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENTS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ACHIEVEMENTS);
        onCreate(database);
    }

    public long addUser(String phone, String email, String password, String username) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_DESCRIPTION, (String)null);
        values.put(COLUMN_EXPERIENCE_POINTS, 0);
        return database.insert(TABLE_USERS, null, values);
    }

    public Cursor getUserByPhone(String phoneNumber) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_USERS,
                new String[] { COLUMN_ID, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_CLASS, COLUMN_USERNAME, COLUMN_DESCRIPTION, COLUMN_EXPERIENCE_POINTS},
                COLUMN_PHONE + " =?",
                new String[] { phoneNumber },
                null,
                null,
                null
        );
    }

    public Cursor getUserByUsername(String username) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_USERS,
                new String[] { COLUMN_ID, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_CLASS, COLUMN_USERNAME, COLUMN_DESCRIPTION, COLUMN_EXPERIENCE_POINTS},
                COLUMN_USERNAME + " =?",
                new String[] { username },
                null,
                null,
                null
        );
    }

    //Classes Works Method.
    public Cursor getAllClasses() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_CLASSES,
                new String[] { COLUMN_ID, COLUMN_CLASS_NAME },
                null,
                null,
                null,
                null,
                null
        );
    }

    public Cursor getStudentsInClass(String className) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_USERS,
                new String[] { COLUMN_ID, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_CLASS, COLUMN_USERNAME, COLUMN_DESCRIPTION, COLUMN_EXPERIENCE_POINTS },
                COLUMN_CLASS + " =?",
                new String[] { className },
                null,
                null,
                COLUMN_EXPERIENCE_POINTS + " DESC"
        );
    }

    public int getStudentCountInClass(String className) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_USERS + " WHERE " + COLUMN_CLASS + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{className});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int updateUserClass(String phoneNumber, String className) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS, className);
        return database.update(
                TABLE_USERS,
                values,
                COLUMN_PHONE + " =?",
                new String[] { phoneNumber }
        );
    }

    //Point Works Method.
    public int updateUserExperiencePoints(String phoneNumber, int pointsToAdd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPERIENCE_POINTS, pointsToAdd);

        Cursor cursor = getUserByPhone(phoneNumber);
        int currentPoints = 0;
        if (cursor != null && cursor.moveToFirst()) {
            currentPoints = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCE_POINTS));
            cursor.close();
        }

        int newPoints = currentPoints + pointsToAdd;
        values.put(COLUMN_EXPERIENCE_POINTS, newPoints);

        return database.update(
                TABLE_USERS,
                values,
                COLUMN_PHONE + " =?",
                new String[] { phoneNumber }
        );
    }

    //Description Works Method.
    public int updateUserDescription(String phoneNumber, String description) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);
        return database.update(
                TABLE_USERS,
                values,
                COLUMN_PHONE + " =?",
                new String[] { phoneNumber }
        );
    }

    //Tests Works Methods.
    public long addTest(String testName, int questionsCount, int points, String className) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEST_NAME, testName);
        values.put(COLUMN_TEST_QUESTIONS_COUNT, questionsCount);
        values.put(COLUMN_TEST_POINTS, points);
        values.put(COLUMN_TEST_CLASS, className);
        return database.insert(TABLE_TESTS, null, values);
    }

    public Cursor getTestsByClass(String className) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_TESTS,
                new String[] { COLUMN_TEST_ID, COLUMN_TEST_NAME, COLUMN_TEST_QUESTIONS_COUNT, COLUMN_TEST_POINTS, COLUMN_TEST_CLASS },
                COLUMN_TEST_CLASS + " =?",
                new String[] { className },
                null,
                null,
                null
        );
    }

    public boolean isTestExists(String testName, String className) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TESTS +
                " WHERE " + COLUMN_TEST_NAME + " =? AND " + COLUMN_TEST_CLASS + " =?";
        Cursor cursor = database.rawQuery(query, new String[]{testName, className});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean hasUserPassedTest(String userPhone, int testId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TEST_RESULTS + " WHERE " + COLUMN_RESULT_USER_PHONE + " =? AND " + COLUMN_RESULT_TEST_ID + " =?";
        Cursor cursor = database.rawQuery(query, new String[] { userPhone, String.valueOf(testId) });

        boolean isPasses = cursor.moveToFirst();
        cursor.close();
        return isPasses;
    }

    public int getTestPointsById(int testId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_TEST_POINTS + " FROM " + TABLE_TESTS + " WHERE " + COLUMN_TEST_ID + " =?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(testId)});
        int points = 0;
        if (cursor != null && cursor.moveToFirst()) {
            points = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TEST_POINTS));
            cursor.close();
        }
        return points;
    }

    public String getTestNameById(int testId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_TEST_NAME + " FROM " + TABLE_TESTS + " WHERE " + COLUMN_TEST_ID + " =?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(testId)});
        String testName = null;
        if (cursor != null && cursor.moveToFirst()) {
            testName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEST_NAME));
            cursor.close();
        }
        return testName;
    }

    //Question works Methods.
    public long addQuestion(int testId, String questionText) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_TEST_ID, testId);
        values.put(COLUMN_QUESTION_TEXT, questionText);
        return database.insert(TABLE_QUESTIONS, null, values);
    }

    public Cursor getQuestionsByTestId(int testId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_QUESTIONS,
                new String[] { COLUMN_QUESTION_ID, COLUMN_QUESTION_TEXT, COLUMN_QUESTION_TEST_ID },
                COLUMN_QUESTION_TEST_ID + " =?",
                new String[] { String.valueOf(testId)},
                null,
                null,
                null
        );
    }

    //Answer works Methods.
    public long addAnswer(int questionId, String answerText, boolean isCorrect) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ANSWER_QUESTION_ID, questionId);
        values.put(COLUMN_ANSWER_TEXT, answerText);
        values.put(COLUMN_ANSWER_CORRECT, isCorrect ? 1 : 0);
        return database.insert(TABLE_ANSWERS, null, values);
    }

    public Cursor getAnswersByQuestionId(int questionId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_ANSWERS,
                new String[] { COLUMN_ANSWER_ID, COLUMN_ANSWER_TEXT, COLUMN_ANSWER_CORRECT, COLUMN_ANSWER_QUESTION_ID },
                COLUMN_ANSWER_QUESTION_ID + " =?",
                new String[] { String.valueOf(questionId) },
                null,
                null,
                null
        );
    }

    //Result works Methods.
    public long addTestResult(String userPhone, int testId, int points) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESULT_USER_PHONE, userPhone);
        values.put(COLUMN_RESULT_TEST_ID, testId);
        values.put(COLUMN_RESULT_POINTS, points);
        return database.insert(TABLE_TEST_RESULTS, null, values);
    }

    //Grades works Methods.
    public long addGrade(String userPhone, String gradeText, String date, int score) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GRADE_USER_PHONE, userPhone);
        values.put(COLUMN_GRADE_TEXT, gradeText);
        values.put(COLUMN_GRADE_DATE, date);
        values.put(COLUMN_GRADE_SCORE, score);
        return database.insert(TABLE_GRADES, null, values);
    }

    public Cursor getGradesByUser(String userPhone) {
        SQLiteDatabase database = getReadableDatabase();
        return database.query(
                TABLE_GRADES,
                new String[] { COLUMN_GRADE_ID, COLUMN_GRADE_USER_PHONE, COLUMN_GRADE_TEXT, COLUMN_GRADE_DATE, COLUMN_GRADE_SCORE },
                COLUMN_GRADE_USER_PHONE + " =?",
                new String[] { userPhone },
                null,
                null,
                COLUMN_GRADE_DATE + " DESC"
        );
    }

    //Schelude works Methods.
    public long addSchedule(String date, int lessonsCount, String className) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCHEDULE_DATE, date);
        values.put(COLUMN_SCHEDULE_LESSONS_COUNT, lessonsCount);
        values.put(COLUMN_SCHEDULE_CLASS, className);
        return database.insert(TABLE_SCHEDULE, null, values);
    }

    public Cursor getAllSchedules() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_SCHEDULE,
                new String[]{COLUMN_SCHEDULE_ID, COLUMN_SCHEDULE_DATE, COLUMN_SCHEDULE_LESSONS_COUNT, COLUMN_SCHEDULE_CLASS},
                null,
                null,
                null,
                null,
                COLUMN_SCHEDULE_DATE + " ASC"
        );
    }

    public boolean isExistScheduleIsDate(String date, String className) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_SCHEDULE + " WHERE " + COLUMN_SCHEDULE_DATE + " =? AND " + COLUMN_SCHEDULE_CLASS + " =?";
        Cursor cursor = database.rawQuery(query, new String[]{date, className});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public Cursor getSchedulesByClass(String className) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_SCHEDULE,
                new String[] { COLUMN_SCHEDULE_ID, COLUMN_SCHEDULE_DATE, COLUMN_SCHEDULE_LESSONS_COUNT, COLUMN_SCHEDULE_CLASS },
                COLUMN_SCHEDULE_CLASS + " =?",
                new String[] { className },
                null,
                null,
                COLUMN_SCHEDULE_DATE + " ASC"
        );
    }

    //Lesson works Method.
    public long addLesson(long scheduleId, String subject, String homework) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LESSON_SCHEDULE_ID, scheduleId);
        values.put(COLUMN_LESSON_SUBJECT, subject);
        values.put(COLUMN_LESSON_HOMEWORK, homework);
        return database.insert(TABLE_LESSONS, null, values);
    }

    public Cursor getLessonsByScheduleId(long scheduleId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_LESSONS,
                new String[]{ COLUMN_LESSON_ID, COLUMN_LESSON_SCHEDULE_ID, COLUMN_LESSON_SUBJECT, COLUMN_LESSON_HOMEWORK },
                COLUMN_LESSON_SCHEDULE_ID + " =?",
                new String[]{String.valueOf(scheduleId)},
                null,
                null,
                null
        );
    }

    //Notes works Methods.
    public long addNote(String userPhone, String title, String content, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_USER_PHONE, userPhone);
        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE_CONTENT, content);
        values.put(COLUMN_NOTE_DATE, date);
        return database.insert(TABLE_NOTES, null, values);
    }

    public long updateNote(int noteId, String title, String content, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE_CONTENT, content);
        values.put(COLUMN_NOTE_DATE, date);
        return database.update(TABLE_NOTES, values, COLUMN_NOTE_ID + " =?", new String[]{String.valueOf(noteId)});
    }

    public int deleteNote(int noteId) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NOTES, COLUMN_NOTE_ID + " =?", new String[]{String.valueOf(noteId)});
    }

    public Cursor getNotesByUser(String userPhone) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_NOTES,
                new String[]{ COLUMN_NOTE_ID, COLUMN_NOTE_USER_PHONE, COLUMN_NOTE_TITLE, COLUMN_NOTE_CONTENT, COLUMN_NOTE_DATE },
                COLUMN_NOTE_USER_PHONE + " =?",
                new String[]{ userPhone },
                null,
                null,
                COLUMN_NOTE_DATE + " DESC"
        );
    }

    //Achievement works Methods.
    public long addAchievement(String title, String content) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACHIEVEMENT_TITLE, title);
        values.put(COLUMN_ACHIEVEMENT_CONTENT, content);
        return database.insert(TABLE_ACHIEVEMENTS, null, values);
    }

    public Cursor getUserAchievements(String userPhone) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT a." + COLUMN_ACHIEVEMENT_ID + ", a." + COLUMN_ACHIEVEMENT_TITLE + ", a." + COLUMN_ACHIEVEMENT_CONTENT +
                " FROM " + TABLE_ACHIEVEMENTS + " a " +
                "JOIN " + TABLE_USER_ACHIEVEMENTS + " ua ON a." + COLUMN_ACHIEVEMENT_ID + " = ua." + COLUMN_USER_ACHIEVEMENT_ACHIEVEMENT_ID +
                " WHERE ua." + COLUMN_USER_ACHIEVEMENT_USER_PHONE + " = ?";
        return database.rawQuery(query, new String[] { userPhone });
    }

    public long addUserAchievement(String userPhone, long achievementId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ACHIEVEMENT_USER_PHONE, userPhone);
        values.put(COLUMN_USER_ACHIEVEMENT_ACHIEVEMENT_ID, achievementId);
        return database.insert(TABLE_USER_ACHIEVEMENTS, null, values);
    }

    public boolean isAchievementExistsByName(String achievementTitle) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ACHIEVEMENTS +
                " WHERE " + COLUMN_ACHIEVEMENT_TITLE + " =?";
        Cursor cursor = database.rawQuery(query, new String[]{achievementTitle});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean isUserHasAchievement(String userPhone, long achievementId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER_ACHIEVEMENTS +
                " WHERE " + COLUMN_USER_ACHIEVEMENT_USER_PHONE + " =? AND " + COLUMN_USER_ACHIEVEMENT_ACHIEVEMENT_ID + " =?";
        Cursor cursor = database.rawQuery(query, new String[]{userPhone, String.valueOf(achievementId)});
        boolean hasAchievement = cursor.moveToFirst();
        cursor.close();
        return hasAchievement;
    }
}
