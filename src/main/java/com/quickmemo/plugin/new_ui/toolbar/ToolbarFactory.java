package com.quickmemo.plugin.new_ui.toolbar;

import com.intellij.util.ui.JBUI;
import com.quickmemo.plugin.new_ui.button.CreateMemoButton;
import com.quickmemo.plugin.new_ui.button.DeleteMemoButton;
import com.quickmemo.plugin.new_ui.button.OpenMemoListButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ToolbarFactory {
    public static final String LEFT_TOOLBAR_PART_NAME = "QuickMemoLeftToolbar";
    public static final String RIGHT_TOOLBAR_PART_NAME = "QuickMemoRightToolbar";

    public static MainToolbar createQuickMemoToolbar(JComponent mainWindow, CreateMemoButton newMemoButton, DeleteMemoButton deleteMemoButton, OpenMemoListButton openMemoListButton) {
        ToolbarPart leftToolbar = ToolbarPart.create(
                LEFT_TOOLBAR_PART_NAME, mainWindow,
                List.of(newMemoButton, deleteMemoButton)
        );

        ToolbarPart rightToolbar = createRightToolbar(mainWindow, openMemoListButton);
        return createMainToolbar(leftToolbar, rightToolbar);
    }

    private static ToolbarPart createRightToolbar(JComponent mainWindow, OpenMemoListButton openMemoListButton) {
        ToolbarPart rightPart = ToolbarPart.create(
                RIGHT_TOOLBAR_PART_NAME, mainWindow,
                List.of(openMemoListButton)
        );

        JComponent rightToolbarComponent = rightPart.getComponent();
        rightToolbarComponent.setBorder(JBUI.Borders.empty());
        rightToolbarComponent.setOpaque(false);
        return rightPart;
    }

    private static MainToolbar createMainToolbar(ToolbarPart leftToolbar, ToolbarPart rightToolbar) {
        JPanel mainToolbar = new JPanel(new BorderLayout(0, 0));
        mainToolbar.setBorder(JBUI.Borders.empty());
        mainToolbar.setOpaque(false);
        mainToolbar.add(leftToolbar.getComponent(), BorderLayout.WEST);
        mainToolbar.add(rightToolbar.getComponent(), BorderLayout.EAST);
        return new MainToolbar(mainToolbar);
    }
}
