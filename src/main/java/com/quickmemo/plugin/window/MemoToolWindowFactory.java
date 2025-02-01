package com.quickmemo.plugin.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MemoToolWindowFactory implements ToolWindowFactory {
    private static final int DEFAULT_SIZE = 400;
    private static final Dimension DEFAULT_DIMENSION_SIZE = new Dimension(DEFAULT_SIZE, DEFAULT_SIZE);
    private static MemoToolWindow memoToolWindow;

    public static MemoToolWindow findMemoToolWindowInstance() {
        return memoToolWindow;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        toolWindow.setType(ToolWindowType.FLOATING, null);

        memoToolWindow = new MemoToolWindow(project);
        JComponent component = memoToolWindow.getContent();

        component.setPreferredSize(DEFAULT_DIMENSION_SIZE);
        component.setSize(DEFAULT_DIMENSION_SIZE);

        Content content = ContentFactory.getInstance()
                .createContent(component, "", false);
        toolWindow.getContentManager().addContent(content);

        SwingUtilities.invokeLater(() -> {
            Timer timer = new Timer(100, event -> {
                Window win = SwingUtilities.getWindowAncestor(component);
                if (win != null) {
                    win.setPreferredSize(DEFAULT_DIMENSION_SIZE);
                    win.setSize(DEFAULT_DIMENSION_SIZE);
                    win.pack();
                }
                ((Timer) event.getSource()).stop();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
}
