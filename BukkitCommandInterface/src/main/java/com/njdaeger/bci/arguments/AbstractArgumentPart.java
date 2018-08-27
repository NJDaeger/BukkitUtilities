package com.njdaeger.bci.arguments;

import com.njdaeger.bci.types.ParsedType;

public abstract class AbstractArgumentPart {
    
    private final ParsedType<?>[] arguments;
    
    /**
     * Creates a new argument part with the arguments already inserted
     * @param arguments The arguments which make up this argument part.
     */
    public AbstractArgumentPart(ParsedType<?>... arguments) {
        this.arguments = arguments;
    }
    
    /**
     * Gets all the arguments which are part of this specific argument part.
     * @return An array of all the AbstractArguments in this ArgumentPart
     */
    public ParsedType<?>[] getArguments() {
        return arguments;
    }
}
