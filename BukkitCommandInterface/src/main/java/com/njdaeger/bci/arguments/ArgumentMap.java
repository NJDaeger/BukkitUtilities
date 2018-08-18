package com.njdaeger.bci.arguments;

import com.njdaeger.bci.base.AbstractCommandContext;
import com.njdaeger.bci.base.AbstractTabContext;
import com.njdaeger.bci.base.BCICommand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class ArgumentMap<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>> implements Iterable<ArgumentTrack> {
    
    private final Map<Integer, ArgumentTrack> argumentMap;
    private BCICommand<C, T> command;
    
    ArgumentMap(BCICommand<C, T> command, List<ArgumentTrack> argumentTracks) {
        this.argumentMap = new HashMap<>();
        this.command = command;
        
        for (int i = 0; i < argumentTracks.size(); i++) {
            argumentMap.put(i, argumentTracks.get(i));
        }
    }
    
    public BCICommand<C, T> getCommand() {
        return command;
    }
    
    public void setCommand(BCICommand<C, T> command) {
        this.command = command;
        command.setArgumentMap(this);
    }
    
    public void addArgumentTrack(ArgumentTrack argumentTrack) {
        argumentMap.put(argumentMap.size(), argumentTrack);
    }
    
    public void removeArgumentTrack(int index) {
        argumentMap.remove(index);
    }
    
    public void removeArgumentTrack(ArgumentTrack argumentTrack) {
        if (argumentMap.containsValue(argumentTrack)) {
            for (int i = 0; i < argumentMap.size(); i++) {
                if (argumentMap.get(i).equals(argumentTrack)) {
                    argumentMap.remove(i);
                    break;
                }
            }
        }
    }
    
    public boolean isEmpty() {
        return argumentMap.isEmpty();
    }
    
    public ArgumentTrack getArgumentTrack(int index) {
        return argumentMap.get(index);
    }
    
    @Override
    public Iterator<ArgumentTrack> iterator() {
        return argumentMap.values().iterator();
    }
}
