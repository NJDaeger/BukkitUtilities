package com.njdaeger.bci.arguments;

import com.njdaeger.bci.exceptions.ArgumentParseException;

public abstract class AbstractArgument<T> {

    private final String name;
    
    public AbstractArgument(String name) {
        this.name = name;
    }
    
    public AbstractArgument() {
        this(null);
    }
    
    public abstract T parse(String input) throws ArgumentParseException;
    
    public abstract Class<T> getType();
    
    public String getName() {
        return name == null ? getType().getSimpleName() : name;
    }
    
}
