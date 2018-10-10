package com.njdaeger.butil.session;

import org.bukkit.plugin.Plugin;

public class AbstractSession<T, S extends ISession<T, S>> implements ISession<T, S> {
    
    private final T value;
    private final Plugin plugin;
    private final String sessionKey;
    private final SessionNamespace<T, S> namespace;
    
    public AbstractSession(Plugin plugin, String namespace, String sessionKey, T value) {
        this.value = value;
        this.plugin = plugin;
        this.sessionKey = sessionKey;
        this.namespace = (SessionNamespace<T, S>)SessionStore.getStore(plugin).getNamespace(namespace);
    }
    
    @Override
    public T getValue() {
        return value;
    }
    
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
    @Override
    public String getSessionKey() {
        return sessionKey;
    }
    
    @Override
    public SessionNamespace<T, S> getNamespace() {
        return namespace;
    }
}
