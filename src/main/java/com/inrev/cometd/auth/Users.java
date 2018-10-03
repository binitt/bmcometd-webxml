package com.inrev.cometd.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.cometd.bayeux.server.ServerSession;

public class Users {
    private final ConcurrentMap<String, ServerSession> users = new ConcurrentHashMap<>();

    public void put(ServerSession session, Map<String, Object> credentials) {
        String user = (String)credentials.get("user");
        users.put(user, session);
    }

    public void remove(ServerSession session) {
        users.values().remove(session);
    }
}