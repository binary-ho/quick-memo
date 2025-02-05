package com.quickmemo.plugin.ui.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.messages.MessageBusConnection;
import com.quickmemo.plugin.application.MemoRepository;
import com.quickmemo.plugin.application.MemoService;
import com.quickmemo.plugin.infrastructure.MemoState;
import com.quickmemo.plugin.infrastructure.MemoStateRepository;
import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.ui.button.CreateMemoButton;
import com.quickmemo.plugin.ui.button.DeleteMemoButton;
import com.quickmemo.plugin.ui.button.OpenMemoListButton;
import com.quickmemo.plugin.ui.editor.MemoEditor;
import com.quickmemo.plugin.ui.editor.MemoEditorView;
import com.quickmemo.plugin.ui.editor.MemoEditorViewFactory;
import com.quickmemo.plugin.ui.memo.SelectedMemo;
import com.quickmemo.plugin.ui.dialog.*;
import com.quickmemo.plugin.ui.popup.MemoListPopup;
import com.quickmemo.plugin.ui.popup.MemoListPopupFactory;
import com.quickmemo.plugin.ui.toolbar.MainToolbar;
import com.quickmemo.plugin.ui.toolbar.ToolbarFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

import static com.quickmemo.plugin.ui.memo.EmptyMemo.EMPTY_MEMO;

public class NewMemoToolWindowFactory implements ToolWindowFactory {
    private static final int DEFAULT_SIZE = 400;
    private static final Dimension DEFAULT_DIMENSION_SIZE = new Dimension(DEFAULT_SIZE, DEFAULT_SIZE);
    public static final String WINDOW_NAME = "QuickMemo";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MemoService memoService = createMemoService(project);
        SelectedMemo selectedMemo = SelectedMemo.getInstance();

        // editor
        MemoEditor editor = MemoEditor.getInstance();

        // memo list popup
        MemoListPopup memoListPopup = MemoListPopupFactory.create(memoService, selectedMemo);

        // open button
        OpenMemoListButton openMemoListButton = new OpenMemoListButton(memoListPopup::open);

        // createMemoButton
        CreateMemoButton createMemoButton = new CreateMemoButton(() -> {
            LocalDateTime createdAt = LocalDateTime.now();
            Memo createdMemo = memoService.createEmptyMemo(createdAt);
            selectedMemo.update(createdMemo);
        }, CreatedMemoDialog::show, MemoCountErrorDialog::show);

        // deleteMemoButton
        DeleteMemoButton deleteMemoButton = new DeleteMemoButton(() -> {
            if (selectedMemo.isUnselected()) {
                NoDeleteMemoErrorDialog.show();
                return;
            }

            if (DeleteConfirm.confirm()) {
                Memo memo = selectedMemo.getMemo();
                memoService.deleteMemo(memo);
                selectFirstMemo(memoService, selectedMemo);
            }
        });

        // main window
        QuickMemoWindow quickMemoWindow = new QuickMemoWindow();
        MainToolbar quickMemoToolbar = ToolbarFactory.createQuickMemoToolbar(quickMemoWindow.getContent(), createMemoButton, deleteMemoButton, openMemoListButton);
        MemoEditorView memoEditorView = MemoEditorViewFactory.create(editor,
                selectedMemo,
                memoService::updateMemo,
                MemoContentSizeErrorDialog::show
        );

        JPanel mainWindowContent = quickMemoWindow.getContent();
        mainWindowContent.add(quickMemoToolbar.getMemoToolbar(), BorderLayout.NORTH);
        mainWindowContent.add(memoEditorView, BorderLayout.CENTER);

        // window setting
        mainWindowContent.setPreferredSize(DEFAULT_DIMENSION_SIZE);
        mainWindowContent.setSize(DEFAULT_DIMENSION_SIZE);

        // select first memo
        selectFirstMemo(memoService, selectedMemo);

        // TODO: Editor가 스스로 감지하도록 변경
        toolWindow.setType(ToolWindowType.FLOATING, null);
        Content content = ContentFactory.getInstance()
                .createContent(mainWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
        addEditorOpenListener(project, editor);

        resizeWindow(mainWindowContent);
    }

    // TODO: memoservice 가 제공 하도록 변경
    private void selectFirstMemo(MemoService memoService, SelectedMemo selectedMemo) {
        Memo first = memoService.getAllMemos()
                .stream()
                .findFirst()
                .orElse(EMPTY_MEMO);
        selectedMemo.update(first);
    }

    private MemoService createMemoService(Project project) {
        MemoState state = project.getService(MemoState.class);
        MemoRepository repository = new MemoStateRepository(state);
        return new MemoService(repository);
    }

    private void addEditorOpenListener(Project project, MemoEditor memoEditor) {
        MessageBusConnection connection = project.getMessageBus().connect();
        connection.subscribe(ToolWindowManagerListener.TOPIC, createOpenWindowListener(memoEditor));
    }

    private ToolWindowManagerListener createOpenWindowListener(MemoEditor memoEditor) {
        return OpenWindowListener.create(
                WINDOW_NAME,
                () -> SwingUtilities.invokeLater(memoEditor::requestFocusOnEditor)
        );
    }

    private void resizeWindow(JPanel mainWindowContent) {
        SwingUtilities.invokeLater(() -> {
            Timer timer = new Timer(100, event -> {
                Window win = SwingUtilities.getWindowAncestor(mainWindowContent);
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
