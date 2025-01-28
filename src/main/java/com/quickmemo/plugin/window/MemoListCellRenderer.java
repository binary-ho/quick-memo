package com.quickmemo.plugin.window;

import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.memo.MemoConstants;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.quickmemo.plugin.memo.MemoConstants.TITLE_LENGTH_LIMIT;

public class MemoListCellRenderer extends DefaultListCellRenderer {
    private static final String EMPTY = "";

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
                .orElse(EMPTY)
                .trim();

        if (firstLine.length() > TITLE_LENGTH_LIMIT) {
            return firstLine.substring(0, TITLE_LENGTH_LIMIT);
        }

        if (firstLine.isEmpty()) {
            return MemoConstants.DEFAULT_TITLE;
        }
        return firstLine;
    }
}