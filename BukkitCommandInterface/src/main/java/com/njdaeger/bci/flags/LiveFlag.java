package com.njdaeger.bci.flags;

import com.njdaeger.bci.exceptions.ArgumentParseException;

public final class LiveFlag {
    
    private final Flag<?> flag;
    private final String separatedFlag;
    
    public LiveFlag(Flag<?> flag, String separatedFlag) {
        this.separatedFlag = separatedFlag;
        this.flag = flag;
        
    }
    
    public Flag<?> getFlag() {
        return flag;
    }
    
    public <T extends Flag> T getFlag(Class<T> flagType) {
        return (T)flag;
    }
    
    public String getRawValue() {
        if (flag.isSplitFlag()) {
            return separatedFlag.split(String.valueOf(flag.getSplitter()))[1];
        }
        else {
            return separatedFlag.split(" ")[1];
        }
    }
    
    public Boolean getBoolean() throws ArgumentParseException {
        return getAs(Boolean.class);
    }
    
    public Integer getInteger() throws ArgumentParseException {
        return getAs(Integer.class);
    }
    
    public Double getDouble() throws ArgumentParseException {
        return getAs(Double.class);
    }
    
    public Float getFloat() throws ArgumentParseException {
        return getAs(Float.class);
    }
    
    public String getString() throws ArgumentParseException {
        return String.valueOf(flag.getFlagType().parse(getRawValue()));
    }
    
    public <T> T getAs(Class<T> type) throws ArgumentParseException {
        return (T)flag.getFlagType().parse(getRawValue());
    }
    
}
