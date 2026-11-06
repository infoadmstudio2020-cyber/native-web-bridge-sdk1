package com.roni.library.contracts.common;

/**
 * Standard runtime states for subsystems in the NativeRuntime ecosystem.
 *
 * State flow:
 * UNINITIALIZED -> INITIALIZING -> READY -> SUSPENDED -> TERMINATED
 *
 * ERROR represents an initialization or runtime failure.
 *
 * @author infoadmstudio2020-cyber
 */
public enum RuntimeState {

    /** Initial state before bootstrapping begins. */
    UNINITIALIZED,

    /** Active bootstrapping and dependency resolution in progress. */
    INITIALIZING,

    /** Fully booted, active and handling requests. */
    READY,

    /** Temporarily paused or backgrounded. */
    SUSPENDED,

    /** Initialization or runtime failure occurred. */
    ERROR,

    /** Completely stopped, cleaned up and disposed. */
    TERMINATED;

    /**
     * Check if the current state is final.
     *
     * @return true if TERMINATED
     */
    public boolean isTerminal() {
        return this == TERMINATED;
    }

    /**
     * Check if the runtime is ready to accept commands.
     *
     * @return true if READY
     */
    public boolean isOperational() {
        return this == READY;
    }

    /**
     * Check if the runtime encountered an error.
     *
     * @return true if ERROR
     */
    public boolean isError() {
        return this == ERROR;
    }
}