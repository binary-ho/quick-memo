package com.quickmemo.plugin.ui.dialog.util;

import com.intellij.openapi.ui.Messages;

public class NoYesDialog {
    public static boolean showNoYesDialog(
            String content,
            String title,
            String no,
            String yes
    ) {
        int confirm = showYesNoDialog(content, title, no, yes);
        return toggleYesAndNo(confirm == Messages.YES);
    }

    private static int showYesNoDialog(
            String content,
            String title,
            String yes,
            String no
    ) {
        // Flip Yes And No!
        return Messages.showYesNoDialog(
                content,
                title,
                yes,
                no,
                Messages.getQuestionIcon()
        );
    }

    private static boolean toggleYesAndNo(boolean result) {
        return !result;
    }
}
