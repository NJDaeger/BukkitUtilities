package com.njdaeger.bci.types.defaults;

import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.types.ParsedType;

public final class FloatType extends ParsedType<Float> {
    
    @Override
    public Float parse(String input) throws ArgumentParseException {
        float parsed;
        try {
            parsed = Float.valueOf(input);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException("Float argument unable to be parsed. Input: " + input);
        }
        return parsed;
    }
    
    @Override
    public Class<Float> getType() {
        return Float.class;
    }
}
