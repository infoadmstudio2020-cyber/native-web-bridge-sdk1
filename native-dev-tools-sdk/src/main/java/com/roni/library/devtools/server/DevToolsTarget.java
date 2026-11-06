package com.roni.library.devtools.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an inspectable DevTools target (e.g. WebView, Native Service, JS Context).
 */
public class DevToolsTarget implements Serializable {

    private final String id;
    private final String title;
    private final String type;
    private final String description;
    private final String url;
    private final String webSocketDebuggerUrl;

    public DevToolsTarget(String id,
                          String title,
                          String type,
                          String description,
                          String url,
                          String webSocketDebuggerUrl) {
        this.id = id != null ? id : String.valueOf(System.currentTimeMillis());
        this.title = title != null ? title : "NativeTarget";
        this.type = type != null ? type : "page";
        this.description = description != null ? description : "";
        this.url = url != null ? url : "about:blank";
        this.webSocketDebuggerUrl = webSocketDebuggerUrl != null ? webSocketDebuggerUrl : ("ws://127.0.0.1:9222/devtools/page/" + this.id);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getWebSocketDebuggerUrl() {
        return webSocketDebuggerUrl;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("type", type);
        map.put("description", description);
        map.put("url", url);
        map.put("webSocketDebuggerUrl", webSocketDebuggerUrl);
        return map;
    }
}
