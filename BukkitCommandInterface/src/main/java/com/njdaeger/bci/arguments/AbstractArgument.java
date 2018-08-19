package com.njdaeger.bci.arguments;

import com.njdaeger.bci.exceptions.ArgumentParseException;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbstractArgument<T> {

    private final String name;
    
    /**
     * Creates a new argument of this type.
     * @param name The name of the argument.
     */
    public AbstractArgument(String name) {
        this.name = name;
    }
    
    /**
     * Creates a new argument of this type. This argument will be named via its Generic Type Class simple name.
     */
    public AbstractArgument() {
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
