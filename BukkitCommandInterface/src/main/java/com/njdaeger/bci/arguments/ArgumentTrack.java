package com.njdaeger.bci.arguments;

import com.njdaeger.bci.exceptions.ArgumentParseException;

import java.util.ArrayList;
import java.util.List;

public final class ArgumentTrack {

    private String name;
    private final String usage;
    private final String description;
    private final List<AbstractArgument<?>> arguments;
    
    public ArgumentTrack(String usage, String description) {
        this.usage = usage;
        this.description = description;
        this.arguments = new ArrayList<>();
    }
    
    public ArgumentTrack() {
        this(null, null);
    }
    
    public String getUsage() {
        return usage;
    }
    
    public String getDescription() {
        return description;
    }
    
    void parse(String[] args) throws ArgumentParseException {
        for (int i = 0; i < args.length; i++) {
            arguments.get(i).parse(args[i]);
        }
    }
    
}
