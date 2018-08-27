package com.njdaeger.bci.types;

import com.njdaeger.bci.exceptions.ArgumentParseException;

public abstract class ParsedType<T> {
    
    private final String name;
    
    public ParsedType(String name) {
        this.name = name;
    }
    
    public ParsedType() {
        this(null);
    }
    
    /**
     * How to get to the desired output with a string input.
     * @param input The string input
     * @return The desired output object
     * @throws ArgumentParseException If the parsing failed.
     */
    public abstract T parse(String input) throws ArgumentParseException;
    
    /**
     * Gets the generic type class.
     * @return The generic type class.
     */
    public abstract Class<T> getType();
    
    /**
     * Gets the specified name of this Argument, or else the name of the Generic Type Class simple name.
     * @return The name of the argument
     */
    public String getName() {
        return name == null ? getType().getSimpleName() : name;
    }
    
}
