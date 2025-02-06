package com.quickmemo.plugin.ui.button;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.quickmemo.plugin.memo.MemoLimitExceededException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class CreateMemoButton extends ActionButton {
    private final AnAction createMemoAction;

    private static final String NEW_MEMO_BUTTON_NAME = "New Memo";
    private static final String NEW_MEMO_BUTTON_DESCRIPTION = "Create new memo";
    private static final Icon NEW_MEMO_BUTTON_ICON = AllIcons.General.Add;

    public CreateMemoButton(Runnable createAction, Runnable onCreateSuccess, Runnable onCreateFailed) {
        this.createMemoAction = createAction(createAction, onCreateSuccess, onCreateFailed);
    }

    private AnAction createAction(Runnable createAction, Runnable onCreateSuccess, Runnable onCreateFailed) {
        return new AnAction(NEW_MEMO_BUTTON_NAME, NEW_MEMO_BUTTON_DESCRIPTION, NEW_MEMO_BUTTON_ICON) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent event) {
                try {
                    createAction.run();
                    acceptIfMouseEvent(event, onCreateSuccess);
                } catch (MemoLimitExceededException exception) {
                    onCreateFailed.run();
                }
            }
        };
    }

    private void acceptIfMouseEvent(@NotNull AnActionEvent event, Runnable onCreateSuccess) {
        InputEvent inputEvent = event.getInputEvent();
        if (!(inputEvent instanceof MouseEvent)) {
            return;
        }
        onCreateSuccess.run();
    }

    @Override
    public AnAction getCreateMemoAction() {
        return createMemoAction;
    }
}
