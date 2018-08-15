package com.njdaeger.bci.base;

import org.bukkit.command.CommandSender;

public class BCIException extends Exception {
    
    public BCIException(String message) {
        super(message, null, true, false);
    }

    public void showError(CommandSender sender) {
        sender.sendMessage(getMessage());
    }
    
}
