package com.njdaeger.bci.defaults;

import com.njdaeger.bci.base.AbstractCommandStore;
import com.njdaeger.bci.base.BCICommand;
import org.bukkit.plugin.Plugin;

public class CommandStore extends AbstractCommandStore<CommandContext, TabContext, BCICommandWrapper> {
    
    public CommandStore(Plugin plugin) {
        super(plugin);
    }
    
    @Override
    public void registerCommand(BCICommand<CommandContext, TabContext> command) {
        registerCommand(new BCICommandWrapper(plugin, command));
    }
}
