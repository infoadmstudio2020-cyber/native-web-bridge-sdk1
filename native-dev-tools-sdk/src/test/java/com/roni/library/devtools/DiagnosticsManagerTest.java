package com.roni.library.devtools;

import com.roni.library.devtools.diagnostics.DiagnosticsManager;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class DiagnosticsManagerTest {

    @Test
    public void testDiagnosticsMetrics() {
        DiagnosticsManager manager = new DiagnosticsManager();

        manager.recordBridgeInvocation("user.profile", 50, true);
        manager.recordBridgeInvocation("user.profile", 70, true);
        manager.recordBridgeInvocation("user.checkout", 120, false);

        Map<String, Object> health = manager.collectServiceHealth();
        Assert.assertEquals(3L, health.get("totalCalls"));
        Assert.assertEquals(1.0 / 3.0, (Double) health.get("errorRate"), 0.01);

        Map<String, Object> bridge = manager.collectBridgeMetrics();
        Assert.assertEquals(3L, bridge.get("totalInvocations"));
        Assert.assertEquals(2L, bridge.get("successfulInvocations"));
        Assert.assertEquals(1L, bridge.get("failedInvocations"));

        Map<String, Object> manifest = manager.generatePlatformManifest();
        Assert.assertEquals("NativeDevToolsSDK", manifest.get("sdkName"));
        Assert.assertEquals("1.0.0", manifest.get("sdkVersion"));
    }
}
