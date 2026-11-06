package com.roni.library.ui.dialog;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages modal alerts, confirmation dialogs, and button dispatchers.
 */
public class DialogManager {

    private final AtomicBoolean dialogShowing = new AtomicBoolean(false);
    private volatile String currentTitle = "";
    private volatile String currentMessage = "";
    private Runnable confirmAction;
    private Runnable cancelAction;

    public DialogManager() {}

    public void showAlert(String title, String message, Runnable onConfirm) {
        this.currentTitle = title != null ? title : "";
        this.currentMessage = message != null ? message : "";
        this.confirmAction = onConfirm;
        this.cancelAction = null;
        this.dialogShowing.set(true);
    }

    public void showConfirm(String title, String message, Runnable onConfirm, Runnable onCancel) {
        this.currentTitle = title != null ? title : "";
        this.currentMessage = message != null ? message : "";
        this.confirmAction = onConfirm;
        this.cancelAction = onCancel;
        this.dialogShowing.set(true);
    }

    public void confirm() {
        if (dialogShowing.get()) {
            dialogShowing.set(false);
            if (confirmAction != null) {
                try {
                    confirmAction.run();
                } catch (Throwable ignored) {}
            }
        }
    }

    public void cancel() {
        if (dialogShowing.get()) {
            dialogShowing.set(false);
            if (cancelAction != null) {
                try {
                    cancelAction.run();
                } catch (Throwable ignored) {}
            }
        }
    }

    public void dismissCurrent() {
        dialogShowing.set(false);
        confirmAction = null;
        cancelAction = null;
    }

    public boolean isDialogShowing() {
        return dialogShowing.get();
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public String getCurrentMessage() {
        return currentMessage;
    }
}
