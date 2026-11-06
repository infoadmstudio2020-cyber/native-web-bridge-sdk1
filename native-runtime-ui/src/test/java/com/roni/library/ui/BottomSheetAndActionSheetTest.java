package com.roni.library.ui;

import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;

public class BottomSheetAndActionSheetTest {

    @Test
    public void testCustomBottomSheet() {
        CustomBottomSheet sheet = new CustomBottomSheet();
        Assert.assertFalse(sheet.isVisible());

        sheet.setTitle("Select Payment Method");
        Assert.assertEquals("Select Payment Method", sheet.getTitle());

        sheet.setExpanded(true);
        Assert.assertTrue(sheet.isExpanded());

        sheet.show();
        Assert.assertTrue(sheet.isVisible());

        sheet.dismiss();
        Assert.assertFalse(sheet.isVisible());
        Assert.assertFalse(sheet.isExpanded());
    }

    @Test
    public void testNativeActionSheet() {
        NativeActionSheet actionSheet = new NativeActionSheet();
        actionSheet.setTitle("Choose Action");
        Assert.assertEquals("Choose Action", actionSheet.getTitle());

        final AtomicBoolean ran = new AtomicBoolean(false);
        actionSheet.addAction("Delete Item", true, new Runnable() {
            @Override
            public void run() {
                ran.set(true);
            }
        });
        actionSheet.addAction("Cancel", false, null);

        Assert.assertEquals(2, actionSheet.getActionCount());
        Assert.assertTrue(actionSheet.getActions().get(0).isDestructive());

        actionSheet.getActions().get(0).execute();
        Assert.assertTrue(ran.get());

        actionSheet.show();
        Assert.assertTrue(actionSheet.isVisible());

        actionSheet.clear();
        Assert.assertEquals(0, actionSheet.getActionCount());
        Assert.assertFalse(actionSheet.isVisible());
    }
}
