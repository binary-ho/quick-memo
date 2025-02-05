package com.quickmemo.plugin.ui.message;

import com.intellij.openapi.ui.Messages;

public class CreatedMemoDialog {
    private static final String CREATED_MEMO_MESSAGE = "Memo Created!";
    private static final String CREATED_MEMO_TITLE = "";

    public static void show() {
        Messages.showInfoMessage(
                CREATED_MEMO_MESSAGE,
                CREATED_MEMO_TITLE
        );
    }
}
