package com.kychnoo.jdiary.OtherClasses;

import android.content.Context;
import android.content.res.Resources;

public class Achievement {
    public static final int RARITY_BRONZE = 0;
    public static final int RARITY_SILVER = 1;
    public static final int RARITY_GOLD = 2;

    private long id;
    private String title;
    private String content;
    private int iconResId;
    private int rarity;
    private int backgroundColor;


    public Achievement(long id, String title, String content, int iconResId, int rarity, int backgroundColor) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.iconResId = iconResId;
        this.rarity = rarity;
        this.backgroundColor = backgroundColor;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getRarity() {
        return rarity;
    }

    public int getIconResId() {
        return iconResId;
    }

    public  int getBackgroundColor() {
        return backgroundColor;
    }

    public String getIconResName(Context context) {
        try {
            return context.getResources().getResourceEntryName(this.iconResId);
        } catch (Resources.NotFoundException e) {
            return "ic_trophy_bronze";
        }
    }
}
