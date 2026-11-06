package com.roni.library.devtools.server;

import com.roni.library.devtools.protocol.CdpDomainDispatcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the local Chrome DevTools Protocol server, targets, and discovery endpoints.
 */
public class DevToolsServer {

    private final String host;
    private final int port;
    private volatile boolean running = false;
    private final Map<String, DevToolsTarget> targets = new ConcurrentHashMap<>();
    private final CdpDomainDispatcher dispatcher = new CdpDomainDispatcher();

    public DevToolsServer() {
        this("127.0.0.1", 9222);
    }

    public DevToolsServer(String host, int port) {
        this.host = host != null ? host : "127.0.0.1";
        this.port = port > 0 ? port : 9222;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return running;
    }

    public synchronized void start() {
        this.running = true;
    }

    public synchronized void stop() {
        this.running = false;
    }

    public void registerTarget(DevToolsTarget target) {
        if (target != null) {
            targets.put(target.getId(), target);
        }
    }

    public void unregisterTarget(String id) {
        if (id != null) {
            targets.remove(id);
        }
    }

    public Map<String, Object> getVersionJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("Browser", "NativeRuntime/1.0.0");
        map.put("Protocol-Version", "1.3");
        map.put("User-Agent", "Mozilla/5.0 (Android; Mobile) NativeRuntime/1.0");
        map.put("V8-Version", "11.0.0");
        map.put("webSocketDebuggerUrl", "ws://" + host + ":" + port + "/devtools/browser");
        return map;
    }

    public List<Map<String, Object>> getListJson() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DevToolsTarget target : targets.values()) {
            list.add(target.toMap());
        }
        return list;
    }

    public boolean activateTarget(String id) {
        return id != null && targets.containsKey(id);
    }

    public Map<String, Object> dispatchCdpMessage(int id, String method, Map<String, Object> params) {
        return dispatcher.dispatch(id, method, params);
    }

    public void emitConsoleMessage(String level, String text) {
        // Broadcasts or stores console events
    }

    public CdpDomainDispatcher getDispatcher() {
        return dispatcher;
    }
}
