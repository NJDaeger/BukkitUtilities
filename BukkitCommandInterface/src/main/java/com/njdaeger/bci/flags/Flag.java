package com.njdaeger.bci.flags;

import com.njdaeger.bci.types.ParsedType;

import java.util.regex.Pattern;

public abstract class Flag<A extends ParsedType<?>> {
    
    private final boolean followingValue;
    private final Character splitter;
    private final Pattern pattern;
    private final char flag;
    
    private Flag(char flagCharacter, Character splitter, boolean followingValue) {
        this.followingValue = followingValue;
        this.splitter = splitter;
        this.flag = flagCharacter;
        if (isSplitFlag()) this.pattern = Pattern.compile(getRawFlag() + "\\S+");
        else if (hasFollowingValue()) this.pattern = Pattern.compile("(" + getRawFlag() + ")\\s\\S+");
        else this.pattern = Pattern.compile(getRawFlag());
    }
    
    public Flag(char flagCharacter, Character splitter) {
        this(flagCharacter, splitter, false);
    }
    
    /**
     * Creates a flag which is formatted like this:
     *
     * <p>-[flagCharacter] [(OPTIONAL)value]
     * @param flagCharacter The character to proceed the dash.
     * @param followingValue If true, this flag will have a value following it.
     */
    public Flag(char flagCharacter, boolean followingValue) {
        this(flagCharacter, null, followingValue);
    }
    
    /**
     * Creates a flag which is formatted like this:
     *
     * <p>-[flagCharacter] [value]
     * @param flagCharacter The character to proceed the dash.
     */
    public Flag(char flagCharacter) {
        this(flagCharacter, true);
    }
    
    public boolean hasFollowingValue() {
        return followingValue;
    }
    
    public Character getSplitter() {
        return splitter;
    }
    
    public boolean isSplitFlag() {
        return splitter != null;
    }
    
    public char getFlagCharacter() {
        return flag;
    }
    
    public String getRawFlag() {
        if (isSplitFlag()) return String.valueOf(flag) + String.valueOf(splitter);
        else return "-" + String.valueOf(flag);
    }
    
    public Pattern getPattern() {
        return pattern;
    }
    
    protected abstract A getFlagType();
    
}
