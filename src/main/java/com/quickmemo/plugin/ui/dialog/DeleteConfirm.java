package com.quickmemo.plugin.ui.dialog;

import com.intellij.openapi.project.Project;
import com.quickmemo.plugin.ui.dialog.util.DialogOpenHelper;
import com.quickmemo.plugin.ui.dialog.util.NoYesDialog;

public class DeleteConfirm {
    private static final String DELETE_MEMO_CONFIRM_TITLE = "Delete Memo";
    private static final String DELETE_MEMO_CONFIRM_CONTENT = "Are you sure?";

    private static final String DELETE_MEMO_CONFIRM_YES = "Delete!";
    private static final String DELETE_MEMO_CONFIRM_NO = "Cancel";

    public static boolean confirm(Project project) {
        return DialogOpenHelper.showYesNoDialog(
            project, DeleteConfirm::showConfirmDialog
        );
    }

    private static boolean showConfirmDialog() {
        return NoYesDialog.showNoYesDialog(
                DELETE_MEMO_CONFIRM_CONTENT,
                DELETE_MEMO_CONFIRM_TITLE,
                DELETE_MEMO_CONFIRM_NO,
                DELETE_MEMO_CONFIRM_YES
        );
    }
}
