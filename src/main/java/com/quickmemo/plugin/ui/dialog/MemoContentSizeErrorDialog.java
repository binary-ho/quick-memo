package com.quickmemo.plugin.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.quickmemo.plugin.ui.dialog.util.DialogOpenHelper;

public class MemoContentSizeErrorDialog {
    private static final String MEMO_CONTENT_SIZE_LIMIT_REACHED_ERROR_MESSAGE = "The memo content has exceeded the maximum size limit.";
    private static final String MEMO_CONTENT_SIZE_LIMIT_REACHED_ERROR_TITLE = "Memo Size Limit Reached";

    public static void show(Project project) {
        DialogOpenHelper.showDialogOnCenterOfWindow(project,
                () -> Messages.showErrorDialog(MEMO_CONTENT_SIZE_LIMIT_REACHED_ERROR_MESSAGE, MEMO_CONTENT_SIZE_LIMIT_REACHED_ERROR_TITLE)
        );
    }
}
