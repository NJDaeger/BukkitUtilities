package com.njdaeger.bci.arguments;

import com.njdaeger.bci.arguments.defaults.BooleanArg;
import com.njdaeger.bci.arguments.parts.ComplexArgumentPart;
import com.njdaeger.bci.arguments.parts.StaticArgumentPart;
import com.njdaeger.bci.arguments.parts.VariableArgumentPart;
import com.njdaeger.bci.base.AbstractCommandContext;
import com.njdaeger.bci.base.AbstractTabContext;
import com.njdaeger.bci.base.BCICommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ArgumentBuilder<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>> {
    
    private final Map<Integer, List<AbstractArgumentPart>> indexMap;
    private final BCICommand<C, T> command;
    private int currentIndex = 0;
    
    public ArgumentBuilder(BCICommand<C, T> command) {
        this.indexMap = new HashMap<>();
        this.command = command;
        new ArgumentBuilder<>(command).index(0).arguments(new BooleanArg("<hide-by-default>"));
    }
    
    public ArgumentBuilder<C, T> index(int index) {
        this.currentIndex = index;
        return this;
    }
    
    public ArgumentBuilder<C, T> arguments(AbstractArgument<?>... arguments) {
        boolean optional = currentIndex >= command.getMinArgs();
        if (!indexMap.containsKey(currentIndex)) indexMap.put(currentIndex, new ArrayList<>());
        if (arguments.length > 1) {
            indexMap.get(currentIndex).add(new VariableArgumentPart(optional, arguments));
        }
        else indexMap.get(currentIndex).add(new StaticArgumentPart(optional && arguments[0].isOptional(), arguments));
        return this;
    }
    
    public <A extends AbstractArgument> ArgumentBuilder<C, T> argumentsAfter(Class<A> type, AbstractArgument<?>... arguments) {
        boolean optional = currentIndex >= command.getMinArgs();
        if (!indexMap.containsKey(currentIndex)) indexMap.put(currentIndex, new ArrayList<>());
        indexMap.get(currentIndex).add(new ComplexArgumentPart(optional, type, arguments));
        return this;
    }
    
    public void build() {
        //this is gonna be a fucking mess
        indexMap.forEach((index, parts) -> {
            
            parts.forEach(part -> {
                
                if (part instanceof StaticArgumentPart) {
                
                }
                
                if (part instanceof VariableArgumentPart) {
                
                }
                
                
                
            });
            
        });
        
    }
    
}
