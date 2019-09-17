package com.njdaeger.bci.base;

import org.bukkit.command.CommandSender;

public class BCIException extends Exception {

    private boolean quiet;

    public BCIException(String message, boolean quiet) {
        super(message, null, true, false);
        this.quiet = quiet;
    }

    public BCIException(String message) {
        this(message, false);
    }

    public BCIException() {
        this(null, true);
    }

    /**
     * Shows the error message to the command sender whenever an error arises
     * @param sender The sender to send the message to
     */
    public void showError(CommandSender sender) {
        if (!quiet) sender.sendMessage(getMessage());
    }
    
}
