package com.njdaeger.bci.flags;

public @interface Flag {

    Class<? extends AbstractFlag> flagClass();
    
}
