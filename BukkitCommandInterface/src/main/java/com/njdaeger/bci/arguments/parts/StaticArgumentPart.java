package com.njdaeger.bci.arguments.parts;

import com.njdaeger.bci.arguments.AbstractArgumentPart;
import com.njdaeger.bci.types.ParsedType;

public class StaticArgumentPart extends AbstractArgumentPart {
    
    private StaticArgumentPart(ParsedType<?>... arguments) {
        super(arguments);
    }
    
    /**
     * Create a new StaticArgumentPart
     * @param argument The argument to represent
     */
    public StaticArgumentPart(ParsedType<?> argument) {
        super(argument);
    }
    
    /**
     * Get the argument represented by this static argument part.
     * @return The AbstractArgument represented by this part.
     */
    public ParsedType<?> getArgument() {
        return getArguments()[0];
    }
    
}
