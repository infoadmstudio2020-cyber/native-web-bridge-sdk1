package com.roni.library.devtools;

import com.roni.library.devtools.profiler.PerformanceProfiler;
import com.roni.library.devtools.profiler.PerformanceSpan;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class PerformanceSpanAndProfilerTest {

    @Test
    public void testSpanLifecycle() {
        PerformanceSpan span = new PerformanceSpan("dbQuery", "database", 1000);
        Assert.assertEquals("dbQuery", span.getName());
        Assert.assertEquals("database", span.getCategory());
        Assert.assertEquals(1000, span.getStartTimeUs());

        span.addMetadata("rows", 42);
        Assert.assertEquals(42, span.getMetadata().get("rows"));

        span.stop(1500);
        Assert.assertEquals(500, span.getDurationUs());

        Map<String, Object> map = span.toMap();
        Assert.assertEquals(500L, map.get("durationUs"));
    }

    @Test
    public void testProfilerRecording() {
        PerformanceProfiler profiler = new PerformanceProfiler();
        Assert.assertFalse(profiler.isRecording());

        profiler.startRecording();
        Assert.assertTrue(profiler.isRecording());

        profiler.updateMetrics(15.5, 59.8, 16.7);
        PerformanceSpan span = profiler.startSpan("renderFrame", "ui");
        span.stop(System.nanoTime() / 1000L);
        profiler.recordSpan(span);

        Assert.assertEquals(1, profiler.getRecordedSpans().size());

        Map<String, Object> snapshot = profiler.getMetricsSnapshot();
        Assert.assertEquals(15.5, (Double) snapshot.get("cpuUsage"), 0.01);
        Assert.assertEquals(59.8, (Double) snapshot.get("currentFps"), 0.01);

        profiler.stopRecording();
        Assert.assertFalse(profiler.isRecording());

        profiler.clear();
        Assert.assertEquals(0, profiler.getRecordedSpans().size());
    }
}
