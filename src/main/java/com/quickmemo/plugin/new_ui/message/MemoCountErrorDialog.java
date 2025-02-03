package com.quickmemo.plugin.new_ui.message;

import com.intellij.openapi.ui.Messages;

public class MemoCountErrorDialog {
    private static final String MEMO_COUNT_LIMIT_REACHED_WARNING_TITLE = "Memo Limit Reached!";
    private static final String MEMO_COUNT_LIMIT_REACHED_WARNING_MESSAGE = "Max 20 Memos";

    public static void show() {
        Messages.showErrorDialog(
                MEMO_COUNT_LIMIT_REACHED_WARNING_MESSAGE,
                MEMO_COUNT_LIMIT_REACHED_WARNING_TITLE
        );
    }
}
