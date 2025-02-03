package com.quickmemo.plugin.ui.toast;

import com.quickmemo.plugin.constant.DialogConstants;

import javax.swing.*;
import java.awt.*;

public class CreatedMemoToast {
    public static void show(Component anchor) {
        SwingUtilities.invokeLater(() -> {
            ToastPopup popup = new ToastPopup(DialogConstants.MEMO_CREATED_TOAST, anchor);
            popup.showToast();
        });
    }
}
