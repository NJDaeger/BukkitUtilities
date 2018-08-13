package com.njdaeger.bci.exceptions;

import org.bukkit.command.CommandSender;

public class BCIException extends RuntimeException {
    
    private String message;
    
    public BCIException(String message) {
        super("", null, true, false);
        this.message = message;
    }

    void setSender(CommandSender sender) {
        sender.sendMessage(message);
    }
    
}
