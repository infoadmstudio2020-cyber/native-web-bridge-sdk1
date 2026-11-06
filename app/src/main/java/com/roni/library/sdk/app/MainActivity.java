package com.roni.library.sdk.app;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.roni.library.contracts.ui.AppBar;
import com.roni.library.contracts.ui.Content;
import com.roni.library.contracts.ui.Dialog;
import com.roni.library.contracts.ui.Feedback;
import com.roni.library.contracts.ui.Navigation;
import com.roni.library.contracts.ui.Overlay;
import com.roni.library.contracts.ui.SystemUI;
import com.roni.library.contracts.ui.UICategory;
import com.roni.library.sdk.activity.NativeRuntimeActivity;
import com.roni.library.ui.NativeRuntimeUIConfig;

import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.sdk.LifecycleObserver;
import com.roni.library.sdk.NativeRuntime;
import com.roni.library.sdk.NativeRuntimeResult;
import com.roni.library.sdk.RuntimeBuilder;
import com.roni.library.sdk.RuntimeConfiguration;
import com.roni.library.sdk.RuntimeSession;
import com.roni.library.sdk.bootstrap.NativeRuntimeBootstrap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Universal Interactive Companion Demonstration App for NativeRuntimeSDK.
 * Implements Dashboard, 10-Step Boot Visualizer, Command Center, Test Center, and Live Console.
 */
public class MainActivity extends NativeRuntimeActivity {

    @Override
    protected NativeRuntimeUIConfig onConfigureUI(NativeRuntimeUIConfig.Builder builder) {
        return builder
                .enable(SystemUI.STATUS_BAR)
                .enable(SystemUI.EDGE_TO_EDGE)
                .enable(AppBar.TOOLBAR)
                .enable(Navigation.BOTTOM_NAVIGATION)
                .enable(Content.CONTENT_CONTAINER)
                .enable(Dialog.ALERT_DIALOG)
                .enable(Feedback.TOAST)
                .enable(Overlay.LOADING_OVERLAY)
                .disable(Overlay.FLOATING_MENU)
                .build();
    }

    private TextView tvRuntimeState;
    private TextView tvSessionInfo;
    private TextView tvBootstrapHealth;
    private TextView tvBootstrapSteps;
    private TextView tvCommandOutput;
    private TextView tvTestSummary;
    private TextView tvConsoleLog;
    private Button btnToggleSession;

