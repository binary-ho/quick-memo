package com.quickmemo.plugin.new_ui.button;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ButtonActionFactory {
    public static AnAction createAction(String name, String description, Icon icon, Runnable runnable) {
        return new AnAction(name, description, icon) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent event) {
                runnable.run();
            }
        };
    }
}
