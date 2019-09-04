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
        return command.execute(new CommandContext(plugin, command, commandSender, args, alias));
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        System.out.println(args.length + "WRAP");
        return command.complete(new TabContext(plugin, command, sender, args, alias));
    }
    
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
