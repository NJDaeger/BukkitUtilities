package com.njdaeger.bci.types;

import com.njdaeger.bci.exceptions.ArgumentParseException;

public abstract class ParsedType<T> {
    
    private final String name;
    private final boolean optional;
    
    public ParsedType(String name, boolean optional) {
        this.name = name;
        this.optional = optional;
    }
    
    public ParsedType(String name) {
        this(name, true);
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
    
    /**
     * Whether this argument was optional or not.
     * @return Whether this argument is optional
     */
    public boolean isOptional() {
        return optional;
    }
    
}
