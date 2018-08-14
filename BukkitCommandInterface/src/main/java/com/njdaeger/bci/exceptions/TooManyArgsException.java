package com.njdaeger.bci.exceptions;

import com.njdaeger.bci.base.BCIException;
import org.bukkit.ChatColor;

public class TooManyArgsException extends BCIException {
    
    public TooManyArgsException() {
        this(ChatColor.RED + "You have provided too many arguments to run this command.");
    }
    
    public TooManyArgsException(String message) {
        super(message);
    }
}
