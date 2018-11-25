package com.njdaeger.butil;

import org.bukkit.plugin.Plugin;

import java.io.*;
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

    /**
     * Moves a file or directory from a
     * @param plugin The plugin installing a file.
     * @param pathInJar The path to the file in the jar file. MUST HAVE EXTENSION
     * @param destination where you want this file moved to. MUST HAVE EXTENSION
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void installFile(Plugin plugin, String pathInJar, String destination) {
        if (!destination.contains(".")) throw new RuntimeException("File type must be specified.");
        try {
            File path;
            File file;

            if (!destination.contains(File.separator)) {
                path = plugin.getDataFolder();
                file = new File(path.getAbsolutePath() + File.separator + destination);
            }
            else {
                int last = destination.lastIndexOf(File.separator);
                String fileName = destination.substring(last + 1);
                String directory = destination.substring(0, last);
                path = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + directory);
                file = new File(path + File.separator + fileName);
            }

            if (!path.exists()) path.mkdirs();
            if (!file.exists()) file.createNewFile();
            else {
                file.delete();
                file.createNewFile();
            }
            InputStream stream;
            OutputStream resStreamOut;

            stream = plugin.getClass().getResourceAsStream(pathInJar);

            if (stream == null) {
                throw new RuntimeException("the path specified could not be found in the jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(file);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            stream.close();
            resStreamOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
