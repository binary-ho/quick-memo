package com.quickmemo.plugin.window.component;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseWheelEvent;

public class MemoEditor extends JPanel {
    private final JBTextArea textArea;
    public static final JBEmptyBorder INPUT_AREA_PADDING = JBUI.Borders.empty(8, 10);

    public MemoEditor() {
        super(new BorderLayout());
        this.textArea = createTextArea();
        add(new JBScrollPane(textArea), BorderLayout.CENTER);
    }

    private JBTextArea createTextArea() {
        JBTextArea area = new JBTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(INPUT_AREA_PADDING);
        
        area.addMouseWheelListener(
                event -> adjustFontSize(event, area)
        );
        return area;
    }

    private void adjustFontSize(MouseWheelEvent e, JBTextArea area) {
        if (e.isControlDown()) {
            Font font = area.getFont();
            int fontSize = font.getSize();

            if (e.getWheelRotation() < 0 && fontSize < 72) {
                area.setFont(font.deriveFont((float) fontSize + 1));
            } else if (e.getWheelRotation() > 0 && fontSize > 8) {
                area.setFont(font.deriveFont((float) fontSize - 1));
            }
            e.consume();
        }
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public String getText() {
        return textArea.getText();
    }

    public void addDocumentListener(DocumentListener listener) {
        textArea.getDocument().addDocumentListener(listener);
    }

    public void requestFocusOnEditor() {
        textArea.requestFocus();
    }
}
