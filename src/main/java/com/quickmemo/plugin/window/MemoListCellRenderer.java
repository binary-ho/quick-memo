package com.quickmemo.plugin.window;

import com.quickmemo.plugin.memo.Memo;

import javax.swing.*;
import java.awt.*;

public class MemoListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Memo memo) {
            String title = memo.content().lines().findFirst().orElse("").trim();
            if (title.isEmpty()) {
                title = "New Memo";
            }
            setText(title);
        }
        return this;
    }
}