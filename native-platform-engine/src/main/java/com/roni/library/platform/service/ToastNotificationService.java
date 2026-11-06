package com.roni.library.platform.service;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.roni.library.platform.listener.ToastListener;
import com.roni.library.platform.model.ToastRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Subsystem service for dispatching toast notifications and tactile haptics.
 *
 * @author infoadmstudio2020-cyber
 */
public class ToastNotificationService {

    private final List<ToastRecord> toastHistory = new ArrayList<>();
    private final List<ToastListener> listeners = new CopyOnWriteArrayList<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Context context;
    private Vibrator vibrator;

    private volatile String lastMessage = "";
    private volatile boolean lastIsLong = false;
    private volatile long lastTimestamp = 0;
    private volatile long lastVibrationMs = 0;

    public ToastNotificationService() {
    }

    public void init(Context context) {
        if (context == null) return;
        this.context = context.getApplicationContext();
        try {
            vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        } catch (Throwable ignored) {
        }
    }

    public String getMessage() {
        return lastMessage;
    }

    public boolean isLong() {
        return lastIsLong;
    }

    public long getTimestamp() {
        return lastTimestamp;
    }

    public synchronized void showToast(String message, boolean isLong) {
        this.lastMessage = message != null ? message : "";
        this.lastIsLong = isLong;
        this.lastTimestamp = System.currentTimeMillis();

        ToastRecord record = new ToastRecord(lastMessage, isLong, lastTimestamp);
        toastHistory.add(record);
        if (toastHistory.size() > 50) {
            toastHistory.remove(0);
        }

        if (context != null) {
            mainHandler.post(() -> {
                try {
                    int duration = isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
                    Toast.makeText(context, lastMessage, duration).show();
                } catch (Throwable ignored) {
                }
            });
        }

        for (ToastListener listener : listeners) {
            try {
                listener.onToastShown(record);
            } catch (Throwable ignored) {
            }
        }
    }

    public void showToast(String message) {
        showToast(message, false);
    }

    public synchronized void vibrate(long durationMs) {
        long duration = Math.max(10, Math.min(3000, durationMs));
        this.lastVibrationMs = duration;

        if (vibrator != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(duration);
                }
            } catch (Throwable ignored) {
            }
        }
    }

    public synchronized List<ToastRecord> getToastHistory() {
        return Collections.unmodifiableList(new ArrayList<>(toastHistory));
    }

    public long getLastVibrationMs() {
        return lastVibrationMs;
    }

    public void addListener(ToastListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ToastListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void release() {
        listeners.clear();
        context = null;
        vibrator = null;
    }
}
