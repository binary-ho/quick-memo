package com.quickmemo.plugin.new_ui;

import javax.swing.*;
import java.awt.*;

public class QuickMemoWindow {
    private final JPanel content;

    public QuickMemoWindow() {
        this.content = new JPanel(new BorderLayout());
    }

    public JPanel getContent() {
        return content;
    }
}
