package com.njdaeger.bci.arguments.parts;

import com.njdaeger.bci.arguments.AbstractArgument;
import com.njdaeger.bci.arguments.AbstractArgumentPart;

public class ComplexArgumentPart extends AbstractArgumentPart {
    
    public <A extends AbstractArgument> ComplexArgumentPart(boolean optional, Class<A> type, AbstractArgument<?>... arguments) {
        super(optional, arguments);
    }
}
