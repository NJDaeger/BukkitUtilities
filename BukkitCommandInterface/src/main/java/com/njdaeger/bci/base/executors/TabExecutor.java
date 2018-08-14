package com.njdaeger.bci.base.executors;

import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.base.AbstractTabContext;

@FunctionalInterface
public interface TabExecutor<T extends AbstractTabContext> {
    
    void complete(T context) throws BCIException;
    
}
