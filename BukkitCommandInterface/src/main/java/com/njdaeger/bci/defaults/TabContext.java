package com.njdaeger.bci.defaults;

import com.njdaeger.bci.base.AbstractTabContext;
import com.njdaeger.bci.base.BCICommand;

public class TabContext extends AbstractTabContext<CommandContext, TabContext> {
    
    public TabContext(CommandContext commandContext, BCICommand<CommandContext, TabContext> command) {
        super(commandContext, command);
    }
}
