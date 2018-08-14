package com.njdaeger.bci.exceptions;

import com.njdaeger.bci.base.BCIException;
import org.bukkit.ChatColor;

public class NotEnoughArgsException extends BCIException {
    
    public NotEnoughArgsException() {
        this(ChatColor.RED + "You have not provided enough arguments to run this command.");
    }
    
    public NotEnoughArgsException(String message) {
        super(message);
    }
}
