package com.kychnoo.jdiary.Managers;

import com.kychnoo.jdiary.Interfaces.AchievementEventListener;
import com.kychnoo.jdiary.OtherClasses.TestResult;

public class AchievementManager {
    private static AchievementManager instance;
    private AchievementEventListener listener;

    private AchievementManager() {}

    public static AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    public void registerListener(AchievementEventListener listener) {
        this.listener = listener;
    }

    public void notifyTestCompleted(TestResult result) {
        if (listener != null) {
            listener.onTestCompleted(result);
        }
    }
}
