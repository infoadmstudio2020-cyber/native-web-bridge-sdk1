package com.roni.library.runtime.server;

import com.roni.library.runtime.source.WebSource;
import com.roni.library.runtime.source.WebSourceResolver;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Local HTTP server controller for 127.0.0.1 offline web assets.
 */
public class LocalWebServer {

    private final int port;
    private final WebSourceResolver resolver;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public LocalWebServer() {
        this(8080, new WebSourceResolver());
    }

    public LocalWebServer(int port, WebSourceResolver resolver) {
        this.port = port > 0 ? port : 8080;
        this.resolver = resolver != null ? resolver : new WebSourceResolver();
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return running.get();
    }

    public synchronized void start() {
        running.set(true);
    }

    public synchronized void stop() {
        running.set(false);
    }

    public WebSource handleRequest(String path, String method) {
        if (!running.get()) {
            return WebSource.notFound(path);
        }
        return resolver.resolve(path);
    }

    public WebSourceResolver getResolver() {
        return resolver;
    }
}
