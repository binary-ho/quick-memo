package com.quickmemo.plugin.window.component;

import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.util.ui.JBUI;
import com.quickmemo.plugin.window.action.MemoActionManager;

import javax.swing.*;
import java.awt.*;

public class MemoToolbar extends JPanel {
    private final ActionToolbar leftToolbar;
    private final ActionToolbar rightToolbar;

    private MemoToolbar(ActionToolbar leftToolbar, ActionToolbar rightToolbar) {
        super(new BorderLayout(0, 0));
        this.leftToolbar = leftToolbar;
        this.rightToolbar = rightToolbar;
        
        initializeLayout();
    }

    public static MemoToolbar initializeWithActionManager(MemoActionManager actionManager, JComponent targetComponent) {
        ActionToolbar leftToolbar = actionManager.createLeftToolbar(targetComponent);
        ActionToolbar rightToolbar = actionManager.createRightToolbar(targetComponent);
        return new MemoToolbar(leftToolbar, rightToolbar);
    }

    private void initializeLayout() {
        setBorder(JBUI.Borders.empty());
        setOpaque(false);

        removeComponentBlank();

        add(leftToolbar.getComponent(), BorderLayout.WEST);
        add(rightToolbar.getComponent(), BorderLayout.EAST);
    }

    private void removeComponentBlank() {
        JComponent rightToolbarComponent = rightToolbar.getComponent();
        rightToolbarComponent.setBorder(JBUI.Borders.empty());
        rightToolbarComponent.setOpaque(false);
    }

    public ActionToolbar getLeftToolbar() {
        return leftToolbar;
    }
}
