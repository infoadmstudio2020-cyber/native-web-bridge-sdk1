package com.roni.library.contracts.logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;

/**
 * Standard structured log entry model.
 *
 * @author infoadmstudio2020-cyber
 */
public final class LogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long timestamp;
    private final LogLevel level;
    private final String tag;
    private final String message;
    private final String threadName;
    private final Throwable throwable;
    private final String correlationId;

    public LogEntry(long timestamp,
                    @NonNull LogLevel level,
                    @NonNull String tag,
                    @NonNull String message,
                    @Nullable String threadName,
                    @Nullable Throwable throwable,
                    @Nullable String correlationId) {
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
        this.level = level != null ? level : LogLevel.INFO;
        this.tag = tag != null ? tag : "NativeRuntime";
        this.message = message != null ? message : "";
        this.threadName = threadName != null ? threadName : Thread.currentThread().getName();
        this.throwable = throwable;
        this.correlationId = correlationId != null ? correlationId : "";
    }

    public LogEntry(@NonNull LogLevel level, @NonNull String tag, @NonNull String message) {
        this(System.currentTimeMillis(), level, tag, message, Thread.currentThread().getName(), null, null);
    }

    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    public LogLevel getLevel() {
        return level;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public String getThreadName() {
        return threadName;
    }

    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }

    @NonNull
    public String getCorrelationId() {
        return correlationId;
    }

    @NonNull
    @Override
    public String toString() {
        return "[" + level.getShortName() + "/" + tag + "] " + message + (correlationId.isEmpty() ? "" : " (corr=" + correlationId + ")");
    }
}
