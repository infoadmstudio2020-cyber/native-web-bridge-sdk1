package com.roni.library.ui;

import com.roni.library.ui.dialog.DialogManager;
import com.roni.library.ui.overlay.LoadingOverlayManager;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;

public class DialogAndOverlayManagerTest {

    @Test
    public void testDialogManagerAlertAndConfirm() {
        DialogManager manager = new DialogManager();
        Assert.assertFalse(manager.isDialogShowing());

        final AtomicBoolean confirmed = new AtomicBoolean(false);
        final AtomicBoolean cancelled = new AtomicBoolean(false);

        manager.showConfirm("Confirm Action", "Do you want to proceed?", new Runnable() {
            @Override
            public void run() {
                confirmed.set(true);
            }
        }, new Runnable() {
            @Override
            public void run() {
                cancelled.set(true);
            }
        });

        Assert.assertTrue(manager.isDialogShowing());
        Assert.assertEquals("Confirm Action", manager.getCurrentTitle());
        Assert.assertEquals("Do you want to proceed?", manager.getCurrentMessage());

        manager.confirm();
        Assert.assertTrue(confirmed.get());
        Assert.assertFalse(manager.isDialogShowing());

        manager.showConfirm("Second Confirm", "Cancel test", null, new Runnable() {
            @Override
            public void run() {
                cancelled.set(true);
            }
        });
        manager.cancel();
        Assert.assertTrue(cancelled.get());
    }

    @Test
    public void testLoadingOverlayManager() {
        LoadingOverlayManager overlay = new LoadingOverlayManager();
        Assert.assertFalse(overlay.isShowing());

        overlay.show("Synchronizing assets...");
        Assert.assertTrue(overlay.isShowing());
        Assert.assertEquals("Synchronizing assets...", overlay.getText());

        overlay.hide();
        Assert.assertFalse(overlay.isShowing());
    }
}
