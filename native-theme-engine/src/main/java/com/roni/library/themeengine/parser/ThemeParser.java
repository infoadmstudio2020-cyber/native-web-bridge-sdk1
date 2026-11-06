package com.roni.library.themeengine.parser;

import com.roni.library.theme.model.ThemeConfig;
import java.util.Map;

/**
 * Forwarding alias to com.roni.library.theme.parser.ThemeParser for backward compatibility.
 */
public class ThemeParser {

    public static ThemeConfig parse(Map<String, Object> map) {
        return com.roni.library.theme.parser.ThemeParser.parse(map);
    }

    public static int parseColor(Object colorObj, int defaultColor) {
        return com.roni.library.theme.parser.ThemeParser.parseColor(colorObj, defaultColor);
    }
}
