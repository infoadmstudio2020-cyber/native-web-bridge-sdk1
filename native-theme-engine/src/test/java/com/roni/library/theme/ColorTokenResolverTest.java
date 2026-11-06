package com.roni.library.theme;

import org.junit.Assert;
import org.junit.Test;

public class ColorTokenResolverTest {

    @Test
    public void testParseColorHexFormats() {
        // #RGB
        int colorRgb = ColorTokenResolver.parseColor("#F00", 0);
        Assert.assertEquals(0xFFFF0000, colorRgb);

        // #RGBA
        int colorRgba = ColorTokenResolver.parseColor("#F00F", 0);
        Assert.assertEquals(0xFFFF0000, colorRgba);

        // #RRGGBB
        int colorRrgbb = ColorTokenResolver.parseColor("#00FF00", 0);
        Assert.assertEquals(0xFF00FF00, colorRrgbb);

        // #AARRGGBB
        int colorAarrggbb = ColorTokenResolver.parseColor("#800000FF", 0);
        Assert.assertEquals(0x800000FF, colorAarrggbb);

        // Without hash
        int noHash = ColorTokenResolver.parseColor("123456", 0);
        Assert.assertEquals(0xFF123456, noHash);
    }

    @Test
    public void testParseColorFallback() {
        int fallback = 0xFF123456;
        Assert.assertEquals(fallback, ColorTokenResolver.parseColor(null, fallback));
        Assert.assertEquals(fallback, ColorTokenResolver.parseColor("", fallback));
        Assert.assertEquals(fallback, ColorTokenResolver.parseColor("invalid_color", fallback));
        Assert.assertEquals(0xFF654321, ColorTokenResolver.parseColor(0xFF654321, fallback));
    }

    @Test
    public void testToHexString() {
        int color = 0xFF123456;
        Assert.assertEquals("#123456", ColorTokenResolver.toHexString(color, false));
        Assert.assertEquals("#FF123456", ColorTokenResolver.toHexString(color, true));
    }

    @Test
    public void testWithAlpha() {
        int color = 0x00FF0000;
        int halfAlpha = ColorTokenResolver.withAlpha(color, 0.5f);
        int alphaVal = (halfAlpha >> 24) & 0xFF;
        Assert.assertTrue(alphaVal >= 127 && alphaVal <= 128);
    }

    @Test
    public void testContrastAndLuminance() {
        int black = 0xFF000000;
        int white = 0xFFFFFFFF;

        double lumBlack = ColorTokenResolver.calculateLuminance(black);
        double lumWhite = ColorTokenResolver.calculateLuminance(white);
        Assert.assertEquals(0.0, lumBlack, 0.001);
        Assert.assertEquals(1.0, lumWhite, 0.001);

        double contrast = ColorTokenResolver.calculateContrastRatio(white, black);
        Assert.assertEquals(21.0, contrast, 0.1);
    }
}
