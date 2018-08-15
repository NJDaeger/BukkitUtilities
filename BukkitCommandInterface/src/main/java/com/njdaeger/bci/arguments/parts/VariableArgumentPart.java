package com.njdaeger.bci.arguments.parts;

import com.njdaeger.bci.arguments.AbstractArgument;
import com.njdaeger.bci.arguments.AbstractArgumentPart;

public class VariableArgumentPart extends AbstractArgumentPart {
    
    public VariableArgumentPart(boolean optional, AbstractArgument<?>... arguments) {
        super(optional, arguments);
    }
}
