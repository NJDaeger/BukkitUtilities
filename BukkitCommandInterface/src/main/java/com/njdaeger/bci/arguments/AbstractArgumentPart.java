package com.njdaeger.bci.arguments;

public abstract class AbstractArgumentPart {
    
    private final AbstractArgument<?>[] arguments;
    
    public AbstractArgumentPart(AbstractArgument<?>... arguments) {
        this.arguments = arguments;
    }
    
    public AbstractArgument<?>[] getArguments() {
        return arguments;
    }
}
