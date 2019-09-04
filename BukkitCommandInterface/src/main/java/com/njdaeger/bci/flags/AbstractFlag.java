package com.njdaeger.bci.flags;

import com.njdaeger.bci.types.ParsedType;

import java.util.regex.Pattern;

public abstract class AbstractFlag<A extends ParsedType<?>> {

    private final boolean followingValue;
    private final Character splitter;
    private final Pattern pattern;
    private final String prefix;
    private final String flag;

    private AbstractFlag(String prefix, String flag, Character splitter, boolean followingValue) {
        this.followingValue = followingValue;
        this.splitter = splitter;
        //We always need some kind of prefix for a flag which isnt split
        this.prefix = !isSplitFlag() && prefix == null ? "-" : prefix;
        this.flag = flag;
        if (isSplitFlag()) this.pattern = Pattern.compile(getRawFlag() + "\\S+");
        else if (hasFollowingValue()) this.pattern = Pattern.compile("(" + getRawFlag() + ")\\s\\S+");
        else this.pattern = Pattern.compile(getRawFlag());
    }

    /**
     * Creates a splitter flag which is meant to split a flag at a specific character.
     *
     * @param flag The string required before the splitter
     * @param splitter The splitter character
     */
    public AbstractFlag(String flag, Character splitter) {
        this(null, flag, splitter, false);
    }

    /**
     * Creates a flag which is formatted like this:
     *
     * <p>[prefix][flag] [(OPTIONAL)value]
     *
     * @param flag The character to proceed the dash.
     * @param followingValue If true, this flag will have a value following it.
     */
    public AbstractFlag(String prefix, String flag, boolean followingValue) {
        this(prefix, flag, null, followingValue);
    }

    /**
     * Creates a flag which is formatted like this:
     *
     * <p>[prefix][flag] [value]
     *
     * @param flag The character to proceed the dash.
     */
    public AbstractFlag(String prefix, String flag) {
        this(prefix, flag, true);
    }

    /**
     * Whether this specific flag is supposed to have a following value or not.
     *
     * @return True if a following value is expected, false otherwise
     */
    public boolean hasFollowingValue() {
        return followingValue;
    }

    /**
     * Gets the splitter needed for this flag to be split
     *
     * @return The flag splitter, or null if this flag is not a split flag.
     */
    public Character getSplitter() {
        return splitter;
    }

    /**
     * Indicates whether this specific flag is a split flag or not.
     *
     * @return True if this flag is a split flag, false otherwise.
     */
    public boolean isSplitFlag() {
        return splitter != null;
    }

    /**
     * The String this flag uses to indicate a flag. This is the string which appears after a flag prefix / before a
     * flag splitter
     *
     * @return The String flag indicator
     */
    public String getFlagString() {
        return flag;
    }

    /**
     * Gets a raw string of this flag constructed.
     * @return The raw flag
     */
    public String getRawFlag() {
        if (isSplitFlag()) return flag + String.valueOf(splitter);
        else return prefix + flag;
    }

    /**
     * The pattern needed to pick out the flag.
     * @return The flag pattern matcher
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Gets the parsed type this flag returns.
     * @return The parsed type this flag returns
     */
    public abstract A getFlagType();

}
