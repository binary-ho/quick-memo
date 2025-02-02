package com.quickmemo.plugin.new_ui.button;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;

import javax.swing.*;

public class OpenMemoListButton extends ActionButton {
    private final AnAction action;

    public static final String OPEN_MEMO_LIST_NAME = "Show Memo List";
    public static final String OPEN_MEMO_LIST_DESCRIPTION = "Show all memos";
    private static final Icon OPEN_MEMO_LIST_BUTTON_ICON = AllIcons.Actions.Minimap;

    public static ActionButton createWithRunnable(Runnable runnable) {
        AnAction action = ButtonActionFactory.createAction(
                OPEN_MEMO_LIST_NAME,
                OPEN_MEMO_LIST_DESCRIPTION,
                OPEN_MEMO_LIST_BUTTON_ICON,
                runnable
        );
        return new OpenMemoListButton(action);
    }

    @Override
    public AnAction getAction() {
        return action;
    }

    private OpenMemoListButton(AnAction action) {
        this.action = action;
    }
}
