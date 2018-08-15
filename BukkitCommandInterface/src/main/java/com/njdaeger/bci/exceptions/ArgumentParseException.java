package com.njdaeger.bci.exceptions;

import com.njdaeger.bci.base.BCIException;
import org.bukkit.ChatColor;

public class ArgumentParseException extends BCIException {
    
    public ArgumentParseException(String message) {
        super(message);
    }
    
    public ArgumentParseException() {
        this(ChatColor.RED + "Could not parse argument.");
    }
    
}
