package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.Flag;
import com.njdaeger.bci.types.defaults.FloatType;

public final class FloatFlag extends Flag<FloatType> {
    
    public FloatFlag(char flagCharacter, Character splitter) {
        super(flagCharacter, splitter);
    }
    
    public FloatFlag(char flagCharacter) {
        super(flagCharacter);
    }
    
    @Override
    protected FloatType getFlagType() {
        return new FloatType();
    }
}
