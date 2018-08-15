package com.njdaeger.bci.arguments;

public abstract class AbstractArgumentPart {
    
    private final boolean optional;
    private final AbstractArgument<?>[] arguments;
    
    public AbstractArgumentPart(boolean optional, AbstractArgument<?>... arguments) {
        this.arguments = arguments;
        this.optional = optional;
    }
    
    public boolean isOptional() {
        return optional;
    }
    
    public AbstractArgument<?>[] getArguments() {
        return arguments;
    }
}
