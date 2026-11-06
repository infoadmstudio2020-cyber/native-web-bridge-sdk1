package com.roni.library.devtools;

import com.roni.library.devtools.profiler.MemoryMetrics;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class MemoryMetricsTest {

    @Test
    public void testMemoryMetricsCapture() {
        MemoryMetrics metrics = MemoryMetrics.capture();
        Assert.assertNotNull(metrics);
        Assert.assertTrue(metrics.getTotalHeapBytes() > 0);
        Assert.assertTrue(metrics.getFreeHeapBytes() >= 0);
        Assert.assertTrue(metrics.getUsedHeapBytes() >= 0);

        Map<String, Object> map = metrics.toMap();
        Assert.assertNotNull(map);
        Assert.assertTrue(map.containsKey("usedHeapBytes"));
        Assert.assertTrue(map.containsKey("totalHeapBytes"));
        Assert.assertTrue(map.containsKey("timestamp"));
    }
}
