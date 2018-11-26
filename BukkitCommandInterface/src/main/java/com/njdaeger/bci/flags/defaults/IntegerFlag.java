package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.AbstractFlag;
import com.njdaeger.bci.types.defaults.IntegerType;

public final class IntegerFlag extends AbstractFlag<IntegerType> {
    
    public IntegerFlag(String flag, Character splitter) {
        super(flag, splitter);
    }

    public IntegerFlag(String prefix, String flag) {
        super(prefix, flag);
    }

    public IntegerFlag(String flag) {
        super("-", flag);
    }
    
    @Override
    protected IntegerType getFlagType() {
        return new IntegerType();
    }
}
