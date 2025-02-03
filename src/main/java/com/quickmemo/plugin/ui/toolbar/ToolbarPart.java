package com.quickmemo.plugin.ui.toolbar;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.quickmemo.plugin.ui.button.ActionButton;

import javax.swing.*;
import java.util.List;

public class ToolbarPart {
    private final ActionToolbar toolbar;

    public static ToolbarPart create(String toolbarName, JComponent mainWindow, List<ActionButton> buttons) {
        DefaultActionGroup actionGroup = createActionGroup(buttons);
        ActionToolbar toolbar = createActionToolbar(toolbarName, actionGroup);
        toolbar.setTargetComponent(mainWindow);
        toolbar.setMiniMode(true);
        return new ToolbarPart(toolbar);
    }

    private static DefaultActionGroup createActionGroup(List<ActionButton> buttons) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        buttons.stream()
                .map(ActionButton::getCreateMemoAction)
                .forEach(actionGroup::add);
        return actionGroup;
    }

    private static ActionToolbar createActionToolbar(String toolbarName, DefaultActionGroup actionGroup) {
        return ActionManager.getInstance()
                .createActionToolbar(toolbarName, actionGroup, true);
    }

    public JComponent getComponent() {
        return toolbar.getComponent();
    }

    private ToolbarPart(ActionToolbar toolbar) {
        this.toolbar = toolbar;
    }
}
