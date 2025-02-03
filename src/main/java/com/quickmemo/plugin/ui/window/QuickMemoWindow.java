package com.quickmemo.plugin.ui.window;

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
