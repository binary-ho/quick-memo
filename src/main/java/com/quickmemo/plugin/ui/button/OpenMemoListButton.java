package com.quickmemo.plugin.ui.button;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class OpenMemoListButton extends ActionButton {
    private final AnAction action;

    public static final String OPEN_MEMO_LIST_NAME = "Show Memo List";
    public static final String OPEN_MEMO_LIST_DESCRIPTION = "Show all memos";
    private static final Icon OPEN_MEMO_LIST_BUTTON_ICON = AllIcons.Actions.Minimap;

    public OpenMemoListButton(Consumer<Component> consumer) {
        this.action = createAction(consumer);
    }

    private AnAction createAction(Consumer<Component> listener) {
        return new AnAction(OPEN_MEMO_LIST_NAME, OPEN_MEMO_LIST_DESCRIPTION, OPEN_MEMO_LIST_BUTTON_ICON) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent event) {
                acceptIfMouseEvent(event, listener);
            }
        };
    }

    private void acceptIfMouseEvent(@NotNull AnActionEvent event, Consumer<Component> listener) {
        InputEvent inputEvent = event.getInputEvent();
        if (!(inputEvent instanceof MouseEvent)) {
            return;
        }

        Component component = inputEvent.getComponent();
        listener.accept(component);
    }

    @Override
    public AnAction getCreateMemoAction() {
        return action;
    }
}
