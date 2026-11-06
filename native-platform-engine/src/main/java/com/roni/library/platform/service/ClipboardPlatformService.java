package com.roni.library.platform.service;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * Subsystem service for managing system clipboard operations.
 *
 * @author infoadmstudio2020-cyber
 */
public class ClipboardPlatformService {

    private volatile String inMemoryClipboard = "";
    private volatile long lastUpdatedTimestamp = System.currentTimeMillis();
    private Context context;
    private ClipboardManager systemClipboard;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ClipboardPlatformService() {
    }

    public void init(Context context) {
        if (context == null) return;
        this.context = context.getApplicationContext();
        try {
            systemClipboard = (ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
        } catch (Throwable ignored) {
        }
    }

    public synchronized boolean setText(String text) {
        this.inMemoryClipboard = text != null ? text : "";
        this.lastUpdatedTimestamp = System.currentTimeMillis();

        if (systemClipboard != null) {
            mainHandler.post(() -> {
                try {
                    ClipData clip = ClipData.newPlainText("NativePlatformEngine", inMemoryClipboard);
                    systemClipboard.setPrimaryClip(clip);
                } catch (Throwable ignored) {
                }
            });
        }
        return true;
    }

    public synchronized String getText() {
        if (systemClipboard != null) {
            try {
                if (systemClipboard.hasPrimaryClip()) {
                    ClipData clip = systemClipboard.getPrimaryClip();
                    if (clip != null && clip.getItemCount() > 0) {
                        CharSequence text = clip.getItemAt(0).getText();
                        if (text != null) {
                            inMemoryClipboard = text.toString();
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        return inMemoryClipboard;
    }

    public synchronized boolean hasText() {
        String text = getText();
        return text != null && !text.isEmpty();
    }

    public synchronized boolean clear() {
        return setText("");
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void release() {
        systemClipboard = null;
        context = null;
    }
}
