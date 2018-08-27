package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.Flag;
import com.njdaeger.bci.types.defaults.BooleanType;

public final class BooleanFlag extends Flag<BooleanType> {
    
    public BooleanFlag(char flagCharacter, Character splitter) {
        super(flagCharacter, splitter);
    }
    
    public BooleanFlag(char flagCharacter) {
        super(flagCharacter);
    }
    
    @Override
    protected BooleanType getFlagType() {
        return new BooleanType();
    }
}
