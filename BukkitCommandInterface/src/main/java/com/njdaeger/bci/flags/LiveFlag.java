package com.njdaeger.bci.flags;

import com.njdaeger.bci.base.BCIException;

public final class LiveFlag {
    
    private final AbstractFlag<?> flag;
    private final String separatedFlag;
    
    public LiveFlag(AbstractFlag<?> flag, String separatedFlag) {
        this.separatedFlag = separatedFlag;
        this.flag = flag;
        
    }
    
    public AbstractFlag<?> getFlag() {
        return flag;
    }
    
    public <T extends AbstractFlag> T getFlag(Class<T> flagType) {
        return (T)flag;
    }
    
    public String getRawValue() {
        if (flag.isSplitFlag()) {
            return separatedFlag.split(String.valueOf(flag.getSplitter()))[1];
        }
        else if (flag.hasFollowingValue()) return separatedFlag.split(" ")[1];
        else return flag.getRawFlag();
    }
    
    public Boolean getBoolean() throws BCIException {
        return getAs(Boolean.class);
    }
    
    public Integer getInteger() throws BCIException {
        return getAs(Integer.class);
    }
    
    public Double getDouble() throws BCIException {
        return getAs(Double.class);
    }
    
    public Float getFloat() throws BCIException {
        return getAs(Float.class);
    }
    
    public String getString() throws BCIException {
        return String.valueOf(flag.getFlagType().parse(getRawValue()));
    }
    
    public <T> T getAs(Class<T> type) throws BCIException {
        return (T)flag.getFlagType().parse(getRawValue());
    }
    
}
