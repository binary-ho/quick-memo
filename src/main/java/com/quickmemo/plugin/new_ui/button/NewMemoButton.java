package com.quickmemo.plugin.new_ui.button;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;

import javax.swing.*;

public class NewMemoButton extends ActionButton {
    private final AnAction action;

    private static final String NEW_MEMO_BUTTON_NAME = "New Memo";
    private static final String NEW_MEMO_BUTTON_DESCRIPTION = "Create new memo";
    private static final Icon NEW_MEMO_BUTTON_ICON = AllIcons.General.Add;

    public static NewMemoButton createWithRunnable(Runnable runnable) {
        AnAction action = ButtonActionFactory.createAction(
                NEW_MEMO_BUTTON_NAME,
                NEW_MEMO_BUTTON_DESCRIPTION,
                NEW_MEMO_BUTTON_ICON,
                runnable
        );
        return new NewMemoButton(action);
    }

    @Override
    public AnAction getAction() {
        return action;
    }

    private NewMemoButton(AnAction action) {
        this.action = action;
    }
}
