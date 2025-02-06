package com.quickmemo.plugin.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.quickmemo.plugin.ui.dialog.util.DialogOpenHelper;

public class MemoCountErrorDialog {
    private static final String MEMO_COUNT_LIMIT_REACHED_WARNING_MESSAGE = "Max 20 Memos";
    private static final String MEMO_COUNT_LIMIT_REACHED_WARNING_TITLE = "Memo Limit Reached!";

    public static void show(Project project) {
        DialogOpenHelper.showDialogOnCenterOfWindow(project,
                () -> Messages.showErrorDialog(MEMO_COUNT_LIMIT_REACHED_WARNING_MESSAGE, MEMO_COUNT_LIMIT_REACHED_WARNING_TITLE)
        );
    }
}
