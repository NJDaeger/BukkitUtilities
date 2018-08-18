package com.njdaeger.bci.arguments;

import com.njdaeger.bci.arguments.parts.ComplexArgumentPart;
import com.njdaeger.bci.arguments.parts.StaticArgumentPart;
import com.njdaeger.bci.arguments.parts.VariableArgumentPart;
import com.njdaeger.bci.base.AbstractCommandBuilder;
import com.njdaeger.bci.base.AbstractCommandContext;
import com.njdaeger.bci.base.AbstractTabContext;
import com.njdaeger.bci.exceptions.ArgumentTrackException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class ArgumentBuilder<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>, B extends AbstractCommandBuilder<C, T, B>> {
    
    private final Map<Integer, List<AbstractArgumentPart>> indexMap;
    private final AbstractCommandBuilder<C, T, B> commandBuilder;
    private final List<ArgumentTrack> tracks;
    private int currentIndex = 0;
    
    private ArgumentBuilder() {
        this(null);
    }
    
    private ArgumentBuilder(AbstractCommandBuilder<C, T, B> command) {
        this.indexMap = new HashMap<>();
        this.tracks = new ArrayList<>();
        this.commandBuilder = command;
    }
    
    public static <C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>, B extends AbstractCommandBuilder<C, T, B>> ArgumentBuilder<C, T, B> builder(Class<C> commandContext, Class<T> tabContext, Class<B> commandBuilder) {
        return new ArgumentBuilder<>();
    }
    
    public static <C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>, B extends AbstractCommandBuilder<C, T, B>> ArgumentBuilder<C, T, B> builder(AbstractCommandBuilder<C, T, B> command) {
        return new ArgumentBuilder<>(command);
    }
    
    public static ArgumentBuilder<?, ?, ?> builder() {
        return new ArgumentBuilder<>();
    }
    
    public ArgumentBuilder<C, T, B> index(int index) {
        this.currentIndex = index;
        return this;
    }
    
    public ArgumentBuilder<C, T, B> arguments(AbstractArgument<?>... arguments) {
        if (!indexMap.containsKey(currentIndex))
            indexMap.put(currentIndex, new ArrayList<>());
        if (arguments.length > 1) {
            indexMap.get(currentIndex).add(new VariableArgumentPart(arguments));
        } else
            indexMap.get(currentIndex).add(new StaticArgumentPart(arguments[0]));
        return this;
    }
    
    public <A extends AbstractArgument> ArgumentBuilder<C, T, B> argumentsAfter(Class<A> type, AbstractArgument<?>... arguments) {
        if (currentIndex == 0)
            try {
                throw new ArgumentTrackException();
            }
            catch (ArgumentTrackException e) {
                e.printStackTrace();
            }
        if (!indexMap.containsKey(currentIndex))
            indexMap.put(currentIndex, new ArrayList<>());
        indexMap.get(currentIndex).add(new ComplexArgumentPart(type, arguments));
        return this;
    }
    
    public B build() {
        if (commandBuilder == null) throw new NullPointerException("ArgumentBuilder#build can only be used in the command builder.");
        for (int i = 0; i < indexMap.size(); i++) {
            int finalI = i;
            indexMap.get(i).forEach(part -> constructTrack(part, finalI));
        }
        return commandBuilder.arguments(new ArgumentMap<>(commandBuilder.build(), tracks));
    }
    
    public ArgumentMap<C, T> buildToMap() {
        for (int i = 0; i < indexMap.size(); i++) {
            int finalI = i;
            indexMap.get(i).forEach(part -> constructTrack(part, finalI));
        }
        return new ArgumentMap<>(null, tracks);
    }
    
    public ArgumentMap<C, T> buildEmptyMap() {
        return new ArgumentMap<>(null, new ArrayList<>());
    }
    
    private void constructTrack(AbstractArgumentPart part, int index) {
        
        if (index == 0) {
            if (!(part instanceof ComplexArgumentPart)) {
                Arrays.asList(part.getArguments()).forEach(arg -> tracks.add(new ArgumentTrack(arg)));
                return;
            } else
                try {
                    throw new ArgumentTrackException();
                }
                catch (ArgumentTrackException e) {
                    e.printStackTrace();
                }
        }
        
        if (part instanceof StaticArgumentPart) {
            for (ArgumentTrack track : tracks) {
                if (track.getArgument(index) == null) track.addArgument(((StaticArgumentPart)part).getArgument());
            }
        }
        
        if (part instanceof VariableArgumentPart) {
            for (ArgumentTrack track : tracks) {
                if (track.getArgument(index) == null) Stream.of(part.getArguments()).forEach(track::addArgument);
            }
        }
        if (part instanceof ComplexArgumentPart) {
            List<ArgumentTrack> newTracks = new ArrayList<>();
            for (ArgumentTrack track : tracks) {
                if (track.getArgument(index) == null && track.getArgument(index-1).getClass().equals(((ComplexArgumentPart)part).getRequiredPrevious())) {
                    for (AbstractArgument<?> arg : part.getArguments()) {
                        ArgumentTrack newTrack = new ArgumentTrack(track.argsBetween(index));
                        newTrack.addArgument(arg);
                        newTracks.add(newTrack);
                    }
                }
            }
            tracks.addAll(newTracks);
        }
    }
}
