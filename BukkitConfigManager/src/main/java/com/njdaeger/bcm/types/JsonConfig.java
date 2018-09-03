package com.njdaeger.bcm.types;

import com.njdaeger.bcm.base.BaseConfig;
import com.njdaeger.bcm.base.ConfigType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Set;

public class JsonConfig extends BaseConfig {
    
    public JsonConfig(Plugin plugin, String configName) {
        super(plugin, ConfigType.JSON, configName);
    }
    
    @Override
    public Set<String> getKeys(boolean deep) {
        throw new UnsupportedOperationException("No json extension yet.");
    }
    
    @Override
    public Object getValue(String path) {
        throw new UnsupportedOperationException("No json extension yet.");
    }
    
    @Override
    public void addEntry(String path, Object value) {
        throw new UnsupportedOperationException("No json extension yet.");
    }
    
    @Override
    public void setEntry(String path, Object value) {
        throw new UnsupportedOperationException("No json extension yet.");
    }
    
    @Override
    public boolean isSection(String path) {
        throw new UnsupportedOperationException("No json extension yet.");
    }
    
    @Override
    public File getFile() {
        throw new UnsupportedOperationException("No json extension yet.");
    }
}
