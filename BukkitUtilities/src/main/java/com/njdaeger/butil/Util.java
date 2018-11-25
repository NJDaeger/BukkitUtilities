package com.njdaeger.butil;

import java.util.Arrays;
import java.util.List;

public class Util {

    /**
     * Formats a message and replaces placeholders
     *
     * @param message The message to format
     * @param objects The objects used to replace the placeholders
     * @return The formatted string
     * <p>
     * Example placeholders: <code>formatString("Hello, I am {0}, a {1} year old person.", "NJDaeger", "100")</code>
     * <p>that would return "Hello, I am NJDaeger, a 100 year old person.</p>
     */
    public static String formatString(String message, Object... objects) {
        List<Object> fillers = Arrays.asList(objects);
        for (int i = 0; i < fillers.size(); i++) {
            if (fillers.get(i) != null) {
                message = message.replace("{" + i + "}", fillers.get(i).toString());
            }
        }
        return message;
    }

}
