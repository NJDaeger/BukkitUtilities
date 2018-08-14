package com.njdaeger.bci.base.executors;

import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.base.AbstractCommandContext;

@FunctionalInterface
public interface CommandExecutor<C extends AbstractCommandContext> {
    
    void execute(C commandContext) throws BCIException;
    
}
