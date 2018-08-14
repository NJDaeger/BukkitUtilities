package com.njdaeger.bci;

public final class Utils {
    
    private Utils() {}
    
    public static String formatString(String message, Object... placeholders) {
        for (int i = 0; i < placeholders.length; i++) {
            message = message.replace("{" + i + "}", (placeholders[i] != null ? placeholders[i].toString() : "null"));
        }
        return message;
    }
    
}
