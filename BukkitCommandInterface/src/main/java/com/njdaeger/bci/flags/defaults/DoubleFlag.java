package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.AbstractFlag;
import com.njdaeger.bci.types.defaults.DoubleType;

public final class DoubleFlag extends AbstractFlag<DoubleType> {
    
    public DoubleFlag(String flag, Character splitter) {
        super(flag, splitter);
    }

    public DoubleFlag(String prefix, String flag) {
        super(prefix, flag);
    }

    public DoubleFlag(String flag) {
        super("-", flag);
    }
    
    @Override
    public DoubleType getFlagType() {
        return new DoubleType();
    }
}
