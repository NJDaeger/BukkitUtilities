package com.njdaeger.bci.arguments;

import com.njdaeger.bci.arguments.parts.ComplexArgumentPart;
import com.njdaeger.bci.arguments.parts.StaticArgumentPart;
import com.njdaeger.bci.arguments.parts.VariableArgumentPart;
import com.njdaeger.bci.base.AbstractCommandBuilder;
import com.njdaeger.bci.base.AbstractCommandContext;
import com.njdaeger.bci.base.AbstractTabContext;
import com.njdaeger.bci.exceptions.ArgumentTrackException;
import com.njdaeger.bci.types.ParsedType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("unused")
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
    
    /**
     * Create a new instance of the ArgumentBuilder
     * @param commandContext Class of the current CommandContext
     * @param tabContext Class of the current TabContext
     * @param commandBuilder Class of the current CommandBuilder
     * @param <C> Command Context
     * @param <T> Tab Context
     * @param <B> Command Builder
     */
    public static <C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>, B extends AbstractCommandBuilder<C, T, B>> ArgumentBuilder<C, T, B> builder(Class<C> commandContext, Class<T> tabContext, Class<B> commandBuilder) {
        return new ArgumentBuilder<>();
    }
    
    /**
     * Create a new instance of the ArgumentBuilder
     * @param command The command which is currently being created.
     * @param <C> Command Context
     * @param <T> Tab Context
     * @param <B> Command Builder
     */
    public static <C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>, B extends AbstractCommandBuilder<C, T, B>> ArgumentBuilder<C, T, B> builder(AbstractCommandBuilder<C, T, B> command) {
        return new ArgumentBuilder<>(command);
    }
    
    /**
     * Create a new instance of the ArgumentBuilder with no specific Contexts/builder
     */
    public static ArgumentBuilder<?, ?, ?> builder() {
        return new ArgumentBuilder<>();
    }
    
    /**
     * Set the current index the arguments should currently be getting put
     * at. This does not need to be used for the first argument, as the default
     * for this is 0. For following arguments, though, this needs to be set to
     * the desired index.
     * @param index The current index
     */
    public ArgumentBuilder<C, T, B> index(int index) {
        this.currentIndex = index;
        return this;
    }
    
    /**
     * Add an argument to the current index of this argument. If one argument is
     * added, it will be a StaticArgumentPart, if more than one is added it will
     * be a VariableArgumentPart. This can be called more than once at a given
     * index to add more arguments.
     * @param arguments The array of arguments to add to the index.
     */
    public ArgumentBuilder<C, T, B> arguments(ParsedType<?>... arguments) {
        if (!indexMap.containsKey(currentIndex))
            indexMap.put(currentIndex, new ArrayList<>());
        if (arguments.length > 1) {
            indexMap.get(currentIndex).add(new VariableArgumentPart(arguments));
        } else
            indexMap.get(currentIndex).add(new StaticArgumentPart(arguments[0]));
        return this;
    }
    
    /**
     * Add an argument to the current index of this argument only if the argument
     * at the previous index is the same type as the type provided. NOTE: If this
     * is called when the currentIndex == 0, an ArgumentTrackException will be
     * thrown.
     * @param type The previous argument type required
     * @param arguments The arguments to add
     * @param <A> The required argument type
     */
    public <A extends ParsedType> ArgumentBuilder<C, T, B> argumentsAfter(Class<A> type, ParsedType<?>... arguments) {
        if (currentIndex == 0)
            try {
                throw new ArgumentTrackException("argumentAfter cannot be called when the currentIndex is 0");
            }
            catch (ArgumentTrackException e) {
                e.printStackTrace();
            }
        if (!indexMap.containsKey(currentIndex))
            indexMap.put(currentIndex, new ArrayList<>());
        indexMap.get(currentIndex).add(new ComplexArgumentPart(type, arguments));
        return this;
    }
    
    /**
     * Builds this ArgumentBuider into a CommandBuilder
     * @return The CommandBuilder being used.
     */
    public B build() {
        if (commandBuilder == null) throw new NullPointerException("ArgumentBuilder#build can only be used in the command builder.");
        for (int i = 0; i < indexMap.size(); i++) {
            int finalI = i;
            indexMap.get(i).forEach(part -> constructTrack(part, finalI));
        }
        return commandBuilder.arguments(new ArgumentMap<>(commandBuilder.build(), tracks));
    }
    
    /**
     * Builds this ArgumentBuilder into an ArgumentMap
     * @return A new ArgumentMap with no attached command.
     */
    public ArgumentMap<C, T> buildToMap() {
        for (int i = 0; i < indexMap.size(); i++) {
            int finalI = i;
            indexMap.get(i).forEach(part -> constructTrack(part, finalI));
        }
        return new ArgumentMap<>(null, tracks);
    }
    
    /**
     * Builds this ArgumentBuilder into an empty ArgumentMap
     * @return A new ArgumentMap with no attached command and no defined arguments.
     */
    public ArgumentMap<C, T> buildEmptyMap() {
        return new ArgumentMap<>(null, new ArrayList<>());
    }
    
    //Constructs an argument track for the given argument part.
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
                    for (ParsedType<?> arg : part.getArguments()) {
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
