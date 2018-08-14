package com.njdaeger.bci.defaults;

import com.njdaeger.bci.base.AbstractCommandStore;
import org.bukkit.plugin.Plugin;

public class CommandStore extends AbstractCommandStore<CommandContext, TabContext, BCICommandWrapper> {
    
    public CommandStore(Plugin plugin) {
        super(plugin);
    }
}
