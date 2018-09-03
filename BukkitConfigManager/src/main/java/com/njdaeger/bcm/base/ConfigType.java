package com.njdaeger.bcm.base;

import com.njdaeger.bcm.types.JsonConfig;
import com.njdaeger.bcm.types.YmlConfig;

public enum ConfigType {
    
    YML(YmlConfig.class),
    JSON(JsonConfig.class);
    
    private final Class<? extends BaseConfig> configType;
    
    ConfigType(Class<? extends BaseConfig> configType) {
        this.configType = configType;
    }
    
    public Class<? extends BaseConfig> getConfigType() {
        return configType;
    }
    
}
