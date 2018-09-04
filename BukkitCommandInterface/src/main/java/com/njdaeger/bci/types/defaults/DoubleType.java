package com.njdaeger.bci.types.defaults;

import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.types.ParsedType;

public final class DoubleType extends ParsedType<Double> {
    
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
