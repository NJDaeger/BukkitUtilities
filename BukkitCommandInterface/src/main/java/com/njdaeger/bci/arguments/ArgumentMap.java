package com.njdaeger.bci.arguments;

import com.njdaeger.bci.base.AbstractCommandContext;
import com.njdaeger.bci.base.AbstractTabContext;
import com.njdaeger.bci.base.BCICommand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
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
    
    /**
     * Get the command attached to this ArgumentMap
     * @return The attached command, or null if no command has ben attached yet.
     */
    public BCICommand<C, T> getCommand() {
        return command;
    }
    
    /**
     * Set the command to be attached to this ArgumentMap
     * @param command The command to be attached
     */
    public void setCommand(BCICommand<C, T> command) {
        this.command = command;
        command.setArgumentMap(this);
    }
    
    /**
     * Add an ArgumentTrack to this ArgumentMap.
     * @param argumentTrack The ArgumentTrack to add to this map.
     */
    public void addArgumentTrack(ArgumentTrack argumentTrack) {
        argumentMap.put(argumentMap.size(), argumentTrack);
    }
    
    /**
     * Remove an ArgumentTrack from this ArgumentMap via index
     * @param index The index to remove.
     */
    public void removeArgumentTrack(int index) {
        argumentMap.remove(index);
    }
    
    /**
     * Remove an ArgumentTrack from this ArgumentMap
     * @param argumentTrack The ArgumentTrack to remove.
     */
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
    
    /**
     * Whether this ArgumentMap is empty or not.
     * @return True if its empty, false otherwise.
     */
    public boolean isEmpty() {
        return argumentMap.isEmpty();
    }
    
    /**
     * Get an ArgumentTrack via index.
     * @param index The index of the ArgumentTrack
     * @return The ArgumentTrack at the given index, or null if there is no mapping for the given index.
     */
    public ArgumentTrack getArgumentTrack(int index) {
        return argumentMap.get(index);
    }
    
    @Override
    public Iterator<ArgumentTrack> iterator() {
        return argumentMap.values().iterator();
    }
}
