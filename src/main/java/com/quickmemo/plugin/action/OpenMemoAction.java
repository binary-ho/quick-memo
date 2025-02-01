package com.quickmemo.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.quickmemo.plugin.window.component.MemoEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class OpenMemoAction extends AnAction {

    public static final String WINDOW_NAME = "QuickMemo";

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        toggleWindow(project);
    }

    private void toggleWindow(Project project) {
        ToolWindow window = getWindow(project);
        if (window == null) {
            return;
        }

        if (window.isVisible()) {
            window.hide();
            return;
        }

        project.getMessageBus().connect().subscribe(
                ToolWindowManagerListener.TOPIC,
                getWindowManagerListener()
        );
        window.show(null);
    }

    private ToolWindowManagerListener getWindowManagerListener() {
        return new ToolWindowManagerListener() {
            @Override
            public void toolWindowShown(@NotNull ToolWindow shownWindow) {
                if (!WINDOW_NAME.equals(shownWindow.getId())) {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    MemoEditor memoEditor = MemoEditor.getInstance();
                    memoEditor.requestFocusOnEditor();
                });
            }
        };
    }

    private @Nullable ToolWindow getWindow(Project project) {
        return ToolWindowManager
                .getInstance(project)
                .getToolWindow(WINDOW_NAME);
    }
}
