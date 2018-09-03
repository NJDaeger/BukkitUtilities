package com.njdaeger.bcm.base;

import com.njdaeger.bcm.common.Section;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ISection {
    
    /**
     * Gets a set of keys that are in this section.
     *
     * @param deep Whether to get the keys of the keys (and so on) in this section.
     * @return A Set of Keys.
     */
    default Set<String> getKeys(boolean deep) {
        
        Set<String> keys = getConfig().getKeys(true).stream().filter(k -> k.startsWith(getCurrentPath()) && k.contains(".")).collect(Collectors.toSet());
        if (deep) {
            return keys;
        } else {
            return keys.stream().map(k -> k.replaceFirst(getCurrentPath() + "\\.", "")).map(k -> k.substring(0, (!k.contains(".") ? k.length() : k.indexOf(".")))).collect(Collectors.toSet());
        }
    }
    
    /**
     * Gets a section from within this section.
     *
     * @param path The path from the start of this section. <p>Note: dont use the string provided originally to get this current section</p>
     * @return A section of the configuration.
     */
    default ISection getSection(String path) {
        String base = getCurrentPath().equals("") ? getCurrentPath() : getCurrentPath() + ".";
        if (!isSection(path)) {
            return null;
        }
        else return new Section(base + path, getConfig());
    }
    
    /**
     * Gets a list of sections from a section specified. Section specified must be contained within the current section
     *
     * @param name The path to get a list of sections from.
     * @return A list of sections from a config section
     */
    default List<ISection> getSections(final String name) {
        if (!hasSection(name)) return null;
        ISection s = getSection(name);
        return s.getKeys(false).stream().filter(s::hasSection).map(s::getSection).collect(Collectors.toList());
    }
    
    /**
     * Gets a list of sections currently in this section
     *
     * @return A list of sections from a config section
     */
    default List<ISection> getSections() {
        return getKeys(false).stream().filter(this::hasSection).map(this::getSection).collect(Collectors.toList());
    }
    
    /**
     * Checks if this section contains the given section
     * @param name The section to look for
     * @return True if the section exists, false otherwise.
     */
    default boolean hasSection(String name) {
        return getSection(name) != null;
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A string from the specified path.
     */
    default String getString(String path) {
        return getValueAs(path, String.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A double from the specified path.
     */
    default double getDouble(String path) {
        return getValueAs(path, Double.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return An integer from the specified path.
     */
    default int getInt(String path) {
        return getValueAs(path, Integer.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A long from the specified path.
     */
    default long getLong(String path) {
        return getValueAs(path, Long.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A boolean from the specified path.
     */
    default boolean getBoolean(String path) {
        return getValueAs(path, Boolean.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A list from the specified path.
     */
    default List<?> getList(String path) {
        Object v = getValue(path);
        return v instanceof List ? (List<?>)v : null;
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A flat from the specified path.
     */
    default float getFloat(String path) {
        return getValueAs(path, Float.class);
    }
    
    /**
     * Gets a specific type of list.
     *
     * @param path The path to the list
     * @param type The type of list to return
     * @param <E>  The list type
     * @return The list
     */
    @SuppressWarnings( {"unchecked", "unused"} )
    default <E> List<E> getList(String path, Class<E> type) {
        List<?> list = getList(path);
        
        if (list == null) {
            return new ArrayList<>();
        }
        
        List<E> r = new ArrayList<>();
        
        for (Object o : list) {
            if (type.isInstance(o)) r.add((E)o);
        }
        
        return r;
    }
    
    /**
     * Gets a List of Strings from a path in this config.
     *
     * @param path The path to get the strings from.
     * @return A list from the specified path.
     */
    default List<String> getStringList(String path) {
        return getList(path, String.class);
    }
    
    /**
     * Gets all the keys which have the matching value
     * @param value The value needing to be matched
     * @return The entries found in this search
     */
    default Collection<String> getKeysFromValue(Object value) {
        return getKeys(true).stream().filter(k -> getValue(k).equals(value)).collect(Collectors.toList());
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration relative to where you currently are in the configuration.
     * @return An object from the specified path.
     */
    default Object getValue(String path) {
        String base = getCurrentPath().equals("") ? getCurrentPath() : getCurrentPath() + ".";
        return getConfig().getValue(base + path);
    }
    
    /**
     * Check whether a section contains a path.
     *
     * @param path The path to look for
     * @return True if the path exists, false otherwise.
     */
    default boolean contains(String path) {
        return getKeys(false).contains(path);
    }
    
    /**
     * The name of the section.
     *
     * @return The name of the section.
     */
    default String getName() {
        return getCurrentPath().substring(!getCurrentPath().contains(".") ? 0 : getCurrentPath().lastIndexOf(".") + 1);
    }
    
    /**
     * Get the entry provided as a certain type.
     * @param path Path to the entry
     * @param type The type class wanting to be returned
     * @param <T> The return type
     * @return Null if the entry doesn't exist, the entry as the provided type otherwise.
     */
    @SuppressWarnings( "unchecked" )
    default <T> T getValueAs(String path, Class<T> type) {
        if (getValue(path) == null) return null;
        
        if (type.equals(Long.class)) {
            return (T)Long.valueOf(getValue(path).toString());
        }
        if (type.equals(Byte.class)) {
            return (T)Byte.valueOf(getValue(path).toString());
        }
        if (type.equals(Float.class)) {
            return (T)Float.valueOf(getValue(path).toString());
        }
        if (type.equals(Short.class)) {
            return (T)Short.valueOf(getValue(path).toString());
        }
        if (type.equals(Double.class)) {
            return (T)Double.valueOf(getValue(path).toString());
        }
        if (type.equals(Integer.class)) {
            return (T)Integer.valueOf(getValue(path).toString());
        }
        if (type.isEnum()) {
            try {
                return (T)Enum.valueOf(type.asSubclass(Enum.class), getValue(path).toString().toUpperCase());
            }
            catch (IllegalArgumentException | NullPointerException e) {
                return null;
            }
        }
        return ((T)getValue(path));
    }
    
    /**
     * Checks if this configuration contains a specified path
     *
     * @param path  The path to look for
     * @param exact Whether the path to look for needs to be an entry or if it just needs to exist.
     * @return True if the path exists.
     */
    default boolean contains(String path, boolean exact) {
        if (exact) {
            return getValue(path) != null;
        } else {
            for (String entry : getKeys(true)) {
                if (entry.startsWith(path)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Gets the parent section to the current section.
     *
     * @return The previous section, returns null if no previous section exists.
     */
    default ISection getParent() {
        if (!getCurrentPath().contains(".")) {
            return null;
        } else {
            return new Section(getCurrentPath().substring(0, getCurrentPath().lastIndexOf(".")), getConfig());
        }
    }
    
    
    boolean isSection(String path);
    
    ConfigType getType();
    
    /**
     * Gets the host plugin.
     *
     * @return The host plugin.
     */
    Plugin getPlugin();
    
    /**
     * Adds a new entry to the current config.
     *
     * @param path  The path in the config.
     * @param value The value to set this entry to.
     */
    void addEntry(String path, Object value);
    
    /**
     * Adds a new entry to the current config. If the
     * config already has a value at the path location
     * it will be updated with the new value supplied
     * from this method.
     *
     * @param path  The path in the config.
     * @param value The value to set the path to.
     */
    void setEntry(String path, Object value);
    
    /**
     * Gets the current path of the configuration section.
     *
     * @return The current path
     */
    String getCurrentPath();
    
    /**
     * Gets the base configuration of this section.
     *
     * @return The current configuration
     */
    IConfig getConfig();
    
}
