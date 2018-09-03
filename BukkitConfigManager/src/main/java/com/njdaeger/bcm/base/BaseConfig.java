package com.njdaeger.bcm.base;

import org.bukkit.plugin.Plugin;

public abstract class BaseConfig implements IConfig {
    
    private final String configName;
    private final ConfigType type;
    private final Plugin plugin;
    
    public BaseConfig(Plugin plugin, ConfigType type, String configName) {
        this.type = type;
        this.plugin = plugin;
        this.configName = configName;
    }
    
    @Override
    public ConfigType getType() {
        return type;
    }
    
    @Override
    public String getName() {
        return configName;
    }
    
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
