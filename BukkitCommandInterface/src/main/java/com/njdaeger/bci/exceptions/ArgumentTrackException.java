package com.njdaeger.bci.exceptions;

import com.njdaeger.bci.base.BCIException;
import org.bukkit.ChatColor;

public class ArgumentTrackException extends BCIException {
    
    public ArgumentTrackException(String message) {
        super(message);
    }
    
    public ArgumentTrackException() {
        this(ChatColor.RED + "Complex Argument Parts cannot exist when the index is 0.");
    }
    
}
