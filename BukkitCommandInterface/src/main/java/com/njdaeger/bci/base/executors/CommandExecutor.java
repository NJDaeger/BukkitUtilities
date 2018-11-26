package com.njdaeger.bci.base.executors;

import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.base.AbstractCommandContext;

/**
 * The command executor. This is used to reference command methods
 * @param <C> The type of command context being used in the command method
 */
@FunctionalInterface
public interface CommandExecutor<C extends AbstractCommandContext> {

    /**
     * Used to execute the command with the given command context argument.
     * @param commandContext The command context
     * @throws BCIException If an error occurred with the execution
     */
    void execute(C commandContext) throws BCIException;
    
}
