package com.neonclient.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {
    public String formatTimeFromSeconds(long seconds) {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (days > 0) {
            return days + "d";
        } else if (hours > 0) {
            return hours + "h";
        } else {
            if (minutes == 0) {
                return secs + "s";
            } else {
                return minutes + "m";
            }
        }
    }
}
