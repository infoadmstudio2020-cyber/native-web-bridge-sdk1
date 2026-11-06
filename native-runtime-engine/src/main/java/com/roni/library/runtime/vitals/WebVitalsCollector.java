package com.roni.library.runtime.vitals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Collects and reports Web Vitals performance metrics (FP, FCP, LCP, DOM Ready, Load).
 */
public class WebVitalsCollector {

    private final AtomicLong firstPaintMs = new AtomicLong(0);
    private final AtomicLong firstContentfulPaintMs = new AtomicLong(0);
    private final AtomicLong largestContentfulPaintMs = new AtomicLong(0);
    private final AtomicLong domReadyMs = new AtomicLong(0);
    private final AtomicLong pageLoadTimeMs = new AtomicLong(0);

    public WebVitalsCollector() {}

    public void recordFp(long ms) {
        firstPaintMs.set(ms);
    }

    public void recordFcp(long ms) {
        firstContentfulPaintMs.set(ms);
    }

    public void recordLcp(long ms) {
        largestContentfulPaintMs.set(ms);
    }

    public void recordDomReady(long ms) {
        domReadyMs.set(ms);
    }

    public void recordLoadTime(long ms) {
        pageLoadTimeMs.set(ms);
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("fpMs", firstPaintMs.get());
        metrics.put("fcpMs", firstContentfulPaintMs.get());
        metrics.put("lcpMs", largestContentfulPaintMs.get());
        metrics.put("domReadyMs", domReadyMs.get());
        metrics.put("pageLoadMs", pageLoadTimeMs.get());
        return metrics;
    }

    public void reset() {
        firstPaintMs.set(0);
        firstContentfulPaintMs.set(0);
        largestContentfulPaintMs.set(0);
        domReadyMs.set(0);
        pageLoadTimeMs.set(0);
    }
}
