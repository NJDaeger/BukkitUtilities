package com.njdaeger.bci.arguments.parts;

import com.njdaeger.bci.arguments.AbstractArgument;
import com.njdaeger.bci.arguments.AbstractArgumentPart;

public class StaticArgumentPart extends AbstractArgumentPart {
    
    public StaticArgumentPart(boolean optional, AbstractArgument<?>... arguments) {
        super(optional, arguments);
    }
}
