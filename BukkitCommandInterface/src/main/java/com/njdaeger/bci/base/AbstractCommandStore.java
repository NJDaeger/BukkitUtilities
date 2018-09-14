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
    
    public AbstractCommandStore(Plugin plugin) {
        this.plugin = plugin;
        this.bciCommandMap = new HashMap<>();
    
        try {
        
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            this.bukkitCommandMap = (CommandMap)field.get(Bukkit.getServer());
        }
    
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public void unregisterCommand(String name) {
        if (isRegistered(name)) {
            Map<String, Command> commands = null;
            try {
                Field commandField = bukkitCommandMap.getClass().getDeclaredField("knownCommands");
                commandField.setAccessible(true);
                commands = (Map<String, Command>)commandField.get(bukkitCommandMap);
            }
            catch (IllegalAccessException | NoSuchFieldException e) {
                try {
                    Method method = bukkitCommandMap.getClass().getDeclaredMethod("getKnownCommands");
                    method.setAccessible(true);
                    commands = (Map<String, Command>)method.invoke(bukkitCommandMap);
                }
                catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
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
     * @param name The name or alias of the command.
     * @return True if the command is registered, false otherwise.
     */
    public boolean isRegistered(String name) {
        return bciCommandMap.containsKey(name);
    }
    
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
    
    public abstract void registerCommand(BCICommand<C, T> command);
    
    @SafeVarargs
    public final void registerCommands(BCICommand<C, T>... commands) {
        Stream.of(commands).forEach(this::registerCommand);
    }
    
    public BCICommand<C, T> getCommand(String name) {
        return bciCommandMap.get(name);
    }
    
    public List<BCICommand<C, T>> getCommands() {
        return new ArrayList<>(bciCommandMap.values());
    }
    
    public Map<String, BCICommand<C, T>> getBciCommandMap() {
        return bciCommandMap;
    }
    
    public CommandMap getBukkitCommandMap() {
        return bukkitCommandMap;
    }
    
}
