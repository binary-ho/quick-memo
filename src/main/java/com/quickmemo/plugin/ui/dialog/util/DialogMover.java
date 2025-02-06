package com.quickmemo.plugin.ui.dialog.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;

import javax.swing.*;
import java.awt.*;

public class DialogMover {
    private static final String TOOL_WINDOW_ID = "QuickMemo";

    static void moveDialogOnCenterOfToolWindow(Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project)
                .getToolWindow(TOOL_WINDOW_ID);
        if (toolWindow == null || !toolWindow.isVisible()) {
            return;
        }

        Content content = toolWindow.getContentManager().getSelectedContent();
        if (content == null) {
            return;
        }

        JComponent component = content.getComponent();
        Window window = SwingUtilities.getWindowAncestor(component);

        if (window == null) {
            return;
        }

        EventQueue.invokeLater(() -> {
            Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
            if (activeWindow == null || activeWindow == window) {
                return;
            }

            setDialogOnCenterOfWindow(window, activeWindow);
        });
    }

    private static void setDialogOnCenterOfWindow(Window window, Window activeWindow) {
        Rectangle bounds = window.getBounds();
        Point location = window.getLocationOnScreen();

        // 툴 윈도우의 중앙 좌표 계산
        int centerX = location.x + (bounds.width / 2);
        int centerY = location.y + (bounds.height / 2);

        activeWindow.pack();
        int dialogWidth = activeWindow.getWidth();
        int dialogHeight = activeWindow.getHeight();

        // 최종 위치 설정
        activeWindow.setLocation(
            centerX - (dialogWidth / 2),
            centerY - (dialogHeight / 2)
        );

        // 툴 윈도우 영역 내에 있는지 확인하고 조정
        if (activeWindow.getX() < location.x) {
            activeWindow.setLocation(location.x, activeWindow.getY());
        }

        if (activeWindow.getY() < location.y) {
            activeWindow.setLocation(activeWindow.getX(), location.y);
        }

        if (activeWindow.getX() + dialogWidth > location.x + bounds.width) {
            activeWindow.setLocation(location.x + bounds.width - dialogWidth, activeWindow.getY());
        }

        if (activeWindow.getY() + dialogHeight > location.y + bounds.height) {
            activeWindow.setLocation(activeWindow.getX(), location.y + bounds.height - dialogHeight);
        }
    }
}
