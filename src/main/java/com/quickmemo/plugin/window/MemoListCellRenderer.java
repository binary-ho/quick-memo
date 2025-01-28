package com.quickmemo.plugin.window;

import com.quickmemo.plugin.constant.MemoConstants;
import com.quickmemo.plugin.memo.Memo;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MemoListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Memo memo) {
            String title = getTitle(memo);
            setText(title);
        }
        return this;
    }

    private static @NotNull String getTitle(Memo memo) {
        String firstLine = memo.content()
                .lines()
                .findFirst()
                .orElse("")
                .trim();

        if (!firstLine.isEmpty()) {
            return firstLine;
        }
        return MemoConstants.DEFAULT_TITLE;
    }
}