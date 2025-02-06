package com.quickmemo.plugin.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.quickmemo.plugin.ui.dialog.util.DialogOpenHelper;

public class CreatedMemoDialog {
    private static final String CREATED_MEMO_MESSAGE = "Memo Created!";
    private static final String CREATED_MEMO_TITLE = "Success";

    public static void show(Project project) {
        DialogOpenHelper.showDialogOnCenterOfWindow(project,
                () -> Messages.showInfoMessage(CREATED_MEMO_MESSAGE, CREATED_MEMO_TITLE)
        );
    }
}
