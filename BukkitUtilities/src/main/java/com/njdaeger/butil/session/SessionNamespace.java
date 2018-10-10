package com.njdaeger.butil.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SessionNamespace<T, S extends ISession<T, S>> {

    private final Map<String, S> sessions;
    private final String namespace;
    
    public SessionNamespace(String namespace) {
        this.sessions = new HashMap<>();
        this.namespace = namespace;
    }
    
    public final String getNamespace() {
        return namespace;
    }
    
    public S getSession(String sessionKey) {
        return sessions.get(sessionKey);
    }
    
    public List<S> getSessions() {
        return new ArrayList<>(sessions.values());
    }
    
    public List<S> getSessions(Predicate<S> predicate) {
        return sessions.values().stream().filter(predicate).collect(Collectors.toList());
    }
    
    public boolean addSession(S session) {
        if (sessions.containsKey(session.getSessionKey())) return false;
        sessions.put(session.getSessionKey(), session);
        return true;
    }
    
    public boolean hasSession(String key) {
        return sessions.containsKey(key);
    }

    public boolean hasSession(S session) {
        return hasSession(session.getSessionKey());
    }
    
    public S removeSession(S session) {
        return removeSession(session.getSessionKey());
    }
    
    public S removeSession(String sessionKey) {
        if (hasSession(sessionKey)) return sessions.remove(sessionKey);
        else return null;
    }
    
    public void addSessions(Collection<S> sessions) {
        sessions.stream().filter(s -> !hasSession(s)).forEach(this::addSession);
    }
    
    public void removeSessions(Collection<S> sessions) {
        sessions.stream().filter(this::hasSession).forEach(this::removeSession);
    }
    
}
