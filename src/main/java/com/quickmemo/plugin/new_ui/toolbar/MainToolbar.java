package com.quickmemo.plugin.new_ui.toolbar;

import javax.swing.*;

public class MainToolbar {
    private final JPanel memoToolbar;

    MainToolbar(JPanel memoToolbar) {
        this.memoToolbar = memoToolbar;
    }

    public JPanel getMemoToolbar() {
        return memoToolbar;
    }
}
