package com.quickmemo.plugin.new_ui.editor;

import com.quickmemo.plugin.new_ui.memo.SelectedMemo;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MemoEditorView extends JPanel {
    private final MemoEditor editor;

    private static final String LAYOUT_MEMO = "MEMO";
    private static final String LAYOUT_EMPTY = "EMPTY";
    private static final CardLayout EDITOR_VIEW_LAYOUT = new CardLayout();

    public MemoEditorView(MemoEditor editor, EmptyMemoViewLabel emptyMemoViewLabel, SelectedMemo selectedMemo) {
        super(EDITOR_VIEW_LAYOUT);
        this.editor = editor;

        addComponentToView(this.editor, LAYOUT_MEMO);
        addComponentToView(emptyMemoViewLabel, LAYOUT_EMPTY);

        updateEditorView(selectedMemo);
    }

    private void addComponentToView(Component component, String name) {
        this.add(component, name);
    }

    public void updateEditorView(final SelectedMemo memo) {
        if (memo.isUnselected()) {
            emptyEditorContent();
            showEmptyMemo();
            return;
        }

        setEditorContent(memo.getMemo().getContent());
        showMemoEditor();
        requestFocusOnEditor();
    }

    private void showMemoEditor() {
        ((CardLayout) getLayout()).show(this, LAYOUT_MEMO);
    }

    private void showEmptyMemo() {
        ((CardLayout) getLayout()).show(this, LAYOUT_EMPTY);
    }

    private void setEditorContent(String text) {
        editor.setText(text);
    }

    private void emptyEditorContent() {
        editor.setText("");
    }

    public String getEditorContent() {
        return editor.getText();
    }

    public void addDocumentListener(DocumentListener listener) {
        editor.addDocumentListener(listener);
    }

    public void requestFocusOnEditor() {
        editor.requestFocusOnEditor();
    }
}
