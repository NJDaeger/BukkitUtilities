package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.AbstractFlag;
import com.njdaeger.bci.types.defaults.StringType;

public final class StringFlag extends AbstractFlag<StringType> {
    
    public StringFlag(String flag, Character splitter) {
        super(flag, splitter);
    }

    public StringFlag(String prefix, String flag) {
        super(prefix, flag);
    }

    public StringFlag(String flag) {
        super("-", flag);
    }
    
    @Override
    protected StringType getFlagType() {
        return new StringType();
    }
}
