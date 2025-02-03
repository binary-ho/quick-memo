package com.quickmemo.plugin.ui.button;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DeleteMemoButton extends ActionButton {
    private final AnAction action;

    private static final String DELETE_MEMO_BUTTON_NAME = "Delete Memo";
    private static final String DELETE_MEMO_BUTTON_DESCRIPTION = "Delete selected memo";
    private static final Icon DELETE_MEMO_BUTTON_ICON = AllIcons.General.Remove;

    public DeleteMemoButton(Runnable runnable) {
        this.action = createAction(runnable);
    }

    private AnAction createAction(Runnable runnable) {
        return new AnAction(DELETE_MEMO_BUTTON_NAME, DELETE_MEMO_BUTTON_DESCRIPTION, DELETE_MEMO_BUTTON_ICON) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent event) {
                runnable.run();
            }
        };
    }

    @Override
    public AnAction getCreateMemoAction() {
        return action;
    }
}
