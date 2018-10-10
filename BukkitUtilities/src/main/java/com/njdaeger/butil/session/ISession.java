package com.njdaeger.butil.session;

import org.bukkit.plugin.Plugin;

public interface ISession<T, S extends ISession<T, S>> {
    
    /**
     * Gets the value this session stores
     * @return The session value
     */
    T getValue();
    
    /**
     * Gets the session's owning plugin
     * @return The session owner
     */
    Plugin getPlugin();
    
    /**
     * Gets the session key for this session
     * @return The session key
     */
    String getSessionKey();
    
    /**
     * The SessionNamespace this Session is stored in
     * @return The sessions SessionNamespace
     */
    SessionNamespace<T, S> getNamespace();
    
    /**
     * Get the class of the session value
     * @return The session value class
     */
    default Class<T> getType() {
        return (Class<T>)getValue().getClass();
    }

}
