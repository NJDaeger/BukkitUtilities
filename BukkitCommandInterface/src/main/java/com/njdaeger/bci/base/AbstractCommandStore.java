package com.njdaeger.bci.base;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractCommandStore<C extends AbstractCommandContext, T extends AbstractTabContext, W extends AbstractCommandWrapper<C, T>> {
    
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
    
}
