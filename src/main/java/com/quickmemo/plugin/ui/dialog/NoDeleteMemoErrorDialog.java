package com.quickmemo.plugin.ui.dialog;

import com.intellij.openapi.ui.Messages;

public class NoDeleteMemoErrorDialog {
    private static final String NO_DELETE_MEMO_ERROR_MESSAGE = "No Selected Memo To Delete";
    private static final String NO_DELETE_MEMO_ERROR_TITLE = "Memo Unselected";

    public static void show() {
        Messages.showErrorDialog(
                NO_DELETE_MEMO_ERROR_MESSAGE,
                NO_DELETE_MEMO_ERROR_TITLE
        );
    }
}
