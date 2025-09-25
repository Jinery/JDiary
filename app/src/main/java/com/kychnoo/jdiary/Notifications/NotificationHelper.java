package com.kychnoo.jdiary.Notifications;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;


import com.google.android.material.snackbar.Snackbar;
import com.kychnoo.jdiary.R;

public class NotificationHelper {

    public enum NotificationColor {
        SUCCESS(R.color.success_notification),
        ERROR(R.color.error_notification),
        WARNING(R.color.warning_notification),
        INFO(R.color.info_notification);

        private final int colorResId;

        NotificationColor(int colorResId) {
            this.colorResId = colorResId;
        }

        public int getColorResId() {
            return colorResId;
        }

        public ColorDrawable getColorDrawable(Context context) {
            int resolvedColor = ContextCompat.getColor(context, colorResId);
            return new ColorDrawable(resolvedColor);
        }
    }

    public static void show(Activity activity, String message, NotificationColor color, int duration) {

        if(activity == null || activity.isFinishing()) return;
        if(TextUtils.isEmpty(message)) return;

        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);

        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE);

        snackbar.setBackgroundTint(ContextCompat.getColor(activity, color.getColorResId()));

        if(color == NotificationColor.SUCCESS || color == NotificationColor.ERROR) {
            snackbar.setTextColor(Color.WHITE);
        } else {
            snackbar.setTextColor(Color.BLACK);
        }
        snackbar.setTextMaxLines(3);

        if (duration > 0) {
            snackbar.setDuration(duration);
        }

        snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }
}
