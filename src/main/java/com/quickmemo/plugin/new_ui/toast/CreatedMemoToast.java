package com.quickmemo.plugin.new_ui.toast;

import com.quickmemo.plugin.constant.DialogConstants;

import javax.swing.*;

public class CreatedMemoToast {
    public static void show(JComponent anchor) {
        SwingUtilities.invokeLater(() -> {
            ToastPopup popup = new ToastPopup(DialogConstants.MEMO_CREATED_TOAST, anchor);
            popup.showToast();
        });
    }
}
