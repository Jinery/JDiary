package com.kychnoo.jdiary.Helpers;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.kychnoo.jdiary.R;

public class SubjectColorHelper {

    public static int getSubjectColor(Context context, String subjectName) {
        if (subjectName == null) return getDefaultColor(context);

        String lowerSubject = subjectName.toLowerCase();

        if (lowerSubject.contains("матема")) {
            return ContextCompat.getColor(context, R.color.math_color);
        } else if (lowerSubject.contains("русск") || lowerSubject.contains("литератур")) {
            return ContextCompat.getColor(context, R.color.language_color);
        } else if (lowerSubject.contains("физик")) {
            return ContextCompat.getColor(context, R.color.physics_color);
        } else if (lowerSubject.contains("хими")) {
            return ContextCompat.getColor(context, R.color.chemistry_color);
        } else if (lowerSubject.contains("биолог")) {
            return ContextCompat.getColor(context, R.color.biology_color);
        } else if (lowerSubject.contains("истори")) {
            return ContextCompat.getColor(context, R.color.history_color);
        } else if (lowerSubject.contains("англий")) {
            return ContextCompat.getColor(context, R.color.english_color);
        } else if (lowerSubject.contains("физкультур")) {
            return ContextCompat.getColor(context, R.color.sport_color);
        } else {
            return getDefaultColor(context);
        }
    }

    public static int getSubjectIcon(String subjectName) {
        if (subjectName == null) return R.drawable.ic_lesson;

        String lowerSubject = subjectName.toLowerCase();

        if (lowerSubject.contains("матема")) {
            return R.drawable.ic_math;
        } else if (lowerSubject.contains("русск") || lowerSubject.contains("литератур")) {
            return R.drawable.ic_language;
        } else if (lowerSubject.contains("физик")) {
            return R.drawable.ic_physics;
        } else if (lowerSubject.contains("хими")) {
            return R.drawable.ic_chemistry;
        } else if (lowerSubject.contains("биолог")) {
            return R.drawable.ic_biology;
        } else if (lowerSubject.contains("истори")) {
            return R.drawable.ic_history;
        } else if (lowerSubject.contains("англий")) {
            return R.drawable.ic_english;
        } else if (lowerSubject.contains("физкультур")) {
            return R.drawable.ic_sport;
        } else {
            return R.drawable.ic_lesson;
        }
    }

    private static int getDefaultColor(Context context) {
        return ContextCompat.getColor(context, R.color.card_background);
    }
}
