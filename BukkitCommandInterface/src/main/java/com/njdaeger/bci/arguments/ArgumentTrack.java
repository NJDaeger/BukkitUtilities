package com.njdaeger.bci.arguments;

import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.types.ParsedType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ArgumentTrack {
    
    private List<ParsedType<?>> arguments;
    
    ArgumentTrack() {
        this.arguments = new ArrayList<>();
    }
    
    ArgumentTrack(ParsedType<?>[] arguments) {
        this.arguments = new ArrayList<>(Arrays.asList(arguments));
    }
    
    ArgumentTrack(ParsedType<?> argument) {
        this.arguments = new ArrayList<>();
        arguments.add(argument);
    }
    
    /**
     * Get how many arguments are currently in this ArgumentTrack
     * @return How many arguments are in this ArgumentTrack.
     */
    public int getSize() {
        return arguments.size();
    }
    
    /**
     * Get the Argument at the desired index
     * @param index The index to get the argument at
     * @return The Argument at the index, or null if the index is out of bounds.
     */
    public ParsedType<?> getArgument(int index) {
        if (index >= arguments.size() || index < 0) return null;
        return arguments.get(index);
    }
    
    /**
     * Get a list of Arguments from this ArgumentTrack
     * @return An unmodifiable list of arguments from this ArgumentTrack
     */
    public List<ParsedType<?>> getArguments() {
        return Collections.unmodifiableList(arguments);
    }
    
    //adds an argument to this track
    void addArgument(ParsedType<?> argument) {
        arguments.add(argument);
    }
    
    //parses this argument track
    public void parse(String[] args) throws ArgumentParseException {
        if (args.length > getSize()) throw new ArgumentParseException();
        for (int i = 0; i < args.length; i++) {
            arguments.get(i).parse(args[i]);
        }
    }
    
    //gets arguments from 0 to the specified index.
    ParsedType<?>[] argsBetween(int exclusiveEnd) {
        return Arrays.copyOfRange(arguments.toArray(new ParsedType<?>[0]), 0, exclusiveEnd, arguments.toArray(new ParsedType<?>[0]).getClass());
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ArgumentTrack[");
        arguments.forEach(a -> builder.append(a.getName()).append(" -> "));
        if (builder.lastIndexOf(" -> ") > -1) builder.replace(builder.lastIndexOf(" -> "), builder.length(), "]");
        return builder.toString();
    }
    
}
