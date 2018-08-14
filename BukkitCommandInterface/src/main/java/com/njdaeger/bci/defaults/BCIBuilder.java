package com.njdaeger.bci.defaults;

import com.njdaeger.bci.base.AbstractCommandBuilder;

public class BCIBuilder extends AbstractCommandBuilder<CommandContext, TabContext, BCIBuilder> {
    
    public BCIBuilder(String name) {
        super(name);
    }
    
    public static BCIBuilder create(String name) {
        return new BCIBuilder(name);
    }
    
}
