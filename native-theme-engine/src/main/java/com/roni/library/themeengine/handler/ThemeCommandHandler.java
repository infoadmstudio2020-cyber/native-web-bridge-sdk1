package com.roni.library.themeengine.handler;

import com.roni.library.theme.manager.ThemeManager;

/**
 * Forwarding alias to com.roni.library.theme.handler.ThemeCommandHandler for backward compatibility.
 */
public class ThemeCommandHandler extends com.roni.library.theme.handler.ThemeCommandHandler {

    public ThemeCommandHandler(ThemeManager themeManager) {
        super(themeManager);
    }
}
