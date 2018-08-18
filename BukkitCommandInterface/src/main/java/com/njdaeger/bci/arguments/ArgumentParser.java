package com.njdaeger.bci.arguments;

import com.njdaeger.bci.base.AbstractCommandContext;
import com.njdaeger.bci.exceptions.ArgumentParseException;
import org.bukkit.ChatColor;

//This class should be used to determine which argument track was used in the command. If none was found throw an argument parse exception
public final class ArgumentParser {
    
    private final AbstractCommandContext<?, ?> command;
    private final String[] args;
    
    public ArgumentParser(AbstractCommandContext<?, ?> command) {
        this.command = command;
        this.args = command.getArgs().toArray(new String[0]);
    }
    
    //this may not make sense rn, but when flags are added in this method will get more complex in order to ignore flags provided to get straight arguments from the command.
    public ArgumentTrack parse() throws ArgumentParseException {
        for (ArgumentTrack track : command.getArgumentMap()) {
            try {
                track.parse(args);
            }
            catch (ArgumentParseException ignore) {
                continue;
            }
            return track;
        }
        throw new ArgumentParseException(ChatColor.RED + "Could not find argument track for given arguments.");
    }
    
}
