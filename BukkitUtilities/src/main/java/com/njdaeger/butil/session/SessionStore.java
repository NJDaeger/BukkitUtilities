package com.njdaeger.butil.session;

import com.njdaeger.butil.Pair;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public final class SessionStore {
    
    private static Pair<Plugin, SessionStore> PLUGIN_STORE_PAIR = null;
    private final Map<String, SessionNamespace<?, ? extends ISession<?, ?>>> namespaces;
    
    /**
     * Creates a new session store object
     */
    public SessionStore(Plugin plugin) {
        this.namespaces = new HashMap<>();
        PLUGIN_STORE_PAIR = new Pair<>(plugin, this);
    }
    
    /**
     * Gets a list of all the namespace names.
     * @return A list of all the namespace names.
     */
    public List<String> getNamespaceNames() {
        return namespaces.values().stream().map(SessionNamespace::getNamespace).collect(Collectors.toList());
    }
    
    /**
     * Gets a list of all the namespaces
     * @return A list of all the namespaces.
     */
    public List<SessionNamespace<?, ? extends ISession<?, ?>>> getNamespaces() {
        return new ArrayList<>(namespaces.values());
    }
    
    /**
     * Checks if a namespace exists.
     * @param namespace The namespace to get
     * @return True if the namespace exists, false otherwise.
     */
    public boolean hasNamespace(String namespace) {
        return namespaces.containsKey(namespace);
    }
    
    /**
     * Gets a namespace
     * @param namespace The name of the namespace
     * @return The namespace if it exists, null otherwise.
     */
    public <T extends ISession<?, T>> SessionNamespace<?, T> getNamespace(String namespace) {
        if (!hasNamespace(namespace)) return null;
        return (SessionNamespace<?, T>)namespaces.get(namespace);
    }
    
    /**
     * Gets a namespace
     * @param name The name of the namespace
     * @param sessionType The type of sessions the namespace holds
     * @param <T> The session type
     * @return The namespace if it exists, null otherwise
     */
    public <T extends ISession<?, T>> SessionNamespace<?, T> getNamespace(String name, Class<T> sessionType) {
        if (!hasNamespace(name)) return null;
        return (SessionNamespace<?, T>)namespaces.get(name);
    }

    /**
     * Gets a namespace
     * @param name The name of the namespace
     * @param holdsType The type of data the session holds
     * @param sessionType The type of session the namespace holds
     * @param <T> The type of data being held
     * @param <S> The type of session the namespace holds
     * @return The namespace if it exists, null otherwise
     */
    public <T, S extends ISession<T, S>> SessionNamespace<T, S> getNamespace(String name, Class<T> holdsType, Class<T> sessionType) {
        if (!hasNamespace(name)) return null;
        return (SessionNamespace<T, S>) namespaces.get(name);
    }

    /**
     * Adds a namespace to the session store
     * @param namespace The namespace to add
     * @return True if it was successfully added, false if it exists or was not successfully added.
     */
    public boolean addNamespace(String namespace) {
        if (hasNamespace(namespace)) return false;
        namespaces.put(namespace, new SessionNamespace<>(namespace));
        return true;
    }
    
    /**
     * Adds a namespace to the session store
     * @param namespace The namespace to add
     * @param sessionType The type of sessions the namespace holds
     * @param <T> The session type
     * @return True if it was successfully added, false if it exists or was not successfully added.
     */
    public <T extends ISession<?, T>> boolean addNamespace(String namespace, Class<T> sessionType) {
        if (hasNamespace(namespace)) return false;
        namespaces.put(namespace, new SessionNamespace<>(namespace));
        return true;
    }

    /**
     * Adds a namespace to the session store
     * @param namespace The namespace to add
     * @return True if it was successfully added, false if it exists or was not successfully added.
     */
    public boolean addNamespace(SessionNamespace<?, ?> namespace) {
        if (hasNamespace(namespace.getNamespace())) return false;
        namespaces.put(namespace.getNamespace(), namespace);
        return true;
    }

    /**
     * Removes a namepsace
     * @param namespace The name of the namespace
     * @return True if it was successfully removed, false if it doesnt exist
     */
    public boolean removeNamespace(String namespace) {
        if (!hasNamespace(namespace)) return false;
        namespaces.remove(namespace);
        return true;
    }
    
    /**
     * Removes all stored namespaces which match the given predicate.
     * @param predicate The predicate to match
     */
    public void removeNamespaces(Predicate<SessionNamespace<?, ? extends ISession<?, ?>>> predicate) {
        namespaces.values().stream().filter(predicate).map(SessionNamespace::getNamespace).forEach(this::removeNamespace);
    }
    /*
    *//**
     * Removes all the stored namespaces which match the given predicate
     * @param predicate The predicate to match
     * @param sessionType The type of session in the wanted namespace
     * @param <S> The type of session
     *//*
    public <S extends ISession<?, S>> void removeNamespaces(Predicate<SessionNamespace<?, S>> predicate, Class<S> sessionType) {
        namespaces.values().stream().filter((Predicate<? super SessionNamespace<?, ? extends ISession<?, ?>>>) predicate).map(SessionNamespace::getNamespace).forEach(this::removeNamespace);
    }
    
    *//**
     * Removes all the stored namespaces which match the given predicate
     * @param predicate The predicate to match
     * @param sessionType The type of session in the wanted namespace
     * @param type The type held in the session
     * @param <T> The type the session represents
     * @param <S> The session type.
     *//*
    public <T, S extends ISession<T, S>> void removeNamespaces(Predicate<SessionNamespace<T, S>> predicate, Class<S> sessionType, Class<T> type) {
        namespaces.values().stream().filter((Predicate<? super SessionNamespace<?, ? extends ISession<?, ?>>>)predicate).map(SessionNamespace::getNamespace).forEach(this::removeNamespace);
    }
    */
    /**
     * Removes all the stored namespaces
     */
    public void removeAll() {
        namespaces.clear();
    }
    
    static SessionStore getStore(Plugin plugin) {
        if (PLUGIN_STORE_PAIR == null) throw new RuntimeException("No session store declared for plugin " + plugin.getName());
        else return PLUGIN_STORE_PAIR.getSecond();
    }
    
}
