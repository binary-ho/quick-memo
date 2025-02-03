package com.quickmemo.plugin.new_ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.quickmemo.plugin.application.MemoRepository;
import com.quickmemo.plugin.application.MemoService;
import com.quickmemo.plugin.infrastructure.MemoState;
import com.quickmemo.plugin.infrastructure.MemoStateRepository;
import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.new_ui.button.CreateMemoButton;
import com.quickmemo.plugin.new_ui.button.DeleteMemoButton;
import com.quickmemo.plugin.new_ui.button.OpenMemoListButton;
import com.quickmemo.plugin.new_ui.editor.MemoEditor;
import com.quickmemo.plugin.new_ui.editor.MemoEditorView;
import com.quickmemo.plugin.new_ui.editor.MemoEditorViewFactory;
import com.quickmemo.plugin.new_ui.popup.MemoListPopup;
import com.quickmemo.plugin.new_ui.popup.MemoListPopupFactory;
import com.quickmemo.plugin.new_ui.toast.CreatedMemoToast;
import com.quickmemo.plugin.new_ui.toolbar.MainToolbar;
import com.quickmemo.plugin.new_ui.toolbar.ToolbarFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

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
        }, CreatedMemoToast::show);

        // deleteMemoButton
        DeleteMemoButton deleteMemoButton = new DeleteMemoButton(() -> {
            Memo memo = selectedMemo.getMemo();
            memoService.deleteMemo(memo);
            Memo first = memoService.getAllMemos()
                    .stream()
                    .findFirst()
                    .orElseGet(Memo::createEmptyMemo);
            selectedMemo.update(first);
        });

        // main window
        QuickMemoWindow quickMemoWindow = new QuickMemoWindow();
        MainToolbar quickMemoToolbar = ToolbarFactory.createQuickMemoToolbar(quickMemoWindow.getContent(), createMemoButton, deleteMemoButton, openMemoListButton);
        MemoEditorView memoEditorView = MemoEditorViewFactory.create(editor, selectedMemo, memoService::updateMemo);

        JPanel mainWindowContent = quickMemoWindow.getContent();
        mainWindowContent.add(quickMemoToolbar.getMemoToolbar(), BorderLayout.NORTH);
        mainWindowContent.add(memoEditorView, BorderLayout.CENTER);

        // window setting
        mainWindowContent.setPreferredSize(DEFAULT_DIMENSION_SIZE);
        mainWindowContent.setSize(DEFAULT_DIMENSION_SIZE);

        Content content = ContentFactory.getInstance()
                .createContent(mainWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

        // TODO: Editor가 스스로 감지하도록 변경
        toolWindow.setType(ToolWindowType.FLOATING, null);
        toolWindow.getContentManager().addContent(content);
        addEditorOpenListener(project);
    }

    private MemoService createMemoService(Project project) {
        MemoState state = project.getService(MemoState.class);
        MemoRepository repository = new MemoStateRepository(state);
        return new MemoService(repository);
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
                    com.quickmemo.plugin.window.component.MemoEditor memoEditor = com.quickmemo.plugin.window.component.MemoEditor.getInstance();
                    memoEditor.requestFocusOnEditor();
                });
            }
        };
    }
}
