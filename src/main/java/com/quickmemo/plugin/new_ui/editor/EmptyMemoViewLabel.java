package com.quickmemo.plugin.new_ui.editor;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;

public class EmptyMemoViewLabel extends JBLabel {
    private static final String EMPTY_STATE_MESSAGE = "Click '+' to write it out";
    private static final float FONT_SIZE = 14f;

    public EmptyMemoViewLabel() {
        super(EMPTY_STATE_MESSAGE, SwingConstants.CENTER);
        this.setFont(getFont().deriveFont(FONT_SIZE));
        this.setForeground(JBUI.CurrentTheme.Label.disabledForeground());
    }
}
