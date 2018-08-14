package com.njdaeger.bci.exceptions;

import com.njdaeger.bci.base.BCIException;
import org.bukkit.ChatColor;

public class PermissionDeniedException extends BCIException {
    
    public PermissionDeniedException() {
        this(ChatColor.RED + "You do not have sufficient permissions to run this command.");
    }
    
    public PermissionDeniedException(String message) {
        super(message);
    }
}
