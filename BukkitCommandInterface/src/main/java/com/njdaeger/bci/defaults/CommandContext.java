package com.njdaeger.bci.defaults;

import com.njdaeger.bci.base.AbstractCommandContext;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandContext extends AbstractCommandContext {
    
    public CommandContext(Plugin plugin, CommandSender sender, String[] args, String alias) {
        super(plugin, sender, args, alias);
    }
    
    @Override
    protected String getPluginMessagePrefix() {
        return ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + plugin.getName() + ChatColor.GRAY + "]";
    }
}
