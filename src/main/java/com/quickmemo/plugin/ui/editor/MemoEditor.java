package com.quickmemo.plugin.ui.editor;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;

public class MemoEditor extends JPanel {
    public static final String STRIKE_STYLE_NAME = "StrikeStyle";
    private static volatile MemoEditor INSTANCE;
//    private final JBTextArea textArea;
    private final JTextPane textArea;

    private static final int TOP_BOTTOM_PADDING = 8;
    private static final int LEFT_RIGHT_PADDING = 10;

    public static MemoEditor getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        synchronized (MemoEditor.class) {
            if (INSTANCE == null) {
                INSTANCE = new MemoEditor();
            }
        }
        return INSTANCE;
    }

    private MemoEditor() {
        super(new BorderLayout());
        this.textArea = createTextArea();
        addMouseWheelListener(textArea);

        JButton strikeButton = new JButton("취소선 토글");
        strikeButton.addActionListener(e -> toggleStrikeThrough(this.textArea));
        this.add(strikeButton, BorderLayout.SOUTH);
    }

    private JTextPane createTextArea() {
        JTextPane area = new JTextPane();

        FontHolder fontHolder = new FontHolder();
        area.setFont(fontHolder.getEditorFont());

        // TODO: 기존 TextArea에 적용된 설정인데, JTextPane에선 적용 불가능한가?
//        area.setLineWrap(true);
//        area.setWrapStyleWord(true);
        area.setMargin(JBUI.insets(TOP_BOTTOM_PADDING, LEFT_RIGHT_PADDING));
        return area;
    }

    private void addMouseWheelListener(JTextPane textArea) {
        JBScrollPane scrollPane = new JBScrollPane(textArea);
        scrollPane.addMouseWheelListener(this::handleFontSize);
        this.add(scrollPane, BorderLayout.CENTER);
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
        int lastPosition = textArea.getDocument().getEndPosition().getOffset();
        int caretPosition = lastPosition - 1;
        textArea.setCaretPosition(caretPosition);
        textArea.grabFocus();
    }

    private void toggleStrikeThrough(JTextPane textPane) {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();
        if (start == end) {
            return; 
        }

        boolean isStrike = isStrike(textPane, start);
        Style strikeStyle = textPane.addStyle(STRIKE_STYLE_NAME, null);
        StyleConstants.setStrikeThrough(strikeStyle, !isStrike);

        StyledDocument styledDocument = textPane.getStyledDocument();
        styledDocument.setCharacterAttributes(start, end - start, strikeStyle, false);
    }

    private boolean isStrike(JTextPane textPane, int start) {
        StyledDocument doc = textPane.getStyledDocument();
        Element element = doc.getCharacterElement(start);
        AttributeSet as = element.getAttributes();
        return StyleConstants.isStrikeThrough(as);
    }
}