    private NativeRuntime runtime;
    private final StringBuilder logBuffer = new StringBuilder();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setupListeners();
        initMasterRuntime();
    }

    private void bindViews() {
        tvRuntimeState = findViewById(R.id.tv_runtime_state);
        tvSessionInfo = findViewById(R.id.tv_session_info);
        tvBootstrapHealth = findViewById(R.id.tv_bootstrap_health);
        tvBootstrapSteps = findViewById(R.id.tv_bootstrap_steps);
        tvCommandOutput = findViewById(R.id.tv_command_output);
        tvTestSummary = findViewById(R.id.tv_test_summary);
        tvConsoleLog = findViewById(R.id.tv_console_log);
        if (tvConsoleLog != null) {
            tvConsoleLog.setMovementMethod(new ScrollingMovementMethod());
        }
        btnToggleSession = findViewById(R.id.btn_toggle_session);
    }

    private void setupListeners() {
        findViewById(R.id.btn_reboot_sequence).setOnClickListener(v -> rebootSequence());
        findViewById(R.id.btn_dispatch_rpc).setOnClickListener(v -> dispatchRpcCommand());
        findViewById(R.id.btn_track_telemetry).setOnClickListener(v -> trackTelemetrySample());
        findViewById(R.id.btn_toggle_session).setOnClickListener(v -> toggleSessionState());
        findViewById(R.id.btn_run_tests).setOnClickListener(v -> runMasterTestSuite());
        findViewById(R.id.btn_clear_log).setOnClickListener(v -> {
            logBuffer.setLength(0);
            if (tvConsoleLog != null) tvConsoleLog.setText("");
        });
    }

    private void log(String message) {
        String entry = "[" + timeFormat.format(new Date()) + "] " + message + "\n";
        logBuffer.append(entry);
        if (tvConsoleLog != null) {
            tvConsoleLog.setText(logBuffer.toString());
        }
    }

    private void initMasterRuntime() {
        runtime = NativeRuntime.getInstance();
        runtime.addObserver(this);

        RuntimeConfiguration config = RuntimeConfiguration.builder()
                .appId("com.roni.app.masterdemo")
                .environment("PRODUCTION")
                .debugEnabled(true)
                .logLevel("VERBOSE")
                .storageEncryptionEnabled(true)
                .networkTimeoutMs(15000)
                .maxNetworkRetries(3)
                .telemetryEnabled(true)
                .addCustomProperty("region", "GLOBAL")
                .build();

        NativeRuntimeResult<Boolean> result = runtime.init(this, config);
        log("NativeRuntime initialization result: " + result);

        updateDashboard();
        renderBootstrapSteps();
    }

    private void updateDashboard() {
        if (runtime == null) return;

        RuntimeState state = runtime.getState();
        if (tvRuntimeState != null) {
            tvRuntimeState.setText("State: " + state + " (Healthy: " + runtime.isHealthy() + ")");
        }

        RuntimeSession session = runtime.getSession();
        if (session != null) {
            String sInfo = "Session: " + session.getSessionId().substring(0, 8) + "… • State: " +
                    session.getState() + " • Duration: " + (session.getDurationMs() / 1000) + "s";
            if (tvSessionInfo != null) tvSessionInfo.setText(sInfo);
            if (btnToggleSession != null) {
                btnToggleSession.setText(session.getState() == RuntimeSession.State.ACTIVE ? "Pause Session" : "Resume Session");
            }
        }

        NativeRuntimeBootstrap.BootstrapReport report = runtime.getBootstrapReport();
        if (report != null && tvBootstrapHealth != null) {
            String bHealth = "Bootstrap: " + report.getPassedCount() + "/" + report.getSteps().size() +
                    " Steps Healthy (" + report.getTotalDurationMs() + "ms total)";
            tvBootstrapHealth.setText(bHealth);
        }
    }

    private void renderBootstrapSteps() {
        if (runtime == null) return;
        NativeRuntimeBootstrap.BootstrapReport report = runtime.getBootstrapReport();
        if (report == null) return;

        StringBuilder sb = new StringBuilder();
        for (NativeRuntimeBootstrap.StepReport s : report.getSteps()) {
            sb.append(s.isSuccess() ? "✓ " : "✗ ")
                    .append(String.format(Locale.US, "Step %02d [%s]: %s (%dms)\n  -> %s\n",
                            s.getStepNumber(), s.getLayer(), s.getStepName(), s.getDurationMs(), s.getDetails()));
        }

        if (tvBootstrapSteps != null) {
            tvBootstrapSteps.setText(sb.toString());
        }
    }

    private void rebootSequence() {
        log("Re-executing turnkey 10-step bootstrap sequence...");
        runtime.release();
        initMasterRuntime();
        log("Turnkey bootstrap completed.");
    }

    private void dispatchRpcCommand() {
        Map<String, Object> params = new HashMap<>();
        params.put("caller", "MainActivity");
        params.put("timestamp", System.currentTimeMillis());

        NativeApiRequest request = new NativeApiRequest("req-" + System.currentTimeMillis(), "runtime", "ping", params, System.currentTimeMillis());
        log("Dispatching RPC request: " + request.getPath() + "/" + request.getAction());

        NativeApiResponse response = runtime.executeApiRequest(request);
        String out = "RPC Result: success=" + response.isSuccess() + ", data=" + response.getData();
        if (tvCommandOutput != null) tvCommandOutput.setText(out);
        log(out);
    }

    private void trackTelemetrySample() {
        Map<String, Object> props = new HashMap<>();
        props.put("interaction", "button_click");
        props.put("target", "trackTelemetry");

        runtime.trackEvent("demo_user_action", props);
        String out = "Telemetry recorded. Total stored events: " + runtime.getTelemetryEvents().size();
        if (tvCommandOutput != null) tvCommandOutput.setText(out);
        log(out);
    }

    private void toggleSessionState() {
        RuntimeSession session = runtime.getSession();
        if (session != null) {
            if (session.getState() == RuntimeSession.State.ACTIVE) {
                runtime.pauseSession();
                log("Session paused by user.");
            } else {
                runtime.resumeSession();
                log("Session resumed by user.");
            }
            updateDashboard();
        }
    }

    private void runMasterTestSuite() {
        log("Executing Comprehensive NativeRuntimeSDK Test Suite...");
        StringBuilder sb = new StringBuilder();
        int pass = 0, fail = 0;

        // 1. Singleton & Initialization
        try {
            if (runtime.isInitialized() && runtime.isHealthy()) {
                sb.append("✓ [PASS] Master Singleton Lifecycle & Health\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] Master Singleton is unhealthy\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] Singleton: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 2. Configuration Bounds
        try {
            RuntimeConfiguration cfg = RuntimeConfiguration.builder()
                    .appId("test.app").networkTimeoutMs(20000).maxNetworkRetries(4).build();
            if ("test.app".equals(cfg.getAppId()) && cfg.getNetworkTimeoutMs() == 20000 && cfg.getMaxNetworkRetries() == 4) {
                sb.append("✓ [PASS] RuntimeConfiguration Bounds & Defaults\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] Configuration bounds mismatch\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] Config: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 3. Fluent RuntimeBuilder
        try {
            NativeRuntime rt = RuntimeBuilder.with(this).appId("fluent.test").initialize();
            if (rt.isInitialized() && "fluent.test".equals(rt.getConfiguration().getAppId())) {
                sb.append("✓ [PASS] Fluent RuntimeBuilder Execution\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] RuntimeBuilder mismatch\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] RuntimeBuilder: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 4. 10-Step Turnkey Bootstrap
        try {
            NativeRuntimeBootstrap.BootstrapReport rep = NativeRuntimeBootstrap.execute(this, runtime.getConfiguration());
            if (rep.isOverallSuccess() && rep.getPassedCount() == 10 && rep.getSteps().size() == 10) {
                sb.append("✓ [PASS] 10-Step Turnkey Bootstrap Pipeline (L0-L4)\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] Turnkey Bootstrap failed\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] Bootstrap: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 5. RuntimeSession State Transitions
        try {
            runtime.pauseSession();
            boolean paused = runtime.getSession().getState() == RuntimeSession.State.PAUSED;
            runtime.resumeSession();
            boolean active = runtime.getSession().getState() == RuntimeSession.State.ACTIVE;
            if (paused && active) {
                sb.append("✓ [PASS] RuntimeSession State Machine (Pause/Resume)\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] RuntimeSession state machine failed\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] Session: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 6. LifecycleObserver Callback Loop
        try {
            final AtomicBoolean notified = new AtomicBoolean(false);
            LifecycleObserver obs = new LifecycleObserver() {
                @Override public void onInitialized(NativeRuntime r) {}
                @Override public void onSessionStarted(RuntimeSession s) {}
                @Override public void onSessionPaused(RuntimeSession s) { notified.set(true); }
                @Override public void onSessionResumed(RuntimeSession s) {}
                @Override public void onSessionStopped(RuntimeSession s) {}
                @Override public void onReleased() {}
                @Override public void onError(int c, String m) {}
            };
            runtime.addObserver(obs);
            runtime.pauseSession();
            runtime.resumeSession();
            runtime.removeObserver(obs);
            if (notified.get()) {
                sb.append("✓ [PASS] LifecycleObserver Event Bus\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] LifecycleObserver failed to receive event\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] LifecycleObserver: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 7. Standardized Result Wrapper
        try {
            NativeRuntimeResult<String> r1 = NativeRuntimeResult.success("TEST");
            NativeRuntimeResult<String> r2 = NativeRuntimeResult.failure(404, "Not Found");
            if (r1.isSuccess() && "TEST".equals(r1.getData()) && !r2.isSuccess() && r2.getErrorCode() == 404) {
                sb.append("✓ [PASS] Standardized NativeRuntimeResult Envelope\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] Result wrapper mismatch\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] Result: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 8. RPC Dispatcher
        try {
            NativeApiRequest req = new NativeApiRequest("req-test", "core", "test", null, System.currentTimeMillis());
            NativeApiResponse res = runtime.executeApiRequest(req);
            if (res != null && res.isSuccess() && "req-test".equals(res.getRequestId())) {
                sb.append("✓ [PASS] Master RPC Dispatcher Subsystem\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] RPC Dispatcher failed\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] RPC: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 9. Telemetry Ingestion
        try {
            Map<String, Object> p = new HashMap<>();
            p.put("key", "val");
            runtime.trackEvent("test_event", p);
            if (!runtime.getTelemetryEvents().isEmpty()) {
                sb.append("✓ [PASS] High-Level Telemetry Ingestion Bus\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] Telemetry ingestion failed\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] Telemetry: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 10. Memory Leak & Clean Release
        try {
            runtime.release();
            boolean cleaned = !runtime.isInitialized() && runtime.getSession() == null &&
                    runtime.getState() == RuntimeState.UNINITIALIZED;
            initMasterRuntime();
            if (cleaned) {
                sb.append("✓ [PASS] Memory Cleanup & Safe Release\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] Release state leaked\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] Release: ").append(e.getMessage()).append("\n");
            fail++;
        }

                // 11. UI Component Registry Coverage (12 Categories)
        try {
            int compCount = ui.getRegistry().getComponentCount();
            if (compCount >= 12) {
                sb.append("✓ [PASS] UI Component Registry (12 Categories, ").append(compCount).append(" Components)\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] UI Component Registry count too low: ").append(compCount).append("\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] UI Registry: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 12. Configuration Priority Rule (Override > Category > Global)
        try {
            NativeRuntimeUIConfig testCfg = NativeRuntimeUIConfig.builder()
                    .setGlobalDefault(false)
                    .enableCategory(UICategory.OVERLAY)
                    .disable(Overlay.FLOATING_MENU)
                    .build();
            boolean fabEnabled = testCfg.isComponentEnabled(Overlay.FLOATING_ACTION_BUTTON);
            boolean menuEnabled = testCfg.isComponentEnabled(Overlay.FLOATING_MENU);
            if (fabEnabled && !menuEnabled) {
                sb.append("✓ [PASS] UI Configuration Priority Rule (Override > Category > Global)\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] UI Config Priority failed (FAB=").append(fabEnabled).append(", Menu=").append(menuEnabled).append(")\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] UI Config Priority: ").append(e.getMessage()).append("\n");
            fail++;
        }

        // 13. Declarative NativeUIRootView & NativeRuntimeActivity
        try {
            if (getUIRootView() != null && getUIHost() != null && getUIConfig() != null) {
                sb.append("✓ [PASS] NativeRuntimeActivity Declarative Single Entry Point\n");
                pass++;
            } else {
                sb.append("✗ [FAIL] NativeRuntimeActivity UI host not initialized\n");
                fail++;
            }
        } catch (Exception e) {
            sb.append("✗ [FAIL] UI Entry Point: ").append(e.getMessage()).append("\n");
            fail++;
        }

        sb.append("\nSUMMARY: ").append(pass).append(" PASSED, ").append(fail).append(" FAILED.");
        if (tvTestSummary != null) {
            tvTestSummary.setText(sb.toString());
        }
        log("Master Test Suite Completed: " + pass + " passed, " + fail + " failed.");
        updateDashboard();
    }

    @Override
    public void onInitialized(NativeRuntime runtime) {
        runOnUiThread(this::updateDashboard);
    }

    @Override
    public void onSessionStarted(RuntimeSession session) {
        runOnUiThread(this::updateDashboard);
    }

    @Override
    public void onSessionPaused(RuntimeSession session) {
        runOnUiThread(this::updateDashboard);
    }

    @Override
    public void onSessionResumed(RuntimeSession session) {
        runOnUiThread(this::updateDashboard);
    }

    @Override
    public void onSessionStopped(RuntimeSession session) {
        runOnUiThread(this::updateDashboard);
    }

    @Override
    public void onReleased() {
        runOnUiThread(this::updateDashboard);
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        log("ERROR (" + errorCode + "): " + errorMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runtime != null) {
            runtime.removeObserver(this);
        }
    }
}
