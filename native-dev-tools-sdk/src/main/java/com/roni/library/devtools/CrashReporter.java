package com.roni.library.devtools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Intercepts uncaught exceptions, records stack traces, and formats diagnostic crash exports.
 */
public class CrashReporter {

    private final AtomicBoolean installed = new AtomicBoolean(false);
    private Thread.UncaughtExceptionHandler defaultHandler;
    private final List<Map<String, Object>> crashLogs = new CopyOnWriteArrayList<>();

    public CrashReporter() {}

    public synchronized void install() {
        if (installed.compareAndSet(false, true)) {
            defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    recordCrash(e);
                    if (defaultHandler != null) {
                        defaultHandler.uncaughtException(t, e);
                    }
                }
            });
        }
    }

    public void recordCrash(Throwable t) {
        if (t == null) return;

        Map<String, Object> log = new HashMap<>();
        log.put("timestamp", System.currentTimeMillis());
        log.put("exceptionClass", t.getClass().getName());
        log.put("message", t.getMessage() != null ? t.getMessage() : "No message");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        log.put("stackTrace", sw.toString());

        crashLogs.add(log);
        if (crashLogs.size() > 50) {
            crashLogs.remove(0);
        }
    }

    public List<Map<String, Object>> getCrashLogs() {
        return new ArrayList<>(crashLogs);
    }

    public void clearCrashLogs() {
        crashLogs.clear();
    }

    public String exportCrashReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== NativeDevToolsSDK Crash Report ===\n");
        sb.append("Total Crashes Captured: ").append(crashLogs.size()).append("\n\n");

        for (int i = 0; i < crashLogs.size(); i++) {
            Map<String, Object> log = crashLogs.get(i);
            sb.append("[").append(i + 1).append("] Timestamp: ").append(log.get("timestamp")).append("\n");
            sb.append("Type: ").append(log.get("exceptionClass")).append("\n");
            sb.append("Message: ").append(log.get("message")).append("\n");
            sb.append("Stack Trace:\n").append(log.get("stackTrace")).append("\n");
            sb.append("--------------------------------------\n");
        }
        return sb.toString();
    }
}
