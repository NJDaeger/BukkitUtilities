package com.njdaeger.bci.defaults;

import com.njdaeger.bci.base.AbstractCommandWrapper;
import com.njdaeger.bci.base.BCICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class BCICommandWrapper extends AbstractCommandWrapper<CommandContext, TabContext> {
    
    public BCICommandWrapper(Plugin plugin, BCICommand<CommandContext, TabContext> command) {
        super(plugin, command);
    }
    
    @Override
    public boolean execute(CommandSender commandSender, String alias, String[] args) {
        return command.execute(new CommandContext(plugin, commandSender, args, alias));
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return command.complete(new TabContext(new CommandContext(plugin, sender, args, alias), command));
    }
    
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
