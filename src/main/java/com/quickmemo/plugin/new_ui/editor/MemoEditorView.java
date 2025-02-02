package com.quickmemo.plugin.new_ui.editor;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MemoEditorView extends JPanel {
    private final MemoEditor editor;
    // TODO: CurrentMemo를 여기에 두어야 하는지?

    private static final String LAYOUT_MEMO = "MEMO";
    private static final String LAYOUT_EMPTY = "EMPTY";
    public static final CardLayout EDITOR_VIEW_LAYOUT = new CardLayout();

    public MemoEditorView(MemoEditor editor, EmptyMemoLabel emptyMemoLabel) {
        super(EDITOR_VIEW_LAYOUT);
        this.editor = editor;
        addComponentToView(this.editor, LAYOUT_MEMO);
        addComponentToView(emptyMemoLabel, LAYOUT_EMPTY);
    }

    private void addComponentToView(Component component, String name) {
        this.add(component, name);
    }

    public void showMemoState() {
        ((CardLayout) getLayout()).show(this, LAYOUT_MEMO);
    }

    public void showEmptyState() {
        ((CardLayout) getLayout()).show(this, LAYOUT_EMPTY);
    }

    public MemoEditor getEditor() {
        return editor;
    }

    public void setText(String text) {
        editor.setText(text);
    }

    public String getText() {
        return editor.getText();
    }

    public void addDocumentListener(DocumentListener listener) {
        editor.addDocumentListener(listener);
    }

    public void requestFocusOnEditor() {
        editor.requestFocusOnEditor();
    }
}
