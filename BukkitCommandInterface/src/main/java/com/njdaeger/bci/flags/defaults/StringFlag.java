package com.njdaeger.bci.flags.defaults;

import com.njdaeger.bci.flags.Flag;
import com.njdaeger.bci.types.defaults.StringType;

public final class StringFlag extends Flag<StringType> {
    
    public StringFlag(char flagCharacter, Character splitter) {
        super(flagCharacter, splitter);
    }
    
    public StringFlag(char flagCharacter) {
        super(flagCharacter);
    }
    
    @Override
    protected StringType getFlagType() {
        return new StringType();
    }
}
