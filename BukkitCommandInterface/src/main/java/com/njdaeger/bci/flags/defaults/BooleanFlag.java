package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.AbstractFlag;
import com.njdaeger.bci.types.defaults.BooleanType;

public final class BooleanFlag extends AbstractFlag<BooleanType> {

    public BooleanFlag(String flag, Character splitter) {
        super(flag, splitter);
    }

    public BooleanFlag(String prefix, String flag) {
        super(prefix, flag);
    }

    public BooleanFlag(String flag) {
        super("-", flag);
    }
    
    @Override
    public BooleanType getFlagType() {
        return new BooleanType();
    }
}
