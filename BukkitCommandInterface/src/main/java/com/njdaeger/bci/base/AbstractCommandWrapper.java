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

    /**
     * Creates a new command wrapper object which wraps the bukkit default command
     * @param plugin The plugin which owns this command
     * @param command The command being wrapped
     */
    public AbstractCommandWrapper(Plugin plugin, BCICommand<C, T> command) {
        super(command.getName());
        this.plugin = plugin;
        this.command = command;
        this.usageMessage = command.getUsage();
        this.description = command.getDescription();
        this.permissions = command.getPermissions();
        this.setAliases(Arrays.asList(command.getAliases()));
    }

    /**
     * Tests to see if the command sender has permission to run this command or not. This is used for the help map.
     * @param sender The command sender to test the permission of
     * @return True if the sender has permission to run this command, false otherwise
     */
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

    /**
     * Tests to see if the command sender has permission to run this command or not. This is also used for the help map
     * @param sender The sender to test the permission of
     * @return True if the sender has permission to run this command, false otherwise
     */
    @Override
    public boolean testPermissionSilent(CommandSender sender) {
        if (this.permissions == null || this.permissions.length == 0) return true;
        return Stream.of(permissions).anyMatch(sender::hasPermission);
    }

    /**
     * Executes this command
     * @param commandSender The command sender
     * @param alias The alias used to run this command
     * @param args The args used with this command
     * @return True if successful
     */
    @Override
    public abstract boolean execute(CommandSender commandSender, String alias, String[] args);

    /**
     * Ececutes this command tab competer
     * @param sender The command sender
     * @param alias The alias used to run this command
     * @param args The args used with this command
     * @return The list of arguments allwoed
     */
    @Override
    public abstract List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException;

    /**
     * The plugin which owns this command
     * @return The owning plugin
     */
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
