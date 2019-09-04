package com.njdaeger.bci.base;

import com.njdaeger.bci.flags.AbstractFlag;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public final class Parser {
    
    private Parser() {}

    /**
     * Parses the flags from the command
     * @param command The command context
     */
    static void parseFlags(AbstractCommandContext<?, ?> command) throws BCIException {
        Map<Class<? extends AbstractFlag<?>>, String> flagToString = new HashMap<>();
        Map<String, Object> stringToObject = new HashMap<>();

        String argumentString = command.joinArgs();
        for (AbstractFlag<?> flag : command.getCommand().getFlags()) {
            Matcher matcher = flag.getPattern().matcher(argumentString);
            if (!matcher.find()) continue;
            argumentString = matcher.replaceFirst("");
            flagToString.put((Class<? extends AbstractFlag<?>>) flag.getClass(), flag.getFlagString());
            stringToObject.put(flag.getFlagString(), flag.getFlagType().parse(getRawValue(flag, matcher.group())));
        }
        System.out.println(Arrays.toString(command.args));
        System.out.println(Arrays.toString(Stream.of(argumentString.trim().split(" ")).map(" "::concat).toArray(String[]::new)));
        command.setFlags(flagToString, stringToObject);
        command.setArgs(Stream.of(argumentString.trim().split(" ")).map(" "::concat).toArray(String[]::new));
    }

    private static String getRawValue(AbstractFlag<?> flag, String entireFlag) {
        if (flag.isSplitFlag()) {
            String[] split = entireFlag.split(String.valueOf(flag.getSplitter().charValue()));
            if (split.length <= 1) return null;
            else return split[1];
        } else if (flag.hasFollowingValue()) {
            String[] split = entireFlag.split(" ");
            if (split.length <= 1) return null;
            return split[1];
        } else return flag.getRawFlag();
    }
    
}
