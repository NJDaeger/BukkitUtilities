package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.AbstractFlag;
import com.njdaeger.bci.types.defaults.BooleanType;

public final class OptionalFlag extends AbstractFlag<BooleanType> {

    public OptionalFlag(String prefix, String flag) {
        super(prefix, flag, false);
    }

    public OptionalFlag(String flag) {
        super("-", flag, false);
    }
    
    @Override
    protected BooleanType getFlagType() {
        return new BooleanType();
    }
    
}
