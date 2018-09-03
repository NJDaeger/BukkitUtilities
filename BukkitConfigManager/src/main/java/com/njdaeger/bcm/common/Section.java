package com.njdaeger.bcm.common;

import com.njdaeger.bcm.base.ConfigType;
import com.njdaeger.bcm.base.IConfig;
import com.njdaeger.bcm.base.ISection;
import org.bukkit.plugin.Plugin;

public class Section implements ISection {
    
    private final IConfig config;
    private final String path;
    
    public Section(String path, IConfig config) {
        this.config = config;
        this.path = path;
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
    public Plugin getPlugin() {
        return config.getPlugin();
    }
    
    public void addEntry(String path, Object value) {
        config.addEntry(path, value);
    }
    
    public void setEntry(String path, Object value) {
        config.setEntry(path, value);
    }
    
    public String getCurrentPath() {
        return path;
    }
    
    public IConfig getConfig() {
        return config;
    }
}
