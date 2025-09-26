package com.kychnoo.jdiary.Managers;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.kychnoo.jdiary.R;

public class LevelManager {

    private Context context;

    private static LevelManager instance;

    public static LevelManager getInstance(Context context) {
        if(instance == null) {
            instance = new LevelManager(context);
        }
        return instance;
    }

    private static final int[] levelThresholds = {100, 300, 500, 700, 850, 1000, 1150, 1300, 1500};

    private LevelManager(Context context) {
        this.context = context;
    }

    public int getLevel(int experiencePoints) {
        for(int index = 0; index < levelThresholds.length; index++) {
            if(experiencePoints < levelThresholds[index]) {
                return index + 1;
            }
        }
        return levelThresholds.length + 1;
    }

    public int getProgress(int experiencePoints) {
        int level = getLevel(experiencePoints);

        if(level == 1) {
            return (experiencePoints * 100) / levelThresholds[0];
        } else if (level > levelThresholds.length) {
            return 100;
        } else {
            int currentLevelThreshold = levelThresholds[level - 2];
            int nextLevelThreshold = levelThresholds[level - 1];
            return ((experiencePoints - currentLevelThreshold) * 100) / (nextLevelThreshold - currentLevelThreshold);
        }
    }

    public int getCurrentLevelThreshold(int experiencePoints) {
        int level = getLevel(experiencePoints);

        if (level > levelThresholds.length)
            return levelThresholds[levelThresholds.length - 1];
        return levelThresholds[level - 1];
    }

    public int getNextLevelThreshold(int experiencePoints) {
        int level = getLevel(experiencePoints);
        if(level > levelThresholds.length)
            return levelThresholds[levelThresholds.length - 1];

        return levelThresholds[level - 1];
    }

    public int getColorForLevel(int level) {
        int[] colorResIds = {
                R.color.gray,
                R.color.blue,
                R.color.green,
                R.color.orange,
                R.color.gold,
                R.color.epic_mystic_purple,
                R.color.red,
                R.color.darkslate_blue,
                R.color.bronze,
                R.color.sienna
        };

        int colorIndex = Math.min(level - 1, colorResIds.length - 1);
        return ContextCompat.getColor(context, colorResIds[colorIndex]);
    }
}
