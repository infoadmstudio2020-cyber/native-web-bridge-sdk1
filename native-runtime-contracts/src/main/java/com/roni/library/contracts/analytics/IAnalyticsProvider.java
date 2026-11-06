package com.roni.library.contracts.analytics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Interface contract for analytics dispatchers in NativeRuntime ecosystem.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IAnalyticsProvider {

    /**
     * Dispatch an analytics event.
     *
     * @param event the event to record
     */
    void trackEvent(@NonNull AnalyticsEvent event);

    /**
     * Record a system or execution performance metric.
     *
     * @param metric the metric to record
     */
    void recordMetric(@NonNull PerformanceMetric metric);

    /**
     * Set the active user identifier for tracking.
     *
     * @param userId unique user id, or null to clear
     */
    void setUserId(@Nullable String userId);

    /**
     * Attach a persistent user property.
     *
     * @param key   property key
     * @param value property value
     */
    void setUserProperty(@NonNull String key, @Nullable String value);
}
