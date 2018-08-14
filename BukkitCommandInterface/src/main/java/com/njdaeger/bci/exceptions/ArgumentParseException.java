package com.njdaeger.bci.exceptions;

import com.njdaeger.bci.arguments.Argument;

public class ArgumentParseException extends Exception {
    
    public ArgumentParseException(String message) {
        super(message);
    }
    
    public ArgumentParseException() {
        this("Could not parse argument.");
    }
    
}
