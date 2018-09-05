package com.njdaeger.bcm;

import com.njdaeger.bcm.base.ConfigType;
import com.njdaeger.bcm.base.IConfig;
import com.njdaeger.bcm.types.YmlConfig;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Set;

public class Configuration implements IConfig {
    
    private final IConfig config;
    
    public Configuration(Plugin plugin, ConfigType configType, String file) {
        switch (configType) {
            case YML:
                this.config = new YmlConfig(plugin, file);
                break;
            case JSON:
        default:
            throw new UnsupportedOperationException("not yet");
        }
    }
    
    @Override
    public String getName() {
        return config.getName();
    }
    
    @Override
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }
    
    @Override
    public Object getValue(String path) {
        return config.getValue(path);
    }
    
    @Override
    public Plugin getPlugin() {
        return config.getPlugin();
    }
    
    @Override
    public void addEntry(String path, Object value) {
        config.addEntry(path, value);
    }
    
    @Override
    public void setEntry(String path, Object value) {
        config.setEntry(path, value);
    }
    
    @Override
    public boolean isSection(String path) {
        return config.isSection(path);
    }
    
    @Override
    public ConfigType getType() {
        return config.getType();
    }
    
    @Override
    public File getFile() {
        return config.getFile();
    }
}
