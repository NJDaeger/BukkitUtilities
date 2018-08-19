package com.njdaeger.bci.arguments;

import com.njdaeger.bci.base.AbstractCommandContext;
import com.njdaeger.bci.exceptions.ArgumentParseException;
import org.bukkit.ChatColor;

public final class ArgumentParser {
    
    private ArgumentParser() {}
    
    
    /**
     * Parse the given command context to check for ArgumentTracks
     * @param command The command context to parse
     * @return The ArgumentTrack corresponding to the given arguments.
     * @throws ArgumentParseException If no ArgumentTrack was found
     */
    public ArgumentTrack parse(AbstractCommandContext<?, ?> command) throws ArgumentParseException {
        for (ArgumentTrack track : command.getArgumentMap()) {
            try {
                track.parse(command.getArgs().toArray(new String[0]));
            }
            catch (ArgumentParseException ignore) {
                continue;
            }
            return track;
        }
        throw new ArgumentParseException(ChatColor.RED + "Could not find argument track for given arguments.");
    }
    
}
