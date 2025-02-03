package com.quickmemo.plugin.ui.window;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import org.jetbrains.annotations.NotNull;

public class OpenWindowListener {
    public static ToolWindowManagerListener create(String windowName, Runnable runnable) {
        return new ToolWindowManagerListener() {
            @Override
            public void toolWindowShown(@NotNull ToolWindow shownWindow) {
                if (isNotEquals(shownWindow.getId(), windowName)) {
                    return;
                }

                runnable.run();
            }
        };
    }

    private static boolean isNotEquals(String shownWindow, String mainWindowName) {
        return !shownWindow.equals(mainWindowName);
    }
}
