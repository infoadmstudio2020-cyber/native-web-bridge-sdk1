package com.roni.library.contracts.common;

/**
 * Universal interface for resources that can be explicitly released.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IDisposable {

    /**
     * Release all held resources and terminate background workers.
     */
    void dispose();

    /**
     * Check if this instance has been disposed.
     *
     * @return true if disposed
     */
    boolean isDisposed();
}
