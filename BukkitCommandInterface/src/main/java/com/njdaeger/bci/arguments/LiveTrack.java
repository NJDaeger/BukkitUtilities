package com.njdaeger.bci.arguments;

import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.types.ParsedType;
import com.njdaeger.bci.types.defaults.BooleanType;
import com.njdaeger.bci.types.defaults.DoubleType;
import com.njdaeger.bci.types.defaults.IntegerType;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public class LiveTrack {
    
    private final ArgumentTrack track;
    private int currentIndex = 0;
    private final String[] args;
    
    public LiveTrack(ArgumentTrack track, String[] args) {
        this.track = track;
        this.args = args;
    }
    
    /**
     * Get the next boolean argument type in this ArgumentTrack
     * @return The boolean argument if it exists
     * @throws ArgumentParseException If the argument of the desired type was not found or if there was an issue parsing.
     */
    public Boolean nextBoolean() throws ArgumentParseException {
        return next(BooleanType.class);
    }
    
    /**
     * Get the next boolean argument type in this ArgumentTrack
     * @param defaultValue The fallback value to use if the value could not be found.
     * @return The found value, or the fallback if it doesnt exist.
     * @throws ArgumentParseException if there was an issue parsing the argument.
     */
    public Boolean nextBoolean(Boolean defaultValue) throws ArgumentParseException {
        return next(BooleanType.class, defaultValue, false);
    }
    
    /**
     * Get a boolean argument type from this ArgumentTrack at the desired index.
     * @param index The index to get the argument at.
     * @return The argument at the desired index
     * @throws ArgumentParseException If the argument at the desired index is not the BooleanArg
     * type or if the argument couldnt be parsed to the argument at the index.
     */
    public Boolean booleanAt(int index) throws ArgumentParseException {
        return nextAt(BooleanType.class, index);
    }
    
    /**
     * Get the next integer argument type in this ArgumentTrack
     * @return The integer argument if it exists
     * @throws ArgumentParseException If the argument of the desired type was not found or if there was an issue parsing.
     */
    public Integer nextInt() throws ArgumentParseException {
        return next(IntegerType.class);
    }
    
    /**
     * Get the next integer argument type in this ArgumentTrack
     * @param defaultValue The fallback value to use if the value could not be found.
     * @return The found value, or the fallback if it doesnt exist.
     * @throws ArgumentParseException if there was an issue parsing the argument.
     */
    public Integer nextInt(Integer defaultValue) throws ArgumentParseException {
        return next(IntegerType.class, defaultValue, false);
    }
    
    /**
     * Get an integer argument type from this ArgumentTrack at the desired index.
     * @param index The index to get the argument at.
     * @return The argument at the desired index.
     * @throws ArgumentParseException If the argument at the desired index is not the IntegerArg
     * type or if the argument couldnt be parsed to the argument at the index.
     */
    public Integer intAt(int index) throws ArgumentParseException {
        return nextAt(IntegerType.class, index);
    }
    
    /**
     * Get the next double argument type in this ArgumentTrack
     * @return The integer argument if it exists.
     * @throws ArgumentParseException If the argument of the desired type was not found or if there was an issue parsing.
     */
    public Double nextDouble() throws ArgumentParseException {
        return next(DoubleType.class);
    }
    
    /**
     * Get the next double argument type in this ArgumentTrack
     * @param defaultValue The fallback value to use if the value could not be found.
     * @return The found value, or the fallback value if it doesnt exist.
     * @throws ArgumentParseException if there was an issue parsing the argument.
     */
    public Double nextDouble(Double defaultValue) throws ArgumentParseException {
        return next(DoubleType.class, defaultValue, false);
    }
    
    /**
     * Get a double argument type from this ArgumentTrack at the desired index.
     * @param index The index to get the argument at.
     * @return The argument at the desired index
     * @throws ArgumentParseException If the argument at the desired index is not the DoubleArg
     * type or if the argument couldnt be parsed to the argument at the index.
     */
    public Double doubleAt(int index) throws ArgumentParseException {
        return nextAt(DoubleType.class, index);
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
    public <T extends ParsedType<R>, R> R next(Class<T> type, R defaultValue, boolean throwArgumentParseException) throws ArgumentParseException {
        for (int i = currentIndex; i < args.length; i++) {
            currentIndex++;
            if (track.getArguments().get(i).getClass().equals(type)) {
                return ((T)track.getArgument(i)).parse(args[i]);
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
    public <T extends ParsedType<R>, R> R next(Class<T> type, R defaultValue) throws ArgumentParseException {
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
    public <T extends ParsedType<R>, R> R next(Class<T> type) throws ArgumentParseException {
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
    public <T extends ParsedType<R>, R> R nextAt(Class<T> type, int index) throws ArgumentParseException {
        if (!type.isInstance(track.getArgument(index))) throw new ArgumentParseException("Argument at index " + index + " is not of the " + type.getSimpleName() + " type.");
        return ((T)track.getArgument(index)).parse(args[index]);
    }
    
}
