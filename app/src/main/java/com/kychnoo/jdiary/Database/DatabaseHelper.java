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

    public static final String TABLE_CLASSES = "classes";
    public static final String COLUMN_CLASS_NAME = "class_name";


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
                COLUMN_DESCRIPTION  + " TEXT)";

        String createClassesTable = "CREATE TABLE " + TABLE_CLASSES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLASS_NAME + " TEXT NOT NULL UNIQUE)";

        database.execSQL(createUsersTable);
        database.execSQL(createClassesTable);

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, "5А");
        database.insert(TABLE_CLASSES, null, values);
        values.put(COLUMN_CLASS_NAME, "5Б");
        database.insert(TABLE_CLASSES, null, values);
        values.put(COLUMN_CLASS_NAME, "5В");
        database.insert(TABLE_CLASSES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
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
        return database.insert(TABLE_USERS, null, values);
    }

    public Cursor getUserByPhone(String phoneNumber) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_USERS,
                new String[] { COLUMN_ID, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_CLASS, COLUMN_USERNAME, COLUMN_DESCRIPTION },
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
                new String[] { COLUMN_ID, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_CLASS, COLUMN_USERNAME, COLUMN_DESCRIPTION },
                COLUMN_USERNAME + " =?",
                new String[] { username },
                null,
                null,
                null
        );
    }

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
                new String[] { COLUMN_ID, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_CLASS, COLUMN_USERNAME, COLUMN_DESCRIPTION },
                COLUMN_CLASS + " =?",
                new String[] { className },
                null,
                null,
                null
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
}
