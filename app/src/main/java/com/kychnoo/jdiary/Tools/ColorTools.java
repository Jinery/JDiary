package com.kychnoo.jdiary.Tools;

import android.graphics.Color;

public class ColorTools {

    public static boolean isLightColor(int color) {
        double brightness = calculateBrightness(color);
        return brightness > 128;
    }

    public static double calculateBrightness(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return (0.299 * red + 0.587 * green + 0.114 * blue);
    }
}
