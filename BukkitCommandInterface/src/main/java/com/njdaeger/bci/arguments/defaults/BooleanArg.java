package com.njdaeger.bci.arguments.defaults;

import com.njdaeger.bci.arguments.Argument;
import com.njdaeger.bci.exceptions.ArgumentParseException;

public final class BooleanArg extends Argument<Boolean> {
    
    @Override
    public Boolean parse(String input) throws ArgumentParseException {
        if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false")) throw new ArgumentParseException("Boolean argument unable to be parsed. Input: " + input);
        return null;
    }
}
