package com.psm.utils.Long;

public class LongUtils {
    public static boolean stringCanBeConvertedToLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
