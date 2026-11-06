package com.roni.library.sdk.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.roni.library.contracts.ui.AppBar;
import com.roni.library.contracts.ui.Content;
import com.roni.library.contracts.ui.Dialog;
import com.roni.library.contracts.ui.Feedback;
import com.roni.library.contracts.ui.Lifecycle;
import com.roni.library.contracts.ui.Navigation;
import com.roni.library.contracts.ui.Overlay;
import com.roni.library.contracts.ui.SystemUI;
import com.roni.library.contracts.ui.Theme;
import com.roni.library.contracts.ui.WebHost;
import com.roni.library.sdk.LifecycleObserver;
import com.roni.library.sdk.NativeRuntime;
import com.roni.library.sdk.RuntimeSession;
import com.roni.library.ui.NativeRuntimeUI;
import com.roni.library.ui.NativeRuntimeUIConfig;
import com.roni.library.ui.host.NativeUIHost;
import com.roni.library.ui.view.NativeUIRootView;

/**
 * Standard host Activity tying Activity lifecycle events directly into NativeRuntimeSDK
 * and acting as the single consumer entry point for declarative UI configuration.
 */
public class NativeRuntimeActivity extends AppCompatActivity implements LifecycleObserver {

    protected NativeRuntime runtime;
    protected NativeRuntimeUI ui;
    protected NativeRuntimeUIConfig uiConfig;
    protected NativeUIHost uiHost;
    protected NativeUIRootView uiRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Initialize Master Runtime
        runtime = NativeRuntime.getInstance();
        if (!runtime.isInitialized()) {
            runtime.init(this);
        }
        runtime.addObserver(this);

        // 2. Obtain Consumer UI Configuration
        NativeRuntimeUIConfig.Builder builder = NativeRuntimeUIConfig.builder();
        uiConfig = onConfigureUI(builder);
        if (uiConfig == null) {
            uiConfig = builder.build();
        }

        // 3. Initialize Modular UI Framework
        ui = NativeRuntimeUI.getInstance();
        if (!ui.isInitialized()) {
            ui.init(this, uiConfig);
        }
        uiHost = ui.getUiHost();

        // 4. Attach and Compose NativeUIRootView
        uiRootView = uiHost.attachRootView(this);
        super.setContentView(uiRootView);

        // 5. Apply Ecosystem SystemUI Configuration
        applySystemUIConfiguration();

        // 6. Restore UI State if present
        if (savedInstanceState != null && uiConfig.isComponentEnabled(Lifecycle.STATE_RESTORATION)) {
            uiHost.getStateManager().restoreAllStates(savedInstanceState);
        }
    }

    /**
     * Override this method in your Activity to declaratively configure the UI.
     * Configuration priority: Individual Component Override > Category Default > Global Default.
     *
     * @param builder Fluent NativeRuntimeUIConfig builder
     * @return Configured NativeRuntimeUIConfig object
     */
    protected NativeRuntimeUIConfig onConfigureUI(NativeRuntimeUIConfig.Builder builder) {
        // Default base enablement
        return builder
                .enable(SystemUI.STATUS_BAR)
                .enable(SystemUI.NAVIGATION_BAR)
                .enable(AppBar.TOOLBAR)
                .enable(Content.CONTENT_CONTAINER)
                .enable(Feedback.TOAST)
                .build();
    }

    private void applySystemUIConfiguration() {
        if (uiConfig == null) return;

        Window window = getWindow();
        if (window == null) return;

        // Keep Screen On
        if (uiConfig.isComponentEnabled(SystemUI.KEEP_SCREEN_ON)) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        // Edge To Edge
        if (uiConfig.isComponentEnabled(SystemUI.EDGE_TO_EDGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false);
            } else {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                );
            }
        }

        // Full Screen & Immersive Mode
        if (uiConfig.isComponentEnabled(SystemUI.FULL_SCREEN) || uiConfig.isComponentEnabled(SystemUI.IMMERSIVE_MODE)) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                );
            }
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (uiRootView != null) {
            View contentView = LayoutInflater.from(this).inflate(layoutResID, uiRootView.getContentContainer(), false);
            uiRootView.setConsumerContent(contentView);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (uiRootView != null) {
            uiRootView.setConsumerContent(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (uiRootView != null) {
            view.setLayoutParams(params);
            uiRootView.setConsumerContent(view);
        } else {
            super.setContentView(view, params);
        }
    }

    @NonNull
    public NativeUIRootView getUIRootView() {
        return uiRootView;
    }

    @NonNull
    public NativeUIHost getUIHost() {
        return uiHost;
    }

    @NonNull
    public NativeRuntimeUIConfig getUIConfig() {
        return uiConfig;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (uiHost != null && uiConfig != null && uiConfig.isComponentEnabled(Lifecycle.STATE_RESTORATION)) {
            uiHost.getStateManager().saveAllStates(outState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (runtime != null && runtime.isInitialized()) {
            runtime.resumeSession();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (runtime != null && runtime.isInitialized()) {
            runtime.pauseSession();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runtime != null) {
            runtime.removeObserver(this);
        }
    }

    @Override
    public void onInitialized(NativeRuntime runtime) {}

    @Override
    public void onSessionStarted(RuntimeSession session) {}

    @Override
    public void onSessionPaused(RuntimeSession session) {}

    @Override
    public void onSessionResumed(RuntimeSession session) {}

    @Override
    public void onSessionStopped(RuntimeSession session) {}

    @Override
    public void onReleased() {}

    @Override
    public void onError(int errorCode, String errorMessage) {}
}
