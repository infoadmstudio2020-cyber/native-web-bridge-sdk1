package com.roni.library.security.biometric;

/**
 * Configuration options for the BiometricPrompt UI dialog.
 *
 * @author infoadmstudio2020-cyber
 */
public class BiometricPromptConfig {

    private final String title;
    private final String subtitle;
    private final String description;
    private final String negativeButtonText;
    private final boolean confirmationRequired;
    private final int allowedAuthenticators;

    private BiometricPromptConfig(Builder builder) {
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.description = builder.description;
        this.negativeButtonText = builder.negativeButtonText;
        this.confirmationRequired = builder.confirmationRequired;
        this.allowedAuthenticators = builder.allowedAuthenticators;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    public boolean isConfirmationRequired() {
        return confirmationRequired;
    }

    public int getAllowedAuthenticators() {
        return allowedAuthenticators;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title = "Authentication Required";
        private String subtitle = "";
        private String description = "";
        private String negativeButtonText = "Cancel";
        private boolean confirmationRequired = true;
        private int allowedAuthenticators = AuthenticatorType.BIOMETRIC_STRONG;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setNegativeButtonText(String text) {
            this.negativeButtonText = text;
            return this;
        }

        public Builder setConfirmationRequired(boolean req) {
            this.confirmationRequired = req;
            return this;
        }

        public Builder setAllowedAuthenticators(int authenticators) {
            this.allowedAuthenticators = authenticators;
            return this;
        }

        public BiometricPromptConfig build() {
            return new BiometricPromptConfig(this);
        }
    }
}
