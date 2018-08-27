package com.njdaeger.bci.base;

import com.njdaeger.bci.arguments.ArgumentMap;
import com.njdaeger.bci.arguments.ArgumentTrack;
import com.njdaeger.bci.arguments.LiveTrack;
import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.flags.Flag;
import com.njdaeger.bci.flags.LiveFlag;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public final class Parser {
    
    private Parser() {}
    
    static LiveTrack parseTrack(List<String> args, ArgumentMap<? ,?> argumentMap) throws ArgumentParseException {
        for (ArgumentTrack track : argumentMap) {
            try {
                track.parse(args.toArray(new String[0]));
            }
            catch (ArgumentParseException ignore) {
                continue;
            }
            return new LiveTrack(track, args.toArray(new String[0]));
        }
        throw new ArgumentParseException(ChatColor.RED + "Could not find argument track for given arguments.");
    }
    
    static List<String> parseFlags(AbstractCommandContext<?, ?> command) {
        List<LiveFlag> currentFlags = new ArrayList<>();
        
        String argumentString = command.joinArgs();
        for (Flag<?> flag : command.getCommand().getFlags()) {
            Matcher matcher = flag.getPattern().matcher(argumentString);
            if (!matcher.find()) continue;
            argumentString = matcher.replaceFirst("");
            if (flag.hasFollowingValue()) currentFlags.add(new LiveFlag(flag, matcher.group()));
            else currentFlags.add(new LiveFlag(flag, flag.getRawFlag()));
        }
        command.setFlags(currentFlags);
        return new ArrayList<>(Arrays.asList(argumentString.split(" ")));
    }
    
}
