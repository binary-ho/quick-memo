package com.quickmemo.plugin.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.quickmemo.plugin.ui.dialog.util.DialogOpenHelper;

public class NoDeleteMemoErrorDialog {
    private static final String NO_DELETE_MEMO_ERROR_MESSAGE = "No Selected Memo To Delete";
    private static final String NO_DELETE_MEMO_ERROR_TITLE = "Memo Unselected";

    public static void show(Project project) {
        DialogOpenHelper.showDialogOnCenterOfWindow(project,
                () -> Messages.showErrorDialog(NO_DELETE_MEMO_ERROR_MESSAGE, NO_DELETE_MEMO_ERROR_TITLE)
        );
    }
}
