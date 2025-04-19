package com.kychnoo.jdiary.Achievements;

import static com.kychnoo.jdiary.Database.DatabaseHelper.COLUMN_TEST_ID;

import android.content.Context;
import android.database.Cursor;

import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.OtherClasses.Achievement;

public class AchievementsHelper {

    private static DatabaseHelper databaseHelper;

    public static void initialize(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context.getApplicationContext());
        }

        createAchievements();
    }

    private static void createAchievements() {

        if(!databaseHelper.isAchievementExistsByName("Теперь я с вами"))
            databaseHelper.addAchievement("Теперь я с вами", "Зарегистрироваться в приложении.", "ic_new_user", Achievement.RARITY_BRONZE);

        if(!databaseHelper.isAchievementExistsByName("Это только начало"))
            databaseHelper.addAchievement("Это только начало", "Пройти свой первый тест.", "ic_first_test", Achievement.RARITY_BRONZE);

    }

    public static void startApp(String phoneNumber) {
        if(!databaseHelper.isUserHasAchievement(phoneNumber, 1))
            databaseHelper.addUserAchievement(phoneNumber, 1);
    }

    public static void passedTest(String phoneNumber) {

        String className = null;
        Cursor cursor = databaseHelper.getUserByPhone(phoneNumber);
        if(cursor != null && cursor.moveToFirst()) {
            className = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
            cursor.close();
        }

        if(getUserPassedTestsCount(phoneNumber, className) == 1) {
            if(!databaseHelper.isUserHasAchievement(phoneNumber, 2))
                databaseHelper.addUserAchievement(phoneNumber, 2);
        }
    }

    private static int getUserPassedTestsCount(String phoneNumber, String className) {

        Cursor testsCursor = databaseHelper.getTestsByClass(className);
        int passedTestsCount = 0;

        if (testsCursor != null && testsCursor.moveToFirst()) {
            do {
                int testId = testsCursor.getInt(testsCursor.getColumnIndexOrThrow(COLUMN_TEST_ID));
                if (databaseHelper.hasUserPassedTest(phoneNumber, testId)) {
                    passedTestsCount++;
                }
            } while (testsCursor.moveToNext());
            testsCursor.close();
        }

        return passedTestsCount;
    }

}
