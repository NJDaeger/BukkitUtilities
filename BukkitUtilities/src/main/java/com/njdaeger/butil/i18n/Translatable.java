package com.njdaeger.butil.i18n;

/**
 * This indicates that this Enum is a Translatable Locale file, and that this should be used for translating purposes.
 * <p>The implementation of this interface is very specific and must be followed strictly.
 */
@SuppressWarnings("unused")
public interface Translatable {

    /**
     * <p>--==IMPLEMENTATION INSTRUCTIONS==--</p>
     * <p>This method needs to return the first value in the enum constructor. The so called "key" is the location of
     * the translatable message within a given configuration file.<p/>
     * <p>
     * Gets the configuration key related to this MessageKey
     *
     * @return The configuration key
     */
    String getKey();

    /**
     * <p>--==IMPLEMENTATION INSTRUCTIONS==--</p>
     * <p>This method should return the values following the key value in the constructor. The "holders" are the names
     * of variables within a given message which need to be replaced.<p/>
     * <p>
     * Gets all the placeholders for this MessageKey
     *
     * @return The placeholders of this message
     */
    String[] getHolders();

    /**
     * This formats the key value to a usable state for proper placeholder parsing.
     *
     * @param string The key value
     * @return The formatted key
     */
    default String format(String string) {
        String formatted = string;
        if (getHolders() != null) {
            for (int i = 0; getHolders().length > i; i++) {
                formatted = formatted.replace(getHolders()[i], "{" + i + "}");
            }
        }
        return formatted;
    }

}
