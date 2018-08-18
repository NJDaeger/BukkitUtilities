package com.njdaeger.bci.arguments.parts;

import com.njdaeger.bci.arguments.AbstractArgument;
import com.njdaeger.bci.arguments.AbstractArgumentPart;

public class StaticArgumentPart extends AbstractArgumentPart {
    
    private StaticArgumentPart(AbstractArgument<?>... arguments) {
        super(arguments);
    }
    
    public StaticArgumentPart(AbstractArgument<?> argument) {
        super(argument);
    }
    
    public AbstractArgument getArgument() {
        return getArguments()[0];
    }
    
}
