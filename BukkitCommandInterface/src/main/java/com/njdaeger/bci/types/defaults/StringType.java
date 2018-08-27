package com.njdaeger.bci.types.defaults;

import com.njdaeger.bci.types.ParsedType;

public final class StringType extends ParsedType<String> {
    
    public StringType() {
        super();
    }
    
    public StringType(String name) {
        super(name);
    }
    
    @Override
    public String parse(String input) {
        return input;
    }
    
    @Override
    public Class<String> getType() {
        return String.class;
    }
}
