package com.roni.library.contracts.logger;

import androidx.annotation.NonNull;

/**
 * Universal logging contract for the NativeRuntime ecosystem.
 *
 * @author infoadmstudio2020-cyber
 */
public interface ILogger {

    /**
     * Log a structured entry.
     *
     * @param entry the log entry
     */
    void log(@NonNull LogEntry entry);

    /**
     * Set minimum logging severity threshold.
     *
     * @param level minimum log level
     */
    void setMinimumLevel(@NonNull LogLevel level);

    /**
     * Get the active minimum logging severity.
     *
     * @return active minimum log level
     */
    @NonNull
    LogLevel getMinimumLevel();
}
