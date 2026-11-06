package com.roni.library.devtools.service;

import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.service.BaseService;
import com.roni.library.service.ServiceRegistry;
import com.roni.library.devtools.diagnostics.DiagnosticsManager;
import com.roni.library.devtools.network.NetworkTrafficInspector;
import com.roni.library.devtools.profiler.PerformanceProfiler;
import com.roni.library.devtools.server.DevToolsServer;

/**
 * Layer 2 Domain Service for NativeDevToolsSDK extending BaseService.
 */
public class NativeDevToolsService extends BaseService implements com.roni.library.service.IService {

    private final DevToolsServer devToolsServer;
    private final PerformanceProfiler performanceProfiler;
    private final NetworkTrafficInspector networkTrafficInspector;
    private final DiagnosticsManager diagnosticsManager;

    public NativeDevToolsService() {
        this(new DevToolsServer(), new PerformanceProfiler(), new NetworkTrafficInspector(), new DiagnosticsManager());
    }

    public NativeDevToolsService(DevToolsServer devToolsServer,
                                PerformanceProfiler performanceProfiler,
                                NetworkTrafficInspector networkTrafficInspector,
                                DiagnosticsManager diagnosticsManager) {
        super("NativeDevToolsService", 80);
        this.devToolsServer = devToolsServer != null ? devToolsServer : new DevToolsServer();
        this.performanceProfiler = performanceProfiler != null ? performanceProfiler : new PerformanceProfiler();
        this.networkTrafficInspector = networkTrafficInspector != null ? networkTrafficInspector : new NetworkTrafficInspector();
        this.diagnosticsManager = diagnosticsManager != null ? diagnosticsManager : new DiagnosticsManager();
    }

    @Override
    protected void onInitialize() throws Exception {
        try {
            ServiceRegistry registry = ServiceRegistry.getInstance();
            if (registry != null) {
                registry.registerService(com.roni.library.service.IService.class, this);
            }
        } catch (Throwable ignored) {}
    }

    @Override
    protected void onServiceStart() throws Exception {
        devToolsServer.start();
        performanceProfiler.startRecording();
    }

    @Override
    protected void onServiceStop() throws Exception {
        devToolsServer.stop();
        performanceProfiler.stopRecording();
    }

    @Override
    protected void onDispose() throws Exception {
        devToolsServer.stop();
        performanceProfiler.clear();
        networkTrafficInspector.clear();
    }

    public DevToolsServer getDevToolsServer() {
        return devToolsServer;
    }

    public PerformanceProfiler getPerformanceProfiler() {
        return performanceProfiler;
    }

    public NetworkTrafficInspector getNetworkTrafficInspector() {
        return networkTrafficInspector;
    }

    public DiagnosticsManager getDiagnosticsManager() {
        return diagnosticsManager;
    }

    public RuntimeState getServiceState() {
        return RuntimeState.READY;
    }
}
