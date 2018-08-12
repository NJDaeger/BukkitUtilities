package com.njdaeger.bci.exceptions;

import org.bukkit.command.CommandSender;

public class BCIException extends RuntimeException {
    
    public BCIException(CommandSender sender, String message) {
        super("", null, true, false);
        sender.sendMessage(message);
    }
    
}
