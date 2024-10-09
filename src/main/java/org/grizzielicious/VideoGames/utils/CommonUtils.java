package org.grizzielicious.VideoGames.utils;

public class CommonUtils {
    public static boolean isIntegerParseable(String str) {
        boolean isParseable = true;
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            isParseable = false;
        }
        return isParseable;
    }
}
