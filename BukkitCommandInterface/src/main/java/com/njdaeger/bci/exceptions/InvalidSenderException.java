package com.njdaeger.bci.exceptions;

import com.njdaeger.bci.base.BCIException;
import org.bukkit.ChatColor;

public class InvalidSenderException extends BCIException {
    
    public InvalidSenderException() {
        this(ChatColor.RED + "You are not the correct sender type to run this command.");
    }
    
    public InvalidSenderException(String message) {
        super(message);
    }
}
