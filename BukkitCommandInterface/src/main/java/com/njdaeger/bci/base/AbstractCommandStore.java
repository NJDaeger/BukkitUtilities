package com.njdaeger.bci.base;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public abstract class AbstractCommandStore<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>, W extends AbstractCommandWrapper<C, T>> {

    protected final Plugin plugin;
    private CommandMap bukkitCommandMap = null;
    private final Map<String, BCICommand<C, T>> bciCommandMap;

    /**
     * Creates a new command store for the specified plugin
     *
     * @param plugin The plugin to create the command store for
     */
    public AbstractCommandStore(Plugin plugin) {
        this.plugin = plugin;
        this.bciCommandMap = new HashMap<>();

        try {

            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            this.bukkitCommandMap = (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unregisters a command from the command map
     *
     * @param name The name of the command to unregister
     */
    public void unregisterCommand(String name) {
        if (isRegistered(name)) {
            Map<String, Command> commands = null;
            try {
                Field commandField = bukkitCommandMap.getClass().getDeclaredField("knownCommands");
                commandField.setAccessible(true);
                commands = (Map<String, Command>) commandField.get(bukkitCommandMap);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                try {
                    Method method = bukkitCommandMap.getClass().getDeclaredMethod("getKnownCommands");
                    method.setAccessible(true);
                    commands = (Map<String, Command>) method.invoke(bukkitCommandMap);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }

            if (commands == null) throw new RuntimeException("Unable to find knownCommands map.");
            commands.get(name).unregister(bukkitCommandMap);
            commands.remove(name);
            bciCommandMap.remove(name);

        }
    }

    /**
     * Check if a command is registered in the command map.
     *
     * @param name The name or alias of the command.
     * @return True if the command is registered, false otherwise.
     */
    public boolean isRegistered(String name) {
        return bciCommandMap.containsKey(name);
    }

    /**
     * Registers a command to the command map.
     *
     * @param wrapper Your new Command Wrapper object. (Your CommandWrapper should extend the {@link
     *         AbstractCommandWrapper} class)
     */
    public void registerCommand(W wrapper) {
        if (this.bukkitCommandMap == null) {
            throw new RuntimeException("Bukkit CommandMap could not be found.");
        }
        if (!isRegistered(wrapper.getName())) {
            bukkitCommandMap.register(plugin.getName(), wrapper);
            wrapper.getAliases().forEach(alias -> bciCommandMap.put(alias, wrapper.command));
            bciCommandMap.put(wrapper.getName(), wrapper.command);
        }
    }

    /**
     * Registers a command to the command map. Suggested implementation for this method is using the {@link
     * AbstractCommandStore#registerCommand(AbstractCommandWrapper)} method and passing a new command wrapper object to
     * it. And, ideally, the command wrapper object should accept a BCICommand argument as a default.)}
     *
     * @param command The command to register.
     */
    public abstract void registerCommand(BCICommand<C, T> command);

    /**
     * Registers an array of commands
     * @param commands The commands to register
     */
    @SafeVarargs
    public final void registerCommands(BCICommand<C, T>... commands) {
        Stream.of(commands).forEach(this::registerCommand);
    }

    /**
     * Gets a command via name
     * @param name The name of the command to get
     * @return The command if it exists, null otherwise.
     */
    public BCICommand<C, T> getCommand(String name) {
        return bciCommandMap.get(name);
    }

    /**
     * Gets a list of all this plugin's current commands
     * @return The commands this plugin has
     */
    public List<BCICommand<C, T>> getCommands() {
        return new ArrayList<>(bciCommandMap.values());
    }

    /**
     * Gets the command map being used by this plugin
     * @return The plugin command map
     */
    public Map<String, BCICommand<C, T>> getBciCommandMap() {
        return bciCommandMap;
    }

    /**
     * Gets the bukkit command map
     * @return The bukkit command map.
     */
    public CommandMap getBukkitCommandMap() {
        return bukkitCommandMap;
    }

}
