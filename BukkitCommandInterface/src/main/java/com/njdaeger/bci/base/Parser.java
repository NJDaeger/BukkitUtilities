package com.njdaeger.bci.base;

import com.njdaeger.bci.flags.AbstractFlag;
import com.njdaeger.bci.flags.LiveFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public final class Parser {
    
    private Parser() {}

    /**
     * Parses the flags from the command
     * @param command The command context
     */
    static void parseFlags(AbstractCommandContext<?, ?> command) {
        List<LiveFlag> currentFlags = new ArrayList<>();
        
        String argumentString = command.joinArgs();
        for (AbstractFlag<?> flag : command.getCommand().getFlags()) {
            Matcher matcher = flag.getPattern().matcher(argumentString);
            if (!matcher.find()) continue;
            argumentString = matcher.replaceFirst("");
            if (flag.hasFollowingValue()) currentFlags.add(new LiveFlag(flag, matcher.group()));
            else currentFlags.add(new LiveFlag(flag, matcher.group()));
        }
        command.setFlags(currentFlags);
        command.setArgs(argumentString.split(" "));
    }
    
}
