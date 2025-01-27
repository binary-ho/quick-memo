package com.quickmemo.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenMemoAction extends AnAction {

    public static final String WINDOW_NAME = "QuickMemo";

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        openWindow(project);
    }

    private void openWindow(Project project) {
        ToolWindow window = getWindow(project);
        if (window != null) {
            window.show();
        }
    }

    private @Nullable ToolWindow getWindow(Project project) {
        return ToolWindowManager
                .getInstance(project)
                .getToolWindow(WINDOW_NAME);
    }
}
