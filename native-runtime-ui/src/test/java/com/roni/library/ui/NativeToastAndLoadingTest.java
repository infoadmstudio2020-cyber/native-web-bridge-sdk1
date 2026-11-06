package com.roni.library.ui;

import org.junit.Assert;
import org.junit.Test;

public class NativeToastAndLoadingTest {

    @Test
    public void testNativeToastLifecycle() {
        NativeToast toast = new NativeToast();
        Assert.assertFalse(toast.isVisible());

        toast.show("Network connection restored", NativeToast.Duration.LONG, NativeToast.Type.SUCCESS);
        Assert.assertTrue(toast.isVisible());
        Assert.assertEquals("Network connection restored", toast.getMessage());
        Assert.assertEquals(NativeToast.Duration.LONG, toast.getDuration());
        Assert.assertEquals(NativeToast.Type.SUCCESS, toast.getType());

        toast.dismiss();
        Assert.assertFalse(toast.isVisible());
    }

    @Test
    public void testLoadingIndicator() {
        LoadingIndicator indicator = new LoadingIndicator();
        Assert.assertFalse(indicator.isShowing());

        indicator.show("Processing transaction...");
        Assert.assertTrue(indicator.isShowing());
        Assert.assertEquals("Processing transaction...", indicator.getMessage());

        indicator.setProgress(55);
        Assert.assertEquals(55, indicator.getProgress());

        indicator.hide();
        Assert.assertFalse(indicator.isShowing());
        Assert.assertEquals(0, indicator.getProgress());
    }
}
