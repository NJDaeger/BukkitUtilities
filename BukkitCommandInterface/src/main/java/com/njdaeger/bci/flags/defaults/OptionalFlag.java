package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.Flag;
import com.njdaeger.bci.types.defaults.BooleanType;

public final class OptionalFlag extends Flag<BooleanType> {
    
    public OptionalFlag(char flagCharacter) {
        super(flagCharacter, false);
    }
    
    @Override
    protected BooleanType getFlagType() {
        return new BooleanType();
    }
    
}
