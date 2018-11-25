package com.njdaeger.butil.i18n;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LangStore<M extends Enum<M> & Translatable> {

    private final Map<Locale, Lang<M>> langMap;
    private final Class<M> enumClass;

    /**
     * Creates a new lang store object.
     * @param enumClass The class type of the messages enum class.
     */
    public LangStore(Class<M> enumClass) {
        this.langMap = new HashMap<>();
        this.enumClass = enumClass;
    }

    /**
     * Gets the enum class associated with the language keys.
     * @return The enum class
     */
    public Class<M> getEnumClass() {
        return enumClass;
    }

    /**
     * Gets a lang based on locale.
     * @param locale The locale to look for
     * @return The lang if it exists in the lang map, null otherwise
     */
    public Lang<M> getLang(Locale locale) {
        return langMap.get(locale);
    }

    /**
     * Checks whether a specific locale has been loaded yet
     * @param locale The locale to check
     * @return True if the locale has been loaded, false otherwise.
     */
    public boolean hasLang(Locale locale) {
        return langMap.containsKey(locale);
    }

    /**
     * Loads a lang into this plugins lang store.
     * @param file The file location of this lang file
     * @param locale The locale this lang file is translated for
     * @return True if it was loaded successfully, false otherwise.
     */
    public boolean loadLang(File file, Locale locale) {
        return loadLang(new Lang<>(enumClass, file, locale));
    }

    /**
     * Loads a lang into this plugins lang store.
     * @param file The path to the location of the lang file
     * @param locale The locale this lang file is translated for
     * @return True if it was loaded successfully, false otherwise.
     */
    public boolean loadLang(String file, Locale locale) {
        return loadLang(new File(file), locale);
    }

    /**
     * Loads a lang into this plugins lang store.
     * @param lang The lang to load
     * @return True if it was loaded successfully, false otherwise.
     */
    public boolean loadLang(Lang<M> lang) {
        switch (lang.getLoadStatus()) {
            case ERROR:
                Bukkit.getLogger().severe(lang.getLoadStatus().getMessage(lang.getFile().getName()));
            case WARNING:
                Bukkit.getLogger().warning(lang.getLoadStatus().getMessage(lang.getFile().getName()));
            default:
                Bukkit.getLogger().info(lang.getLoadStatus().getMessage(lang.getFile().getName()));
        }
        if (lang.getLoadStatus() == Lang.LoadStatus.ERROR) return false;
        if (hasLang(lang.getLocale())) return false;
        langMap.put(lang.getLocale(), lang);
        return true;
    }

    /**
     * Unloads a locale from this plugins lang store.
     * @param locale The locale to remove
     * @return True if it was removed successfully, false otherwise.
     */
    public boolean unloadLang(Locale locale) {
        if (!hasLang(locale)) return false;
        langMap.remove(locale);
        return true;
    }

    /**
     * Translates a key in the specified locale
     * @param key The key to translate
     * @param locale The locale to translate the key to
     * @return The translated key
     */
    public String translate(M key, Locale locale) {
        return getLang(locale).translate(key);
    }

    /**
     * Translates a key in the specified locale
     * @param key The key to translate
     * @param locale The locale to translate the key to
     * @param placeholders The placeholders to use in the key
     * @return The translated and formatted key
     */
    public String translate(M key, Locale locale, Object... placeholders) {
        return getLang(locale).translate(key, placeholders);
    }
}

