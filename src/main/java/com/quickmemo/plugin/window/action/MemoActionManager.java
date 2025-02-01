package com.quickmemo.plugin.window.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ui.Messages;
import com.quickmemo.plugin.constant.ActionConstants;
import com.quickmemo.plugin.constant.DialogConstants;
import com.quickmemo.plugin.window.MemoToolWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MemoActionManager {
    private final MemoToolWindow window;
    private final DefaultActionGroup leftGroup;
    private final DefaultActionGroup rightGroup;
    private final AnAction addAction;

    public MemoActionManager(MemoToolWindow window) {
        this.window = window;
        this.leftGroup = new DefaultActionGroup();
        this.rightGroup = new DefaultActionGroup();
        this.addAction = createAddAction();
        
        initializeActions();
    }

    private void initializeActions() {
        leftGroup.add(addAction);
        leftGroup.add(createDeleteAction());
        rightGroup.add(createShowListAction());
    }

    private AnAction createAddAction() {
        return new AnAction(
            ActionConstants.ACTION_NEW_MEMO,
            ActionConstants.ACTION_NEW_MEMO_DESC,
            AllIcons.General.Add
        ) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                window.createNewMemo();
            }
        };
    }

    private AnAction createDeleteAction() {
        return new AnAction(
            ActionConstants.ACTION_DELETE_MEMO,
            ActionConstants.ACTION_DELETE_MEMO_DESC,
            AllIcons.General.Remove
        ) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                if (window.getCurrentMemo().isUnselected()) {
                    return;
                }

                int result = Messages.showYesNoDialog(
                    DialogConstants.DELETE_MEMO_CONFIRM_CONTENT,
                    DialogConstants.DELETE_MEMO_CONFIRM_TITLE,
                    DialogConstants.DELETE_MEMO_CONFIRM_YES,
                    DialogConstants.DELETE_MEMO_CONFIRM_NO,
                    Messages.getQuestionIcon()
                );

                if (result == Messages.YES) {
                    window.deleteSelectedMemo();
                }
            }
        };
    }

    private AnAction createShowListAction() {
        return new AnAction(
            ActionConstants.ACTION_SHOW_LIST,
            ActionConstants.ACTION_SHOW_LIST_DESC,
            AllIcons.Actions.Minimap
        ) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                window.showMemoListPopup();
            }
        };
    }

    public ActionToolbar createLeftToolbar(JComponent targetComponent) {
        ActionToolbar toolbar = ActionManager.getInstance()
            .createActionToolbar("MemoToolbarLeft", leftGroup, true);
        toolbar.setTargetComponent(targetComponent);
        toolbar.setMiniMode(true);
        return toolbar;
    }

    public ActionToolbar createRightToolbar(JComponent targetComponent) {
        ActionToolbar toolbar = ActionManager.getInstance()
            .createActionToolbar("MemoToolbarRight", rightGroup, true);
        toolbar.setTargetComponent(targetComponent);
        toolbar.setMiniMode(true);
        return toolbar;
    }

    public AnAction getAddAction() {
        return addAction;
    }
}
