package com.quickmemo.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

public class NewMemoAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (e.getProject() == null) return;
        
        ToolWindow toolWindow = ToolWindowManager.getInstance(e.getProject()).getToolWindow("QuickMemo");
        if (toolWindow != null) {
            toolWindow.show(null);
        }
    }
}
