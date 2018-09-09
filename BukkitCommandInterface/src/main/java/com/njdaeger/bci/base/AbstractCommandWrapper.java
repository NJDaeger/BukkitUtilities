package com.njdaeger.bci.base;

import com.njdaeger.bci.exceptions.PermissionDeniedException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractCommandWrapper<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>> extends Command implements PluginIdentifiableCommand {
    
    protected final BCICommand<C, T> command;
    private final String[] permissions;
    protected final Plugin plugin;
    
    public AbstractCommandWrapper(Plugin plugin, BCICommand<C, T> command) {
        super(command.getName());
        this.plugin = plugin;
        this.command = command;
        this.usageMessage = command.getUsage();
        this.description = command.getDescription();
        this.permissions = command.getPermissions();
        this.setAliases(Arrays.asList(command.getAliases()));
    }
    
    @Override
    public boolean testPermission(CommandSender sender) {
        if (testPermissionSilent(sender)) return true;
        try {
            throw new PermissionDeniedException();
        }
        catch (BCIException e) {
            e.showError(sender);
        }
        return false;
        
    }
    
    @Override
    public boolean testPermissionSilent(CommandSender sender) {
        if (this.permissions == null || this.permissions.length == 0) return true;
        return Stream.of(permissions).anyMatch(sender::hasPermission);
    }
    
    @Override
    public abstract boolean execute(CommandSender commandSender, String alias, String[] args);
    
    @Override
    public abstract List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException;
    
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
