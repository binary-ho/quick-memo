package com.quickmemo.plugin.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.quickmemo.plugin.window.component.MemoEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MemoToolWindowFactory implements ToolWindowFactory {
    private static final int DEFAULT_SIZE = 400;
    private static final Dimension DEFAULT_DIMENSION_SIZE = new Dimension(DEFAULT_SIZE, DEFAULT_SIZE);
    public static final String WINDOW_NAME = "QuickMemo";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        toolWindow.setType(ToolWindowType.FLOATING, null);

        MemoToolWindow memoToolWindow = new MemoToolWindow(project);
        JComponent component = memoToolWindow.getContent();

        component.setPreferredSize(DEFAULT_DIMENSION_SIZE);
        component.setSize(DEFAULT_DIMENSION_SIZE);

        Content content = ContentFactory.getInstance()
                .createContent(component, "", false);
        toolWindow.getContentManager().addContent(content);

        addEditorOpenListener(project);

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

    private void addEditorOpenListener(@NotNull Project project) {
        project.getMessageBus().connect().subscribe(
                ToolWindowManagerListener.TOPIC,
                getWindowManagerListener()
        );
    }

    private ToolWindowManagerListener getWindowManagerListener() {
        return new ToolWindowManagerListener() {
            @Override
            public void toolWindowShown(@NotNull ToolWindow shownWindow) {
                if (!WINDOW_NAME.equals(shownWindow.getId())) {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    MemoEditor memoEditor = MemoEditor.getInstance();
                    memoEditor.requestFocusOnEditor();
                });
            }
        };
    }
}
