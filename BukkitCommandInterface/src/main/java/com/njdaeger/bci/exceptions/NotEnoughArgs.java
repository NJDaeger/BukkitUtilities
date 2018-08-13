package com.njdaeger.bci.exceptions;

import org.bukkit.command.CommandSender;

public class NotEnoughArgs extends BCIException {
    
    public NotEnoughArgs(String message) {
        super(message);
    }
}
