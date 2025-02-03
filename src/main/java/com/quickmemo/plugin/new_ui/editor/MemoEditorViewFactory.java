package com.quickmemo.plugin.new_ui.editor;

import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.new_ui.memo.SelectedMemo;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

public class MemoEditorViewFactory {

    public static MemoEditorView create(
            MemoEditor memoEditor,
            SelectedMemo selectedMemo,
            Consumer<Memo> saveMemoConsumer,
            Runnable onMemoUpdateFailed
    ) {
        EmptyMemoViewLabel emptyMemoViewLabel = new EmptyMemoViewLabel();

        MemoEditorView editorView = new MemoEditorView(memoEditor, emptyMemoViewLabel, selectedMemo);
        selectedMemo.addListener(editorView::updateEditorView);

        addListenerToEditorView(editorView, selectedMemo, saveMemoConsumer, onMemoUpdateFailed);
        return editorView;
    }

    private static void addListenerToEditorView(MemoEditorView editorView, SelectedMemo selectedMemo, Consumer<Memo> saveMemoConsumer, Runnable onMemoUpdateFailed) {
        editorView.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                saveEditorContentToMemo(selectedMemo, editorView, saveMemoConsumer, onMemoUpdateFailed);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                saveEditorContentToMemo(selectedMemo, editorView, saveMemoConsumer, onMemoUpdateFailed);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                saveEditorContentToMemo(selectedMemo, editorView, saveMemoConsumer, onMemoUpdateFailed);
            }
        });
    }

    private static void saveEditorContentToMemo(SelectedMemo selectedMemo, MemoEditorView editorView, Consumer<Memo> saveMemoConsumer, Runnable onMemoUpdateFailed) {
        if (selectedMemo.isUnselected()) {
            return;
        }

        Memo memo = selectedMemo.getMemo();
        String content = editorView.getEditorContent();
        try {
            Memo updatedMemo = memo.copyWithContent(content);
            saveMemoConsumer.accept(updatedMemo);
        } catch (IllegalArgumentException e) {
            onMemoUpdateFailed.run();
        }
    }
}
