package com.quickmemo.plugin.new_ui.message;

import com.intellij.openapi.ui.Messages;
import com.quickmemo.plugin.constant.DialogConstants;

public class DeleteConfirm {
    public static boolean confirm() {
        int result = openConfirmWindow();
        return result == Messages.YES;
    }

    private static int openConfirmWindow() {
        return Messages.showYesNoDialog(
                DialogConstants.DELETE_MEMO_CONFIRM_CONTENT,
                DialogConstants.DELETE_MEMO_CONFIRM_TITLE,
                DialogConstants.DELETE_MEMO_CONFIRM_YES,
                DialogConstants.DELETE_MEMO_CONFIRM_NO,
                Messages.getQuestionIcon()
        );
    }
}
