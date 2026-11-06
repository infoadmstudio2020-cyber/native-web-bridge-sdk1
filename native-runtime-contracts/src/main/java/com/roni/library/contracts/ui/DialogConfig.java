package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;

/**
 * Standard configuration object for native modal dialogs triggered from Web or Native code.
 *
 * @author infoadmstudio2020-cyber
 */
public final class DialogConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String title;
    private final String message;
    private final String positiveButtonText;
    private final String negativeButtonText;
    private final boolean cancelable;

    public DialogConfig(@Nullable String title,
                        @NonNull String message,
                        @Nullable String positiveButtonText,
                        @Nullable String negativeButtonText,
                        boolean cancelable) {
        this.title = title != null ? title : "";
        this.message = message != null ? message : "";
        this.positiveButtonText = positiveButtonText != null ? positiveButtonText : "OK";
        this.negativeButtonText = negativeButtonText;
        this.cancelable = cancelable;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    @Nullable
    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    @NonNull
    @Override
    public String toString() {
        return "DialogConfig{title='" + title + "', message='" + message + "', cancelable=" + cancelable + "}";
    }

    public static final class Builder {
        private String title = "";
        private String message = "";
        private String positiveButtonText = "OK";
        private String negativeButtonText = null;
        private boolean cancelable = true;

        @NonNull
        public Builder title(@Nullable String title) {
            this.title = title;
            return this;
        }

        @NonNull
        public Builder message(@NonNull String message) {
            this.message = message;
            return this;
        }

        @NonNull
        public Builder positiveButtonText(@Nullable String text) {
            this.positiveButtonText = text;
            return this;
        }

        @NonNull
        public Builder negativeButtonText(@Nullable String text) {
            this.negativeButtonText = text;
            return this;
        }

        @NonNull
        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        @NonNull
        public DialogConfig build() {
            return new DialogConfig(title, message, positiveButtonText, negativeButtonText, cancelable);
        }
    }
}
