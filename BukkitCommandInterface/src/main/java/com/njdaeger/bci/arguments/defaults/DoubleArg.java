package com.njdaeger.bci.arguments.defaults;

import com.njdaeger.bci.arguments.AbstractArgument;
import com.njdaeger.bci.exceptions.ArgumentParseException;

public final class DoubleArg extends AbstractArgument<Double> {
    
    public DoubleArg(String name) {
        super(name);
    }
    
    @Override
    public Double parse(String input) throws ArgumentParseException {
        double parsed;
        try {
            parsed = Double.parseDouble(input);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException("Double argument unable to be parsed. Input: " + input);
        }
        return parsed;
    }
    
    @Override
    public Class<Double> getType() {
        return Double.class;
    }
}
