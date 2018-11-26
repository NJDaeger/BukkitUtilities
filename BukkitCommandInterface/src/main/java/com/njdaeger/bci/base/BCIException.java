package com.njdaeger.bci.base;

import org.bukkit.command.CommandSender;

public class BCIException extends Exception {
    
    public BCIException(String message) {
        super(message, null, true, false);
    }

    /**
     * Shows the error message to the command sender whenever an error arises
     * @param sender The sender to send the message to
     */
    public void showError(CommandSender sender) {
        sender.sendMessage(getMessage());
    }
    
}
