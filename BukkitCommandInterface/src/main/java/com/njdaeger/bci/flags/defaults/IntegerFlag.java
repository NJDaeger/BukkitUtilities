package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.Flag;
import com.njdaeger.bci.types.defaults.IntegerType;

public final class IntegerFlag extends Flag<IntegerType> {
    
    public IntegerFlag(char flagCharacter, Character splitter) {
        super(flagCharacter, splitter);
    }
    
    public IntegerFlag(char flagCharacter) {
        super(flagCharacter);
    }
    
    @Override
    protected IntegerType getFlagType() {
        return new IntegerType();
    }
}
