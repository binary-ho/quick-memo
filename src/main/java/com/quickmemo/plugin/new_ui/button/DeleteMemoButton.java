package com.quickmemo.plugin.new_ui.button;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;

import javax.swing.*;

public class DeleteMemoButton extends ActionButton {
    private final AnAction action;

    private static final String DELETE_MEMO_BUTTON_NAME = "Delete Memo";
    private static final String DELETE_MEMO_BUTTON_DESCRIPTION = "Delete selected memo";
    private static final Icon DELETE_MEMO_BUTTON_ICON = AllIcons.General.Remove;

    public static ActionButton createWithRunnable(Runnable runnable) {
        AnAction action = ButtonActionFactory.createAction(
                DELETE_MEMO_BUTTON_NAME,
                DELETE_MEMO_BUTTON_DESCRIPTION,
                DELETE_MEMO_BUTTON_ICON,
                runnable
        );
        return new DeleteMemoButton(action);
    }

    @Override
    public AnAction getAction() {
        return action;
    }

    private DeleteMemoButton(AnAction action) {
        this.action = action;
    }
}
