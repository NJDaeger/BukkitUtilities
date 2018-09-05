package com.njdaeger.bci.types;

import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.exceptions.ArgumentParseException;

public abstract class ParsedType<T> {
    
    public ParsedType() {
    
    }
    
    /**
     * How to get to the desired output with a string input.
     * @param input The string input
     * @return The desired output object
     * @throws ArgumentParseException If the parsing failed.
     */
    public abstract T parse(String input) throws BCIException;
    
    /**
     * Gets the generic type class.
     * @return The generic type class.
     */
    public abstract Class<T> getType();
    
}
