package com.roni.library.contracts.logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Standard log severity levels for NativeRuntime.
 *
 * @author infoadmstudio2020-cyber
 */
public enum LogLevel {

    VERBOSE(2, "V"),
    DEBUG(3, "D"),
    INFO(4, "I"),
    WARN(5, "W"),
    ERROR(6, "E"),
    NONE(7, "N");

    private final int priority;
    private final String shortName;

    LogLevel(int priority, @NonNull String shortName) {
        this.priority = priority;
        this.shortName = shortName;
    }

    public int getPriority() {
        return priority;
    }

    @NonNull
    public String getShortName() {
        return shortName;
    }

    /**
     * Check if this log level meets or exceeds the given minimum threshold.
     *
     * @param minimumLevel minimum required level
     * @return true if loggable
     */
    public boolean isLoggable(@NonNull LogLevel minimumLevel) {
        if (this == NONE || minimumLevel == NONE) {
            return false;
        }
        return this.priority >= minimumLevel.priority;
    }

    /**
     * Parse log level from string name, fallback to default.
     *
     * @param levelName    name of level
     * @param defaultLevel fallback
     * @return matching LogLevel
     */
    @NonNull
    public static LogLevel fromString(@Nullable String levelName, @NonNull LogLevel defaultLevel) {
        if (levelName == null || levelName.trim().isEmpty()) {
            return defaultLevel;
        }
        try {
            return LogLevel.valueOf(levelName.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return defaultLevel;
        }
    }
}
