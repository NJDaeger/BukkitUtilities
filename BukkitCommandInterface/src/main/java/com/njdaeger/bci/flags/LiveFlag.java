package com.njdaeger.bci.flags;

import com.njdaeger.bci.base.BCIException;

public final class LiveFlag {

    private final AbstractFlag<?> flag;
    private final String separatedFlag;

    public LiveFlag(AbstractFlag<?> flag, String separatedFlag) {
        this.separatedFlag = separatedFlag;
        this.flag = flag;

    }

    /**
     * Gets the flag represented by this LiveFlag
     *
     * @return The flag represented by this LiveFlag
     */
    public AbstractFlag<?> getFlag() {
        return flag;
    }

    /**
     * Gets the flag represented by this LiveFlag
     *
     * @param flagType The class type of this flag.
     * @param <T> The type of flag this LiveFlag Represents
     * @return The Flag as the desired type.
     */
    public <T extends AbstractFlag> T getFlag(Class<T> flagType) {
        return (T) flag;
    }

    /**
     * Gets the raw value of this flag. If this flag is a split flag, this will return the value after the given
     * splitter. If this value is a standard flag with a following value, it returns the following value. In all other
     * conditions, it returns {@link AbstractFlag#getRawFlag()}
     *
     * @return The value of this flag.
     */
    public String getRawValue() {
        if (flag.isSplitFlag()) {
            return separatedFlag.split(String.valueOf(flag.getSplitter().charValue()))[1];
        } else if (flag.hasFollowingValue()) return separatedFlag.split(" ")[1];
        else return flag.getRawFlag();
    }

    /**
     * This flag as a boolean
     * @return The boolean value of this flag
     * @throws BCIException If it was unable to be parsed to a boolean
     */
    public Boolean getBoolean() throws BCIException {
        return getAs(Boolean.class);
    }

    /**
     * This flag as an integer
     * @return The integer value of this flag
     * @throws BCIException If it was unable to be parsed as an integer
     */
    public Integer getInteger() throws BCIException {
        return getAs(Integer.class);
    }

    /**
     * This flag as a double
     * @return The double value of this flag
     * @throws BCIException If it was unable to be parsed as a double
     */
    public Double getDouble() throws BCIException {
        return getAs(Double.class);
    }

    /**
     * This flag as a float
     * @return The float value of this flag
     * @throws BCIException If it was unable to be parsed as a float
     */
    public Float getFloat() throws BCIException {
        return getAs(Float.class);
    }

    /**
     * The flag as a string
     * @return The string value of this flag
     * @throws BCIException If it was unable to be parsed as a string
     */
    public String getString() throws BCIException {
        return getAs(String.class);
    }

    public <T> T getAs(Class<T> type) throws BCIException {
        return (T) flag.getFlagType().parse(getRawValue());
    }

}
