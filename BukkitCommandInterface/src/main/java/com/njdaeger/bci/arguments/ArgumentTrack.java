package com.njdaeger.bci.arguments;

import com.njdaeger.bci.arguments.defaults.BooleanArg;
import com.njdaeger.bci.arguments.defaults.DoubleArg;
import com.njdaeger.bci.arguments.defaults.IntegerArg;
import com.njdaeger.bci.exceptions.ArgumentParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    
    public int getSize() {
        return arguments.size();
    }
    
    public AbstractArgument<?> getArgument(int index) {
        if (index >= arguments.size() || index < 0) return null;
        return arguments.get(index);
    }
    
    public List<AbstractArgument<?>> getArguments() {
        return Collections.unmodifiableList(arguments);
    }
    
    void addArgument(AbstractArgument<?> argument) {
        arguments.add(argument);
    }
    
    void parse(String[] args) throws ArgumentParseException {
        this.currentIndex = 0;
        if (args.length > getSize()) throw new ArgumentParseException();
        this.args = args;
        for (int i = 0; i < args.length; i++) {
            arguments.get(i).parse(args[i]);
        }
    }
    
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
    
    public boolean nextBoolean() throws ArgumentParseException {
        return next(BooleanArg.class);
    }
    
    public boolean booleanAt(int index) throws ArgumentParseException {
        if (!(arguments.get(index) instanceof BooleanArg)) throw new ArgumentParseException("Argument at index " + index + " is not of the boolean type.");
        return ((BooleanArg)arguments.get(index)).parse(args[index]);
    }
    
    public int nextInt() throws ArgumentParseException {
        return next(IntegerArg.class);
    }
    
    public int intAt(int index) throws ArgumentParseException {
        if (!(arguments.get(index) instanceof IntegerArg)) throw new ArgumentParseException("Argument at index " + index + " is not of the integer type.");
        return ((IntegerArg)arguments.get(index)).parse(args[index]);
    }
    
    public double nextDouble() throws ArgumentParseException {
        return next(DoubleArg.class);
    }
    
    public double doubleAt(int index) throws ArgumentParseException {
        if (!(arguments.get(index) instanceof BooleanArg)) throw new ArgumentParseException("Argument at index " + index + " is not of the double type.");
        return ((DoubleArg)arguments.get(index)).parse(args[index]);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends AbstractArgument<R>, R> R next(Class<T> type) throws ArgumentParseException {
        for (int i = currentIndex; i < args.length; i++) {
            currentIndex++;
            if (arguments.get(i).getClass().equals(type)) {
                return ((T)arguments.get(i)).parse(args[i]);
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends AbstractArgument<R>, R> R nextAt(Class<T> type, int index) throws ArgumentParseException {
        if (!type.isInstance(arguments.get(index))) throw new ArgumentParseException("Argument at index " + index + " is not of the " + type.getSimpleName() + " type.");
        return ((T)arguments.get(index)).parse(args[index]);
    }
    
}
