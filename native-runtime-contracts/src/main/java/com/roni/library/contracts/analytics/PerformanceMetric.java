package com.roni.library.contracts.analytics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;

/**
 * Universal performance metric data model.
 *
 * @author infoadmstudio2020-cyber
 */
public final class PerformanceMetric implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String metricName;
    private final double value;
    private final String unit;
    private final long timestamp;

    public PerformanceMetric(@NonNull String metricName, double value, @Nullable String unit, long timestamp) {
        if (metricName == null || metricName.trim().isEmpty()) {
            throw new IllegalArgumentException("Metric name cannot be null or empty");
        }
        this.metricName = metricName;
        this.value = value;
        this.unit = unit != null ? unit : "ms";
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
    }

    public PerformanceMetric(@NonNull String metricName, double value, @Nullable String unit) {
        this(metricName, value, unit, System.currentTimeMillis());
    }

    public PerformanceMetric(@NonNull String metricName, double value) {
        this(metricName, value, "ms", System.currentTimeMillis());
    }

    @NonNull
    public String getMetricName() {
        return metricName;
    }

    public double getValue() {
        return value;
    }

    @NonNull
    public String getUnit() {
        return unit;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "PerformanceMetric{metricName='" + metricName + "', value=" + value + ", unit='" + unit + "', timestamp=" + timestamp + "}";
    }
}
