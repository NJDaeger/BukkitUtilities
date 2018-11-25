package com.njdaeger.butil.i18n;

import com.njdaeger.butil.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Lang<M extends Enum<M> & Translatable> {

    private final File file;
    private final Locale locale;
    private final char colorChar;
    private final Map<M, String> keys;
    private final Class<M> enumClass;
    private LoadStatus loadStatus;

    public Lang(Class<M> enumClass, File langFile, Locale locale) {
        this(enumClass, langFile, locale, '&');
    }

    public Lang(Class<M> enumClass, File langFile, Locale locale, char colorChar) {
        this.enumClass = enumClass;
        this.file = langFile;
        this.locale = locale;
        this.colorChar = colorChar;
        this.keys = decode();
    }

    private Map<M, String> decode() {
        loadStatus = LoadStatus.CLEAR;
        try {
            Map<M, String> keys = new HashMap<>();
            YamlConfiguration lang = YamlConfiguration.loadConfiguration(file);
            for (M key : enumClass.getEnumConstants()) {
                if (!lang.contains(key.getKey())) {
                    if (loadStatus != LoadStatus.WARNING) loadStatus = LoadStatus.WARNING;
                    Bukkit.getLogger().warning("Could not find key \"" + key.getKey() + "\" in " + file.getName());
                } else keys.put(key, key.format(lang.getString(key.getKey())));
            }
            return keys;
        } catch (Exception e) {
            loadStatus = LoadStatus.ERROR;
        }
        return null;
    }

    /**
     * Get the file this lang uses for translations
     * @return The lang file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Gets the load status of this lang
     *
     * @return The lang load status.
     */
    public LoadStatus getLoadStatus() {
        return loadStatus;
    }

    /**
     * Gets the locale this lang is translated for
     *
     * @return The locale this lang represents.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Gets all the translated keys in this locale.
     *
     * @return All the translated keys
     */
    public Map<M, String> getKeys() {
        return keys;
    }

    /**
     * Translates a key to a usable string
     *
     * @param key The key to translate
     * @return The translated anf formatted key
     */
    public String translate(M key) {
        return keys.containsKey(key) ? ChatColor.translateAlternateColorCodes(colorChar, keys.get(key)) : null;
    }

    /**
     * Translates a key to a usable string
     *
     * @param key The key to translate
     * @param placeholders The placeholders to use in the key
     * @return The translated and formatted key
     */
    public String translate(M key, Object... placeholders) {
        return keys.containsKey(key) ? ChatColor.translateAlternateColorCodes(colorChar, Util.formatString(keys.get(key), placeholders)) : null;
    }

    public enum LoadStatus {

        /**
         * Lang was loaded with no errors or warnings.
         */
        CLEAR("[{0}] Lang is fully loaded with no errors or warnings."),
        /**
         * Lang was loaded with warnings.
         */
        WARNING("[{0}] Lang is loaded, but with warnings."),
        /**
         * Lang was not able to be loaded.
         */
        ERROR("[{0}] Lang was unable to be loaded.");

        private String message;

        LoadStatus(String message) {
            this.message = message;
        }

        public String getMessage(String file) {
            return Util.formatString(message, file);
        }
    }

}
