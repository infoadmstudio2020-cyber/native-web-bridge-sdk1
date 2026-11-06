package com.roni.library.ui;

import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.contracts.ui.*;
import com.roni.library.ui.config.UIConfigurationManager;
import com.roni.library.ui.event.UIEventDispatcher;
import com.roni.library.ui.host.NativeUIHost;
import com.roni.library.ui.registry.StandardUIComponentRegistryInitializer;
import com.roni.library.ui.registry.UIComponentRegistry;
import com.roni.library.ui.state.UIStateManager;
import com.roni.library.ui.visibility.UIVisibilityManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UIComponentRegistryAndArchitectureTest {

    private NativeRuntimeUI ui;

    @Before
    public void setUp() {
        ui = NativeRuntimeUI.getInstance();
    }

    @After
    public void tearDown() {
        ui.release();
    }

    @Test
    public void testRegistryInitializationAndCategoryCoverage() {
        NativeRuntimeUIConfig config = NativeRuntimeUIConfig.builder()
                .enable(SystemUI.STATUS_BAR)
                .enable(AppBar.TOOLBAR)
                .enable(Navigation.BOTTOM_NAVIGATION)
                .enable(Content.CONTENT_CONTAINER)
                .enable(Dialog.ALERT_DIALOG)
                .enable(Feedback.TOAST)
                .enable(Overlay.LOADING_OVERLAY)
                .enable(Input.TEXT_FIELD)
                .enable(WebHost.WEB_VIEW_CONTAINER)
                .enable(Theme.THEME_CONTROLLER)
                .enable(Lifecycle.UI_STATE_CONTROLLER)
                .enable(Animation.TRANSITION_ANIMATION)
                .build();

        ui.init(null, config);

        IUIComponentRegistry registry = ui.getRegistry();
        Assert.assertNotNull(registry);

        // Verify all 12 categories are populated
        for (UICategory cat : UICategory.values()) {
            List<IUIComponentDescriptor> descs = registry.getDescriptorsByCategory(cat);
            Assert.assertFalse("Category " + cat + " must have registered descriptors", descs.isEmpty());
        }

        Assert.assertTrue(registry.getComponentCount() >= 12);
        Assert.assertTrue(registry.isRegistered(SystemUI.STATUS_BAR));
        Assert.assertTrue(registry.isRegistered(AppBar.TOOLBAR));
        Assert.assertTrue(registry.isRegistered(Overlay.LOADING_OVERLAY));
    }

    @Test
    public void testConfigurationPriorityRule() {
        // Rule: Individual Component Override > Category Default > Global Default
        NativeRuntimeUIConfig config = NativeRuntimeUIConfig.builder()
                .setGlobalDefault(false)
                .enableCategory(UICategory.OVERLAY)
                .disable(Overlay.FLOATING_MENU)
                .enable(AppBar.TOOLBAR) // Category default is unset, override is true
                .build();

        ui.init(null, config);

        IUIConfigurationManager cfg = ui.getConfigurationManager();

        // 1. Category OVERLAY is enabled, so LOADING_OVERLAY should be enabled
        Assert.assertTrue(cfg.isComponentEnabled(Overlay.LOADING_OVERLAY));
        Assert.assertTrue(config.isComponentEnabled(Overlay.LOADING_OVERLAY));

        // 2. Individual override for FLOATING_MENU is disabled, taking precedence over category default
        Assert.assertFalse(cfg.isComponentEnabled(Overlay.FLOATING_MENU));
        Assert.assertFalse(config.isComponentEnabled(Overlay.FLOATING_MENU));

        // 3. Category INPUT is unset, global default is false
        Assert.assertFalse(cfg.isComponentEnabled(Input.TEXT_FIELD));
        Assert.assertFalse(config.isComponentEnabled(Input.TEXT_FIELD));

        // 4. Individual override for TOOLBAR is enabled
        Assert.assertTrue(cfg.isComponentEnabled(AppBar.TOOLBAR));
        Assert.assertTrue(config.isComponentEnabled(AppBar.TOOLBAR));
    }

    @Test
    public void testVisibilityManagerAndListeners() {
        ui.init(null, NativeRuntimeUIConfig.defaultConfiguration());
        IUIVisibilityManager vm = ui.getVisibilityManager();

        final AtomicBoolean notified = new AtomicBoolean(false);
        UIVisibilityListener listener = (type, isVisible) -> {
            if (type.equals(AppBar.TOOLBAR) && !isVisible) {
                notified.set(true);
            }
        };

        vm.addVisibilityListener(listener);
        vm.setVisible(AppBar.TOOLBAR, false);
        Assert.assertFalse(vm.isVisible(AppBar.TOOLBAR));
        Assert.assertTrue(notified.get());

        vm.toggle(AppBar.TOOLBAR);
        Assert.assertTrue(vm.isVisible(AppBar.TOOLBAR));

        vm.removeVisibilityListener(listener);
    }

    @Test
    public void testStateManagerAndSnapshot() {
        ui.init(null, NativeRuntimeUIConfig.defaultConfiguration());
        IUIStateManager sm = ui.getStateManager();

        UIState state = sm.getComponentState(AppBar.TOOLBAR);
        Assert.assertNotNull(state);
        state.set("title", "Dashboard");
        state.set("badgeCount", 5);

        Assert.assertEquals("Dashboard", state.get("title"));
        Assert.assertEquals((Integer) 5, state.get("badgeCount", 0));
        Assert.assertTrue(state.contains("title"));
        Assert.assertFalse(state.contains("unknownKey"));
    }

    @Test
    public void testEventDispatcher() {
        ui.init(null, NativeRuntimeUIConfig.defaultConfiguration());
        IUIEventDispatcher ed = ui.getEventDispatcher();

        final AtomicBoolean eventHandled = new AtomicBoolean(false);
        ed.addEventListener("NAV_CLICKED", event -> {
            if ("NAV_CLICKED".equals(event.getEventType())) {
                eventHandled.set(true);
            }
        });

        ed.dispatchEvent(new UIEvent("NAV_CLICKED", Navigation.BOTTOM_NAVIGATION));
        Assert.assertTrue(eventHandled.get());
    }

    @Test
    public void testWebBridgeUICommandHandlerExtensions() {
        ui.init(null, NativeRuntimeUIConfig.builder().enable(AppBar.TOOLBAR).build());

        HashMap<String, Object> params = new HashMap<>();
        params.put("category", "APP_BAR");
        params.put("key", "TOOLBAR");

        NativeApiRequest req = new NativeApiRequest("req-comp", "ui", "isComponentEnabled", params, System.currentTimeMillis());
        NativeApiResponse resp = ui.getCommandHandler().handleRequest(req);
        Assert.assertTrue(resp.isSuccess());

        NativeApiRequest countReq = new NativeApiRequest("req-count", "ui", "getComponentCount", null, System.currentTimeMillis());
        NativeApiResponse countResp = ui.getCommandHandler().handleRequest(countReq);
        Assert.assertTrue(countResp.isSuccess());
    }
}
