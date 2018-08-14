package com.njdaeger.bci.base;

import org.bukkit.command.CommandSender;

public class BCIException extends Exception {
    
    private String message;
    
    public BCIException(String message) {
        super("", null, true, false);
        this.message = message;
    }

    void showError(CommandSender sender) {
        sender.sendMessage(message);
    }
    
}
