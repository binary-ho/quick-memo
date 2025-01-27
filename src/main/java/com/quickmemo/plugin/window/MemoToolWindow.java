package com.quickmemo.plugin.window;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.quickmemo.plugin.service.MemoService;

import javax.swing.*;
import java.awt.*;

public class MemoToolWindow {
    private final JPanel content;
    private final JBTextArea textArea;
    private final MemoService memoService;

    public MemoToolWindow(Project project) {
        memoService = project.getService(MemoService.class);
        content = new JPanel(new BorderLayout());
        textArea = new JBTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Load saved content
        String savedContent = memoService.loadMemo();
        if (savedContent != null) {
            textArea.setText(savedContent);
        }

        // Auto-save on text change
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                saveContent();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                saveContent();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                saveContent();
            }
        });

        content.add(new JBScrollPane(textArea), BorderLayout.CENTER);
    }

    private void saveContent() {
        memoService.saveMemo(textArea.getText());
    }

    public JPanel getContent() {
        return content;
    }
}
