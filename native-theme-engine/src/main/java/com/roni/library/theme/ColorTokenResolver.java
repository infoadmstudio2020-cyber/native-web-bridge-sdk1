package com.roni.library.theme;

import java.util.Locale;

/**
 * Resolves color tokens between Hex strings, ARGB integers, and CSS variables.
 */
public final class ColorTokenResolver {

    private ColorTokenResolver() {}

    /**
     * Parses a color string (#RGB, #RGBA, #RRGGBB, #AARRGGBB) into an ARGB 32-bit int.
     */
    public static int parseColor(Object colorObj, int defaultColor) {
        if (colorObj == null) {
            return defaultColor;
        }
        if (colorObj instanceof Number) {
            return ((Number) colorObj).intValue();
        }
        String colorString = colorObj.toString().trim();
        if (colorString.isEmpty()) {
            return defaultColor;
        }

        if (colorString.startsWith("#")) {
            colorString = colorString.substring(1);
        }

        try {
            if (colorString.length() == 3) {
                // #RGB -> #RRGGBB
                char r = colorString.charAt(0);
                char g = colorString.charAt(1);
                char b = colorString.charAt(2);
                colorString = "" + r + r + g + g + b + b;
            } else if (colorString.length() == 4) {
                // #RGBA -> #AARRGGBB
                char r = colorString.charAt(0);
                char g = colorString.charAt(1);
                char b = colorString.charAt(2);
                char a = colorString.charAt(3);
                colorString = "" + a + a + r + r + g + g + b + b;
            }

            if (colorString.length() == 6) {
                // #RRGGBB
                long rgb = Long.parseLong(colorString, 16);
                return (int) (0xFF000000L | rgb);
            } else if (colorString.length() == 8) {
                // #AARRGGBB
                return (int) Long.parseLong(colorString, 16);
            }
        } catch (NumberFormatException ignored) {}

        return defaultColor;
    }

    /**
     * Converts a 32-bit ARGB int to hex string "#AARRGGBB" or "#RRGGBB".
     */
    public static String toHexString(int color, boolean includeAlpha) {
        if (includeAlpha) {
            return String.format(Locale.US, "#%08X", color);
        } else {
            return String.format(Locale.US, "#%06X", (0xFFFFFF & color));
        }
    }

    /**
     * Adjusts the alpha channel of an ARGB int color.
     */
    public static int withAlpha(int color, float alphaRatio) {
        int a = Math.round(255 * Math.max(0.0f, Math.min(1.0f, alphaRatio)));
        return (a << 24) | (color & 0x00FFFFFF);
    }

    /**
     * Calculates the perceived contrast ratio between two ARGB colors.
     */
    public static double calculateContrastRatio(int foreground, int background) {
        double lum1 = calculateLuminance(foreground);
        double lum2 = calculateLuminance(background);
        return (Math.max(lum1, lum2) + 0.05) / (Math.min(lum1, lum2) + 0.05);
    }

    public static double calculateLuminance(int color) {
        double r = ((color >> 16) & 0xFF) / 255.0;
        double g = ((color >> 8) & 0xFF) / 255.0;
        double b = (color & 0xFF) / 255.0;

        r = (r <= 0.03928) ? r / 12.92 : Math.pow((r + 0.055) / 1.055, 2.4);
        g = (g <= 0.03928) ? g / 12.92 : Math.pow((g + 0.055) / 1.055, 2.4);
        b = (b <= 0.03928) ? b / 12.92 : Math.pow((b + 0.055) / 1.055, 2.4);

        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }
}
