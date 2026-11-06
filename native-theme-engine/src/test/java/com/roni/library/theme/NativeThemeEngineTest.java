package com.roni.library.theme;

import com.roni.library.contracts.theme.IThemeConfig;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;

public class NativeThemeEngineTest {

    @Before
    @After
    public void tearDown() {
        NativeThemeEngine.getInstance().release();
    }

    @Test
    public void testEngineInitializationAndLifecycle() {
        NativeThemeEngine engine = NativeThemeEngine.getInstance();
        Assert.assertFalse(engine.isInitialized());

        NativeThemeEngineConfig config = NativeThemeEngineConfig.builder()
                .debugEnabled(true)
                .tag("CustomTag")
                .timeout(5000)
                .autoDetectSystemTheme(true)
                .build();

        NativeThemeEngineResult<Boolean> initResult = engine.init(null, config);
        Assert.assertTrue(initResult.isSuccess());
        Assert.assertTrue(engine.isInitialized());
        Assert.assertEquals("CustomTag", engine.getConfig().getTag());
        Assert.assertNotNull(engine.getService());
        Assert.assertNotNull(engine.getCommandHandler());

        // Test ThemeEngine static facade
        Assert.assertTrue(ThemeEngine.isInitialized());

        final AtomicBoolean observerNotified = new AtomicBoolean(false);
        ThemeEngine.addObserver(new ThemeObserver() {
            @Override
            public void onThemeChanged(IThemeConfig theme) {
                observerNotified.set(true);
            }
        });

        ThemeEngine.setThemeMode(ThemeMode.DARK);
        Assert.assertTrue(observerNotified.get());
        Assert.assertTrue(ThemeEngine.getActiveTheme().isDarkMode());

        ThemeEngine.release();
        Assert.assertFalse(ThemeEngine.isInitialized());
    }

    @Test
    public void testResultModel() {
        NativeThemeEngineResult<String> success = NativeThemeEngineResult.success("Payload");
        Assert.assertTrue(success.isSuccess());
        Assert.assertEquals("Payload", success.getData());
        Assert.assertEquals(0, success.getErrorCode());

        NativeThemeEngineResult<String> failure = NativeThemeEngineResult.failure("Error occurred", 404);
        Assert.assertFalse(failure.isSuccess());
        Assert.assertNull(failure.getData());
        Assert.assertEquals("Error occurred", failure.getErrorMessage());
        Assert.assertEquals(404, failure.getErrorCode());
    }
}
