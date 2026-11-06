package com.roni.library.contracts;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented tests for {@link NativeRuntimeContracts}.
 * Run on a real device or emulator.
 *
 * @author infoadmstudio2020-cyber
 */
@RunWith(AndroidJUnit4.class)
public class NativeRuntimeContractsInstrumentedTest {

    @Test
    public void useAppContext() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.roni.library.contracts.test", ctx.getPackageName());
    }

    @Test
    public void init_withContext_succeeds() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        NativeRuntimeContractsConfig config = new NativeRuntimeContractsConfig.Builder()
            .debugEnabled(true)
            .build();
        NativeRuntimeContracts lib = NativeRuntimeContracts.getInstance();
        lib.init(ctx, config);
        assertTrue(lib.isInitialized());
        lib.release();
    }
}
