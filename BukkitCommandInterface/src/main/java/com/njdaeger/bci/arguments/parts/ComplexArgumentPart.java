package com.njdaeger.bci.arguments.parts;

import com.njdaeger.bci.arguments.AbstractArgument;
import com.njdaeger.bci.arguments.AbstractArgumentPart;

public class ComplexArgumentPart extends AbstractArgumentPart {
    
    private final Class<?> type;
    
    public <A extends AbstractArgument> ComplexArgumentPart(Class<A> type, AbstractArgument<?>... arguments) {
        super(arguments);
        this.type = type;
    }
    
    public Class<?> getRequiredPrevious() {
        return type;
    }
    
}
