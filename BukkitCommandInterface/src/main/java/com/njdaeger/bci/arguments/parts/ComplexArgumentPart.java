package com.njdaeger.bci.arguments.parts;

import com.njdaeger.bci.arguments.AbstractArgumentPart;
import com.njdaeger.bci.types.ParsedType;

public class ComplexArgumentPart extends AbstractArgumentPart {
    
    private final Class<?> type;
    
    /**
     * Create a ComplexArgumentPart
     * @param type The argument type required before this argument type
     * @param arguments The arguments which are a part of this complex argument part.
     * @param <A> The AbstractArgument required before this part.
     */
    public <A extends ParsedType> ComplexArgumentPart(Class<A> type, ParsedType<?>... arguments) {
        super(arguments);
        this.type = type;
    }
    
    /**
     * Get the required previous argument for this Argument part
     * @return The required previous argument.
     */
    public Class<?> getRequiredPrevious() {
        return type;
    }
    
}
