package com.njdaeger.bci.arguments.parts;

import com.njdaeger.bci.arguments.AbstractArgumentPart;
import com.njdaeger.bci.types.ParsedType;

public class VariableArgumentPart extends AbstractArgumentPart {
    
    /**
     * Create a new VariableArgumentPart
     * @param arguments The arguments to represent
     */
    public VariableArgumentPart(ParsedType<?>... arguments) {
        super(arguments);
    }
    
}
