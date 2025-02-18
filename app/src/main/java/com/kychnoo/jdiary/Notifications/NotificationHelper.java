package com.kychnoo.jdiary.Notifications;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kychnoo.jdiary.R;

public class NotificationHelper {

    private final static int NOTIFICATION_DURATION_TIME = 5000;
    private static final String NOTIFICATION_TAG = "main_notification";

    public enum NotificationColor {
        SUCCESS(Color.GREEN),
        ERROR(Color.RED),
        WARNING(Color.YELLOW);

        private final int color;

        NotificationColor(int color) {
            this.color = color;
        }

        public  int getColor() {
            return color;
        }
    }

    private static View notificationView;
    private static WindowManager windowManager;
    private static WindowManager.LayoutParams params;

    public static void showNotification(Context context, String text, NotificationColor color) {
        if (!(context instanceof Activity)) {
            return;
        }

        Activity activity = (Activity) context;

        if (windowManager == null) {
            windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        }

        if (notificationView != null) {
            windowManager.removeView(notificationView);
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        notificationView = inflater.inflate(R.layout.notification_layout, null);
        TextView textView = notificationView.findViewById(R.id.notification_text);
        FrameLayout container = notificationView.findViewById(R.id.notification_container);

        textView.setText(text);
        container.setBackgroundColor(color.getColor());

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                android.graphics.PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.BOTTOM;
        params.y = 100;

        windowManager.addView(notificationView, params);

        notificationView.setTranslationY(notificationView.getHeight());
        notificationView.animate().translationY(0).setDuration(300).start();

        new Handler().postDelayed(() -> {
            notificationView.animate().translationY(notificationView.getHeight()).setDuration(300).withEndAction(() -> {
                windowManager.removeView(notificationView);
                notificationView = null;
            }).start();
        }, NOTIFICATION_DURATION_TIME);
    }

}
