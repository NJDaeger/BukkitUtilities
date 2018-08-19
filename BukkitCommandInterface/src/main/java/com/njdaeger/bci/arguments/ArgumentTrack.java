package com.njdaeger.bci.arguments;

import com.njdaeger.bci.arguments.defaults.BooleanArg;
import com.njdaeger.bci.arguments.defaults.DoubleArg;
import com.njdaeger.bci.arguments.defaults.IntegerArg;
import com.njdaeger.bci.exceptions.ArgumentParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public class ArgumentTrack {
    
    private List<AbstractArgument<?>> arguments;
    private int currentIndex = 0;
    private String[] args;
    
    ArgumentTrack(AbstractArgument<?>[] arguments) {
        this.arguments = new ArrayList<>(Arrays.asList(arguments));
    }
    
    ArgumentTrack(AbstractArgument<?> argument) {
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
    public AbstractArgument<?> getArgument(int index) {
        if (index >= arguments.size() || index < 0) return null;
        return arguments.get(index);
    }
    
    /**
     * Get a list of Arguments from this ArgumentTrack
     * @return An unmodifiable list of arguments from this ArgumentTrack
     */
    public List<AbstractArgument<?>> getArguments() {
        return Collections.unmodifiableList(arguments);
    }
    
    //adds an argument to this track
    void addArgument(AbstractArgument<?> argument) {
        arguments.add(argument);
    }
    
    //parses this argument track
    void parse(String[] args) throws ArgumentParseException {
        this.currentIndex = 0;
        if (args.length > getSize()) throw new ArgumentParseException();
        this.args = args;
        for (int i = 0; i < args.length; i++) {
            arguments.get(i).parse(args[i]);
        }
    }
    
    //gets arguments from 0 to the specified index.
    AbstractArgument<?>[] argsBetween(int exclusiveEnd) {
        return Arrays.copyOfRange(arguments.toArray(new AbstractArgument<?>[0]), 0, exclusiveEnd, arguments.toArray(new AbstractArgument<?>[0]).getClass());
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ArgumentTrack[");
        arguments.forEach(a -> builder.append(a.getName()).append(" -> "));
        if (builder.lastIndexOf(" -> ") > -1) builder.replace(builder.lastIndexOf(" -> "), builder.length(), "]");
        return builder.toString();
    }
    
    /**
     * Get the next boolean argument type in this ArgumentTrack
     * @return The boolean argument if it exists
     * @throws ArgumentParseException If the argument of the desired type was not found or if there was an issue parsing.
     */
    public Boolean nextBoolean() throws ArgumentParseException {
        return next(BooleanArg.class);
    }
    
    /**
     * Get the next boolean argument type in this ArgumentTrack
     * @param defaultValue The fallback value to use if the value could not be found.
     * @return The found value, or the fallback if it doesnt exist.
     * @throws ArgumentParseException if there was an issue parsing the argument.
     */
    public Boolean nextBoolean(Boolean defaultValue) throws ArgumentParseException {
        return next(BooleanArg.class, defaultValue, false);
    }
    
    /**
     * Get a boolean argument type from this ArgumentTrack at the desired index.
     * @param index The index to get the argument at.
     * @return The argument at the desired index
     * @throws ArgumentParseException If the argument at the desired index is not the BooleanArg
     * type or if the argument couldnt be parsed to the argument at the index.
     */
    public Boolean booleanAt(int index) throws ArgumentParseException {
        return nextAt(BooleanArg.class, index);
    }
    
    /**
     * Get the next integer argument type in this ArgumentTrack
     * @return The integer argument if it exists
     * @throws ArgumentParseException If the argument of the desired type was not found or if there was an issue parsing.
     */
    public Integer nextInt() throws ArgumentParseException {
        return next(IntegerArg.class);
    }
    
    /**
     * Get the next integer argument type in this ArgumentTrack
     * @param defaultValue The fallback value to use if the value could not be found.
     * @return The found value, or the fallback if it doesnt exist.
     * @throws ArgumentParseException if there was an issue parsing the argument.
     */
    public Integer nextInt(Integer defaultValue) throws ArgumentParseException {
        return next(IntegerArg.class, defaultValue, false);
    }
    
    /**
     * Get an integer argument type from this ArgumentTrack at the desired index.
     * @param index The index to get the argument at.
     * @return The argument at the desired index.
     * @throws ArgumentParseException If the argument at the desired index is not the IntegerArg
     * type or if the argument couldnt be parsed to the argument at the index.
     */
    public Integer intAt(int index) throws ArgumentParseException {
        return nextAt(IntegerArg.class, index);
    }
    
    /**
     * Get the next double argument type in this ArgumentTrack
     * @return The integer argument if it exists.
     * @throws ArgumentParseException If the argument of the desired type was not found or if there was an issue parsing.
     */
    public Double nextDouble() throws ArgumentParseException {
        return next(DoubleArg.class);
    }
    
    /**
     * Get the next double argument type in this ArgumentTrack
     * @param defaultValue The fallback value to use if the value could not be found.
     * @return The found value, or the fallback value if it doesnt exist.
     * @throws ArgumentParseException if there was an issue parsing the argument.
     */
    public Double nextDouble(Double defaultValue) throws ArgumentParseException {
        return next(DoubleArg.class, defaultValue, false);
    }
    
    /**
     * Get a double argument type from this ArgumentTrack at the desired index.
     * @param index The index to get the argument at.
     * @return The argument at the desired index
     * @throws ArgumentParseException If the argument at the desired index is not the DoubleArg
     * type or if the argument couldnt be parsed to the argument at the index.
     */
    public Double doubleAt(int index) throws ArgumentParseException {
        return nextAt(DoubleArg.class, index);
    }
    
    /**
     * Get the next Argument of the desired type in this ArgumentTrack
     * @param type The desired argument type to search for
     * @param defaultValue The fallback value to use if the value could not be found
     * @param throwArgumentParseException Whether to throw an ArgumentParseException if the argument
     *                                    could not be found. If false, the default value will be
     *                                    returned.
     * @param <T> The AbstractArgument type to search for.
     * @param <R> The type to return
     * @return The argument if found, or if it is not found it will return the default value only if
     * the throwArgumentParseException arg is false, if it is true the exception will be thrown.
     * @throws ArgumentParseException If there was an issue parsing or if the desired argument was
     * not found and throwArgumentParseException is true.
     */
    public <T extends AbstractArgument<R>, R> R next(Class<T> type, R defaultValue, boolean throwArgumentParseException) throws ArgumentParseException {
        for (int i = currentIndex; i < args.length; i++) {
            currentIndex++;
            if (arguments.get(i).getClass().equals(type)) {
                return ((T)arguments.get(i)).parse(args[i]);
            }
        }
        if (throwArgumentParseException) {
            throw new ArgumentParseException();
        } else return defaultValue;
    }
    
    /**
     * Get the next argument of the desired type in this ArgumentTrack
     * @param type The desired argument type to search for
     * @param defaultValue The fallback value to use if the value could not be found
     * @param <T> The AbstractArgument type to search for.
     * @param <R> The type to return
     * @return The argument if found, or the fallback value otherwise.
     * @throws ArgumentParseException If there was an issue parsing.
     */
    public <T extends AbstractArgument<R>, R> R next(Class<T> type, R defaultValue) throws ArgumentParseException {
        return next(type, defaultValue, false);
    }
    
    /**
     * Get the next desired argument type in this ArgumentTrack
     * @param type The desired argument type to search for
     * @param <T> The AbstractArgument type to search for
     * @param <R> The type to return
     * @return The argument if found
     * @throws ArgumentParseException If the argument of the desired type was not found or if there was an issue parsing.
     */
    public <T extends AbstractArgument<R>, R> R next(Class<T> type) throws ArgumentParseException {
        return next(type, null, true);
    }
    
    /**
     * Get an argument of the desired type from this ArgumentTrack at the desired index
     * @param type The desired argument type to search for
     * @param index The index to get the argument at
     * @param <T> The AbstractArgument type to search for.
     * @param <R> The type to return
     * @return The argument at the desired index.
     * @throws ArgumentParseException  If the argument at the desired index is not the specified
     * type or if the argument couldnt be parsed to the argument at the index.
     */
    public <T extends AbstractArgument<R>, R> R nextAt(Class<T> type, int index) throws ArgumentParseException {
        if (!type.isInstance(arguments.get(index))) throw new ArgumentParseException("Argument at index " + index + " is not of the " + type.getSimpleName() + " type.");
        return ((T)arguments.get(index)).parse(args[index]);
    }
    
}
