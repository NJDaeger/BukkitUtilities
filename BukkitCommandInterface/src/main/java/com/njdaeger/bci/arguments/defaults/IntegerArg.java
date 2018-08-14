package com.njdaeger.bci.arguments.defaults;

import com.njdaeger.bci.arguments.Argument;
import com.njdaeger.bci.exceptions.ArgumentParseException;

public final class IntegerArg extends Argument<Integer> {
    
    @Override
    public Integer parse(String input) throws ArgumentParseException {
        int parsed;
        try {
            parsed = Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException("Integer argument unable to be parsed. Input: " + input);
        }
        return parsed;
    }
}
