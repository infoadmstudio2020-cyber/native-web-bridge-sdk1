package com.roni.library.theme;

import com.roni.library.contracts.theme.IThemeConfig;

/**
 * Generates CSS variable style injection scripts for WebView synchronization.
 */
public class WebThemeBridge {

    /**
     * Converts an IThemeConfig into a CSS string containing root variables.
     */
    public static String generateCssVariables(IThemeConfig theme) {
        if (theme == null) {
            theme = ThemePalette.defaultLight();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(":root {");
        sb.append("--theme-name: '").append(theme.getThemeName()).append("';");
        sb.append("--theme-dark-mode: ").append(theme.isDarkMode()).append(";");
        sb.append("--color-primary: ").append(theme.getPrimaryColor()).append(";");
        sb.append("--color-surface: ").append(theme.getSurfaceColor()).append(";");
        sb.append("--color-text: ").append(theme.getTextColor()).append(";");

        if (theme instanceof ThemePalette) {
            ThemePalette p = (ThemePalette) theme;
            sb.append("--color-secondary: ").append(p.getSecondaryColor()).append(";");
            sb.append("--color-background: ").append(p.getBackgroundColor()).append(";");
            sb.append("--color-text-secondary: ").append(p.getTextSecondaryColor()).append(";");
            sb.append("--color-accent: ").append(p.getAccentColor()).append(";");
            sb.append("--color-status-bar: ").append(p.getStatusBarColor()).append(";");
            sb.append("--color-nav-bar: ").append(p.getNavigationBarColor()).append(";");

            for (java.util.Map.Entry<String, String> entry : p.getCustomTokens().entrySet()) {
                sb.append("--").append(entry.getKey()).append(": ").append(entry.getValue()).append(";");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Generates a JavaScript snippet that dynamically injects or updates
     * the :root CSS variables into a Web page.
     */
    public static String generateInjectionScript(IThemeConfig theme) {
        String css = generateCssVariables(theme).replace("'", "\\'");
        return "(function() {" +
                "  var styleId = '__native_theme_engine_styles__';" +
                "  var styleEl = document.getElementById(styleId);" +
                "  if (!styleEl) {" +
                "    styleEl = document.createElement('style');" +
                "    styleEl.id = styleId;" +
                "    document.head.appendChild(styleEl);" +
                "  }" +
                "  styleEl.textContent = '" + css + "';" +
                "  window.dispatchEvent(new CustomEvent('nativeThemeChanged', {" +
                "    detail: { darkMode: " + theme.isDarkMode() + ", themeName: '" + theme.getThemeName() + "' }" +
                "  }));" +
                "})();";
    }
}
