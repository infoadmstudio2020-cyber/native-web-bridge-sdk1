package com.roni.library.platform.listener;

import com.roni.library.platform.model.ToastRecord;

/**
 * Listener interface for toast and haptic events.
 *
 * @author infoadmstudio2020-cyber
 */
public interface ToastListener {

    /**
     * Called when a toast notification is displayed.
     *
     * @param record The toast record details
     */
    void onToastShown(ToastRecord record);
}
