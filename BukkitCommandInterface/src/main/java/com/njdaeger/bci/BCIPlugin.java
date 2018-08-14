package com.njdaeger.bci;

import com.njdaeger.bci.defaults.CommandStore;
import org.bukkit.plugin.java.JavaPlugin;

public class BCIPlugin extends JavaPlugin {
    
    private static CommandStore commandStore;
    
    @Override
    public void onEnable() {
        commandStore = new CommandStore(this);
        new CommandTest();
    }
    
    public static CommandStore getCommandStore() {
        return commandStore;
    }
    
}
