package com.njdaeger.bci.base.executors;

import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.base.AbstractTabContext;

/**
 * The tab executor. This is used to reference tab complete methods
 * @param <T> The type of tab context being used in the tab complete method
 */
@FunctionalInterface
public interface TabExecutor<T extends AbstractTabContext> {

    /**
     * Used to execute the tab completion with the given tab context argument
     * @param context The tab context
     * @throws BCIException If an error occurred with the execution
     */
    void complete(T context) throws BCIException;
    
}
