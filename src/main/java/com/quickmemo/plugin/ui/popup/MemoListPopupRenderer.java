package com.quickmemo.plugin.ui.popup;

import com.quickmemo.plugin.memo.Memo;

import javax.swing.*;
import java.awt.*;

public class MemoListPopupRenderer extends DefaultListCellRenderer {
    private static final String DEFAULT_MEMO_TITLE = "New Memo";
    private static final int TITLE_LENGTH_LIMIT = 30;

    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Memo memo) {
            String title = buildTitle(memo.getContent());
            super.setText(title);
        }
        return this;
    }

    private static String buildTitle(String content) {
        String firstLine = getFirstLine(content);

        if (firstLine.length() > TITLE_LENGTH_LIMIT) {
            return firstLine.substring(0, TITLE_LENGTH_LIMIT);
        }

        if (firstLine.isEmpty()) {
            return DEFAULT_MEMO_TITLE;
        }

        return firstLine;
    }

    private static String getFirstLine(String content) {
        int newlineIndex = content.indexOf('\n');
        if (newlineIndex == -1) {
            return content;
        }
        return content.substring(0, newlineIndex);
    }
}
