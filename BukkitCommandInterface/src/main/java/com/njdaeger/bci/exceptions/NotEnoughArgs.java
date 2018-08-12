package com.njdaeger.bci.exceptions;

import org.bukkit.command.CommandSender;

public class NotEnoughArgs extends BCIException {
    
    public NotEnoughArgs(CommandSender sender, String message) {
        super(sender, message);
    }
}
