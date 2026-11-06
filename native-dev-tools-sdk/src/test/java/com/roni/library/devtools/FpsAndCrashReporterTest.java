package com.roni.library.devtools;

import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class FpsAndCrashReporterTest {

    @Test
    public void testFpsMonitor() {
        FpsMonitor monitor = new FpsMonitor();
        Assert.assertFalse(monitor.isRunning());

        monitor.start();
        Assert.assertTrue(monitor.isRunning());

        monitor.updateFps(58.5);
        Assert.assertEquals(58.5, monitor.getCurrentFps(), 0.01);
        Assert.assertTrue(monitor.getFrameTimeMs() > 0);
        Assert.assertTrue(monitor.getThreadCount() > 0);

        Map<String, Object> telemetry = monitor.getTelemetrySnapshot();
        Assert.assertNotNull(telemetry);
        Assert.assertEquals(58.5, (Double) telemetry.get("fps"), 0.01);

        monitor.stop();
        Assert.assertFalse(monitor.isRunning());
    }

    @Test
    public void testCrashReporter() {
        CrashReporter reporter = new CrashReporter();
        reporter.clearCrashLogs();
        Assert.assertEquals(0, reporter.getCrashLogs().size());

        try {
            throw new IllegalArgumentException("Test artificial crash");
        } catch (Throwable t) {
            reporter.recordCrash(t);
        }

        Assert.assertEquals(1, reporter.getCrashLogs().size());
        String export = reporter.exportCrashReport();
        Assert.assertTrue(export.contains("Test artificial crash"));
        Assert.assertTrue(export.contains("IllegalArgumentException"));

        reporter.clearCrashLogs();
        Assert.assertEquals(0, reporter.getCrashLogs().size());
    }
}
