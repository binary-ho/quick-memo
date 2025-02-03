package com.quickmemo.plugin.ui.message;

import com.intellij.openapi.ui.Messages;

public class MemoContentSizeErrorDialog {
    private static final String MEMO_CONTENT_SIZE_LIMIT_REACHED_ERROR_TITLE = "Memo Size Limit Reached";
    private static final String MEMO_CONTENT_SIZE_LIMIT_REACHED_ERROR_MESSAGE = "The memo content has exceeded the maximum size limit.";


    public static void show() {
        Messages.showErrorDialog(
                MEMO_CONTENT_SIZE_LIMIT_REACHED_ERROR_TITLE,
                MEMO_CONTENT_SIZE_LIMIT_REACHED_ERROR_MESSAGE
        );
    }
}
