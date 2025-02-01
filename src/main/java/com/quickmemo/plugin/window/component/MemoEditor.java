package com.quickmemo.plugin.window.component;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseWheelEvent;

public class MemoEditor extends JPanel {
    private static MemoEditor instance;
    private final JBTextArea textArea;

    private static final int TOP_BOTTOM_PADDING = 8;
    private static final int LEFT_RIGHT_PADDING = 10;
    private static final JBEmptyBorder INPUT_AREA_PADDING = JBUI.Borders
            .empty(TOP_BOTTOM_PADDING, LEFT_RIGHT_PADDING);

    public static MemoEditor getInstance() {
        if (instance == null) {
            instance = new MemoEditor();
        }
        return instance;
    }

    private MemoEditor() {
        super(new BorderLayout());
        textArea = createTextArea();
        addMouseWheelListener(textArea);
    }

    private void addMouseWheelListener(JBTextArea textArea) {
        JBScrollPane scrollPane = new JBScrollPane(textArea);
        scrollPane.addMouseWheelListener(this::handleFontSize);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JBTextArea createTextArea() {
        JBTextArea area = new JBTextArea();
        area.setFont(area.getFont().deriveFont((float) JBUI.scale(14)));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(INPUT_AREA_PADDING);
        return area;
    }

    private void handleFontSize(MouseWheelEvent event) {
        if (!event.isControlDown()) {
            return;
        }

        Font font = textArea.getFont();
        int fontSize = font.getSize();

        if (event.getWheelRotation() < 0 && fontSize < 72) {
            textArea.setFont(font.deriveFont((float) fontSize + 1));
        } else if (event.getWheelRotation() > 0 && fontSize > 8) {
            textArea.setFont(font.deriveFont((float) fontSize - 1));
        }
        event.consume();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public String getText() {
        return textArea.getText();
    }

    public void addDocumentListener(DocumentListener listener) {
        Document document = textArea.getDocument();
        document.addDocumentListener(listener);
    }

    public void requestFocusOnEditor() {
        textArea.setCaretPosition(textArea.getText().length());
        textArea.setEditable(true);
        textArea.setEnabled(true);
        textArea.setFocusable(true);
        textArea.grabFocus();
        textArea.requestFocusInWindow();
    }
}
