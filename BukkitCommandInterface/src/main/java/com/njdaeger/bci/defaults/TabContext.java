package com.njdaeger.bci.defaults;

import com.njdaeger.bci.base.AbstractTabContext;
import com.njdaeger.bci.base.BCICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class TabContext extends AbstractTabContext<CommandContext, TabContext> {
    
    public TabContext(Plugin plugin, BCICommand<CommandContext, TabContext> command, CommandSender sender, String[] args, String alias) {
        super(plugin, command, sender, args, alias);
    }

    @Override
    protected String getPluginMessagePrefix() {
        return ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + plugin.getName() + ChatColor.GRAY + "]";
    }

}
