package com.njdaeger.bci.arguments;

import com.njdaeger.bci.base.AbstractCommandContext;
import com.njdaeger.bci.base.AbstractTabContext;
import com.njdaeger.bci.base.BCICommand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class ArgumentMap<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>> implements Iterable<ArgumentTrack> {
    
    private final Map<Integer, ArgumentTrack> argumentTracks;
    private final BCICommand<C, T> command;
    
    public ArgumentMap(BCICommand<C, T> command) {
        this.argumentTracks = new HashMap<>();
        this.command = command;
    }
    
    public BCICommand<C, T> getCommand() {
        return command;
    }
    
    public void addArgumentTrack(ArgumentTrack argumentTrack) {
        argumentTracks.put(argumentTracks.size(), argumentTrack);
    }
    
    public void removeArgumentTrack(int index) {
        argumentTracks.remove(index);
    }
    
    public void removeArgumentTrack(ArgumentTrack argumentTrack) {
        if (argumentTracks.containsValue(argumentTrack)) {
            for (int i = 0; i < argumentTracks.size(); i++) {
                if (argumentTracks.get(i).equals(argumentTrack)) {
                    argumentTracks.remove(i);
                    break;
                }
            }
        }
    }
    
    public ArgumentTrack getArgumentTrack(int index) {
        return argumentTracks.get(index);
    }
    
    @Override
    public Iterator<ArgumentTrack> iterator() {
        return argumentTracks.values().iterator();
    }
}
