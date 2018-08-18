package com.njdaeger.bci.arguments.defaults;

import com.njdaeger.bci.arguments.AbstractArgument;
import com.njdaeger.bci.exceptions.ArgumentParseException;

public final class BooleanArg extends AbstractArgument<Boolean> {
    
    public BooleanArg(String name) {
        super(name);
    }
    
    @Override
    public Boolean parse(String input) throws ArgumentParseException {
        if (input == null || (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false"))) throw new ArgumentParseException("Boolean argument unable to be parsed. Input: " + input);
        return input.equalsIgnoreCase("true");
    }
    
    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }
}
