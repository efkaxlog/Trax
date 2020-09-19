package com.bajera.xlog.trax.util;

public class StringUtils {

    public static String format(float value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.format("%.1f", value);
        }
    }
}
