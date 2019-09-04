package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.AbstractFlag;
import com.njdaeger.bci.types.defaults.FloatType;

public final class FloatFlag extends AbstractFlag<FloatType> {
    
    public FloatFlag(String flag, Character splitter) {
        super(flag, splitter);
    }

    public FloatFlag(String prefix, String flag) {
        super(prefix, flag);
    }

    public FloatFlag(String flag) {
        super("-", flag);
    }
    
    @Override
    public FloatType getFlagType() {
        return new FloatType();
    }
}
