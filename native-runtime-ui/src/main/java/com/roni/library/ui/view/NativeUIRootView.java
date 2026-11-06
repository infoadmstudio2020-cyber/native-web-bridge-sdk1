package com.roni.library.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Standard Native UI Root View coordinating SystemBars, AppBar, Content, Navigation, and Overlay layers.
 */
public class NativeUIRootView extends FrameLayout {

    private LinearLayout verticalFlowLayout;
    private FrameLayout statusBarSpacer;
    private FrameLayout appBarContainer;
    private FrameLayout contentContainer;
    private FrameLayout navigationContainer;
    private FrameLayout navigationBarSpacer;
    private FrameLayout overlayContainer;

    public NativeUIRootView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NativeUIRootView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NativeUIRootView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // Main vertical structural flow
        verticalFlowLayout = new LinearLayout(context);
        verticalFlowLayout.setOrientation(LinearLayout.VERTICAL);
        verticalFlowLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // 1. StatusBar spacer
        statusBarSpacer = new FrameLayout(context);
        statusBarSpacer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        verticalFlowLayout.addView(statusBarSpacer);

        // 2. AppBar container
        appBarContainer = new FrameLayout(context);
        appBarContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        verticalFlowLayout.addView(appBarContainer);

        // 3. Central Content container (weight=1)
        contentContainer = new FrameLayout(context);
        LinearLayout.LayoutParams contentLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f);
        contentContainer.setLayoutParams(contentLp);
        verticalFlowLayout.addView(contentContainer);

        // 4. Navigation container (bottom navigation/rail)
        navigationContainer = new FrameLayout(context);
        navigationContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        verticalFlowLayout.addView(navigationContainer);

        // 5. NavigationBar spacer
        navigationBarSpacer = new FrameLayout(context);
        navigationBarSpacer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        verticalFlowLayout.addView(navigationBarSpacer);

        addView(verticalFlowLayout);

        // 6. Top-level Overlay container for modal backdrops, dialogs, toasts, snackbars, and FABs
        overlayContainer = new FrameLayout(context);
        overlayContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(overlayContainer);
    }

    @NonNull
    public FrameLayout getStatusBarSpacer() { return statusBarSpacer; }

    @NonNull
    public FrameLayout getAppBarContainer() { return appBarContainer; }

    @NonNull
    public FrameLayout getContentContainer() { return contentContainer; }

    @NonNull
    public FrameLayout getNavigationContainer() { return navigationContainer; }

    @NonNull
    public FrameLayout getNavigationBarSpacer() { return navigationBarSpacer; }

    @NonNull
    public FrameLayout getOverlayContainer() { return overlayContainer; }

    public void setConsumerContent(@NonNull View contentView) {
        contentContainer.removeAllViews();
        contentContainer.addView(contentView);
    }
}
