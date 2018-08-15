package com.njdaeger.bci.arguments;

import com.njdaeger.bci.exceptions.ArgumentParseException;

public abstract class AbstractArgument<T> {

    private final String name;
    private boolean optional;
    
    public AbstractArgument(String name, boolean optional) {
        this.name = name;
        this.optional = optional;
    }
    
    public AbstractArgument(String name) {
        this(name, true);
    }
    
    public AbstractArgument() {
        this(null);
    }
    
    /*
    
    Arguments are going to be able to be dynamic. In other words, the user should be
    able to have more than one type of argument for a specified index of the command.
    
    It should be able to be branched off as well, for example, if the first argument
    could be a boolean or an integer and the second arg could only be a string if the
    first arg was an integer, this should be able to figure our the possible argument
    tree.
    
    StaticArgumentPart      The argument index only has one possible type.
        - /command <boolean> <boolean> <integer>
            tracks:
                boolean -> boolean -> integer
        
    VariableArgumentPart    The argument index has multiple possible argument types, but all have the same following arguments.
        - /command <boolean|string> <integer> <string>
            tracks:
                boolean -> integer -> string
                string -> integer -> string
        
        - /command <boolean|integer> <string|integer>
            tracks:
                boolean -> string
                boolean -> integer
                integer -> string
                integer -> integer
        
    ComplexArgumentPart     The argument index has multiple possible argument types- one or more of those types having different following possible arguments
        - /command <boolean|string> <boolean:<string> string:<integer>> <integer:<string>>
                    |  variable  |   |           complex             |   |   complex    |
            tracks:
                boolean -> string
                string -> integer -> string
    
    addArgument(0, BooleanArgument.class, IntegerArgument.class)
    
    ArgumentBuilder.index(0).arguments(BooleanArgument.class, IntegerArgument.class).index(1).argumentIf((b) -> b.get(0).is(BooleanArgument.class), Integer.class)
    
     */
    
    public abstract T parse(String input) throws ArgumentParseException;
    
    public abstract Class<T> getType();
    
    public String getName() {
        return name == null ? getType().getSimpleName() : name;
    }
    
    public boolean isOptional() {
        return optional;
    }
    
}
