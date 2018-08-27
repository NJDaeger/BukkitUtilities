package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.Flag;
import com.njdaeger.bci.types.defaults.DoubleType;

public final class DoubleFlag extends Flag<DoubleType> {
    
    public DoubleFlag(char flagCharacter, Character splitter) {
        super(flagCharacter, splitter);
    }
    
    public DoubleFlag(char flagCharacter) {
        super(flagCharacter);
    }
    
    @Override
    protected DoubleType getFlagType() {
        return new DoubleType();
    }
}
