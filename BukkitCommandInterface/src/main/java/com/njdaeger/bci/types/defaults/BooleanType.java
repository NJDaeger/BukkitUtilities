package com.njdaeger.bci.types.defaults;

import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.types.ParsedType;

public final class BooleanType extends ParsedType<Boolean> {
    
    public BooleanType() {
        this(null);
    }
    
    public BooleanType(String name) {
        this(name, false);
    }
    
    public BooleanType(String name, boolean optional) {
        super(name, optional);
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
