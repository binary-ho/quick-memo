package com.quickmemo.plugin.ui.dialog.util;

import com.intellij.openapi.project.Project;

import java.util.function.BooleanSupplier;

public class DialogOpenHelper {
    public static void showDialogOnCenterOfWindow(Project project, Runnable showDialog) {
        DialogMover.moveDialogOnCenterOfToolWindow(project);
        showDialog.run();
    }

    public static boolean showYesNoDialog(Project project, BooleanSupplier supplier) {
        DialogMover.moveDialogOnCenterOfToolWindow(project);
        return supplier.getAsBoolean();
    }
}
