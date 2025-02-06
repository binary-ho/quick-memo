package com.quickmemo.plugin.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.quickmemo.plugin.constant.DialogConstants;
import com.quickmemo.plugin.ui.dialog.util.DialogOpenHelper;

public class DeleteConfirm {
    public static boolean confirm(Project project) {
        return DialogOpenHelper.showYesNoDialog(
            project, DeleteConfirm::isConfirmed
        );
    }

    private static boolean isConfirmed() {
        return showConfirmDialog() == Messages.YES;
    }

    private static int showConfirmDialog() {
        return Messages.showYesNoDialog(
                DialogConstants.DELETE_MEMO_CONFIRM_CONTENT,
                DialogConstants.DELETE_MEMO_CONFIRM_TITLE,
                DialogConstants.DELETE_MEMO_CONFIRM_YES,
                DialogConstants.DELETE_MEMO_CONFIRM_NO,
                Messages.getQuestionIcon()
        );
    }
}
