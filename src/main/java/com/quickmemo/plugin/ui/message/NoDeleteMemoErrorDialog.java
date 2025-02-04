package com.quickmemo.plugin.ui.message;

import com.intellij.openapi.ui.Messages;

public class NoDeleteMemoErrorDialog {
    private static final String NO_DELETE_MEMO_ERROR_TITLE = "Select memo before delete";
    private static final String NO_DELETE_MEMO_ERROR_MESSAGE = "No Selected Memo To Delete";

    public static void show() {
        Messages.showErrorDialog(
                NO_DELETE_MEMO_ERROR_TITLE,
                NO_DELETE_MEMO_ERROR_MESSAGE
        );
    }
}
