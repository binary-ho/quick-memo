package com.quickmemo.plugin.window;

import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.quickmemo.plugin.application.MemoRepository;
import com.quickmemo.plugin.application.MemoService;
import com.quickmemo.plugin.constant.DialogConstants;
import com.quickmemo.plugin.infrastructure.MemoState;
import com.quickmemo.plugin.infrastructure.MemoStateRepository;
import com.quickmemo.plugin.memo.CurrentMemo;
import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.memo.MemoLimitExceededException;
import com.quickmemo.plugin.ui.ToastPopup;
import com.quickmemo.plugin.window.action.MemoActionManager;
import com.quickmemo.plugin.window.component.MemoEditor;
import com.quickmemo.plugin.window.component.MemoList;
import com.quickmemo.plugin.window.component.MemoToolbar;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class MemoToolWindow {
    private final JPanel content;
    private final MemoService memoService;
    private final MemoEditor editor;
    private final MemoList memoList;
    private final MemoActionManager actionManager;
    private final JPanel centerPanel;

    private JBPopup memoListPopup;
    private CurrentMemo currentMemo = CurrentMemo.UNSELECTED;

    private static final String LAYOUT_MEMO = "MEMO";
    private static final String LAYOUT_EMPTY = "EMPTY";
    private static final String EMPTY_STATE_NO_MEMO = "Click '+' to write it out";
    private static final String EMPTY_STATE_NO_MEMOS_IN_LIST = "Empty";

    private static final JBLabel EMPTY_LABEL = getEmptyLabel();

    public MemoToolWindow(Project project) {
        this.memoService = getMemoService(project);
        this.content = new JPanel(new BorderLayout());
        this.editor = new MemoEditor();
        this.memoList = new MemoList();
        this.actionManager = new MemoActionManager(this);
        this.centerPanel = new JPanel(new CardLayout());
        
        initializeLayout();
        initializeListeners();
        initializeState();
    }

    private void initializeLayout() {
        // 중앙 패널 설정
        centerPanel.add(editor, LAYOUT_MEMO);
        centerPanel.add(EMPTY_LABEL, LAYOUT_EMPTY);

        // 툴바 설정
        MemoToolbar toolbar = MemoToolbar.initializeWithActionManager(actionManager, content);
        
        // 전체 레이아웃 구성
        content.add(toolbar, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
    }

    private void initializeListeners() {
        memoList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Memo selectedMemo = memoList.getSelectedMemo();
                if (selectedMemo == null) {
                    return;
                }

                currentMemo = CurrentMemo.of(selectedMemo);
                editor.setText(currentMemo.getMemo().getContent());
                showMemoState();

                if (memoListPopup != null && memoListPopup.isVisible()) {
                    memoListPopup.cancel();
                }
            }
        });

        editor.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                saveContent();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                saveContent();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                saveContent();
            }
        });
    }

    private void initializeState() {
        refreshMemoList();
        if (!memoList.isEmpty()) {
            selectMemoById(memoList.getElementAt(0).getId());
        } else {
            showEmptyState();
        }
    }

    private static JBLabel getEmptyLabel() {
        JBLabel emptyLabel = new JBLabel(EMPTY_STATE_NO_MEMO, SwingConstants.CENTER);
        emptyLabel.setFont(emptyLabel.getFont().deriveFont((float) JBUI.scale(14)));
        emptyLabel.setForeground(JBUI.CurrentTheme.Label.disabledForeground());
        return emptyLabel;
    }

    public void showMemoListPopup() {
        if (memoListPopup != null && memoListPopup.isVisible()) {
            memoListPopup.cancel();
            return;
        }

        JPanel listPanel = getListPanel();
        memoListPopup = buildMemoListPopup(listPanel);
        Component rightToolbar = ((JPanel)content.getComponent(0)).getComponent(1);
        memoListPopup.showUnderneathOf(rightToolbar);
    }

    private @NotNull JPanel getListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        if (isMemoListEmpty()) {
            JBLabel emptyListLabel = new JBLabel(EMPTY_STATE_NO_MEMOS_IN_LIST, SwingConstants.CENTER);
            emptyListLabel.setFont(emptyListLabel.getFont().deriveFont((float) JBUI.scale(14)));
            emptyListLabel.setForeground(JBUI.CurrentTheme.Label.disabledForeground());
            listPanel.add(emptyListLabel, BorderLayout.CENTER);
            return listPanel;
        }

        refreshMemoList();
        listPanel.add(new JBScrollPane(memoList.getList()), BorderLayout.CENTER);
        return listPanel;
    }

    private boolean isMemoListEmpty() {
        List<Memo> memos = memoService.getAllMemos();
        return memos.isEmpty();
    }

    private @NotNull JBPopup buildMemoListPopup(JPanel listPanel) {
        return JBPopupFactory.getInstance()
                .createComponentPopupBuilder(listPanel, memoList.getList())
                .setTitle(DialogConstants.MEMO_LIST_TITLE)
                .setMovable(true)
                .setResizable(true)
                .setMinSize(new Dimension(200, 300))
                .createPopup();
    }

    private MemoService getMemoService(Project project) {
        MemoState state = project.getService(MemoState.class);
        MemoRepository repository = new MemoStateRepository(state);
        return new MemoService(repository);
    }

    public void createNewMemo() {
        try {
            String id = memoService.createEmptyMemo();
            refreshMemoList();
            selectMemoById(id);
            editor.requestFocusOnEditor();
            
            ActionButton addButton = findAddButton();
            if (addButton != null) {
                showToast(addButton);
            }
        } catch (MemoLimitExceededException e) {
            Messages.showWarningDialog(
                DialogConstants.MEMO_LIMIT_REACHED_WARNING_MESSAGE,
                DialogConstants.MEMO_LIMIT_REACHED_WARNING_TITLE
            );
        }
    }

    private ActionButton findAddButton() {
        ActionToolbar leftToolbar = ((MemoToolbar)content.getComponent(0)).getLeftToolbar();
        for (Component comp : leftToolbar.getComponent().getComponents()) {
            if (comp instanceof ActionButton && ((ActionButton) comp).getAction() == actionManager.getAddAction()) {
                return (ActionButton) comp;
            }
        }
        return null;
    }

    private void showToast(JComponent anchor) {
        SwingUtilities.invokeLater(() -> {
            ToastPopup popup = new ToastPopup(DialogConstants.MEMO_CREATED_TOAST, anchor);
            popup.showToast();
        });
    }

    public void deleteSelectedMemo() {
        if (currentMemo.isUnselected()) {
            return;
        }

        Memo memoToDelete = currentMemo.getMemo();
        memoService.deleteMemo(memoToDelete);

        SwingUtilities.invokeLater(() -> {
            currentMemo = CurrentMemo.UNSELECTED;
            refreshMemoList();

            List<Memo> remainingMemos = memoService.getAllMemos();
            if (!remainingMemos.isEmpty()) {
                memoList.setSelectedIndex(0);
                showMemoState();
            } else {
                editor.setText("");
                showEmptyState();
            }
        });
    }

    private void showMemoState() {
        ((CardLayout) centerPanel.getLayout()).show(centerPanel, LAYOUT_MEMO);
    }

    private void showEmptyState() {
        ((CardLayout) centerPanel.getLayout()).show(centerPanel, LAYOUT_EMPTY);
    }

    private void refreshMemoList() {
        List<Memo> allMemos = this.memoService.getAllMemos();
        memoList.setMemos(allMemos);
    }

    private void selectMemoById(String id) {
        for (int i = 0; i < memoList.getListSize(); i++) {
            if (memoList.getElementAt(i).getId().equals(id)) {
                memoList.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveContent() {
        try {
            if (currentMemo.isUnselected()) {
                return;
            }

            Memo updatedMemo = new Memo(
                currentMemo.getMemo().getId(),
                editor.getText(),
                currentMemo.getMemo().getCreatedAt()
            );
            memoService.updateMemo(updatedMemo);
            refreshMemoList();
            memoList.getList().repaint();
        } catch (IllegalArgumentException e) {
            Messages.showErrorDialog(
                DialogConstants.MEMO_SIZE_LIMIT_REACHED_ERROR_MESSAGE,
                DialogConstants.MEMO_SIZE_LIMIT_REACHED_ERROR_TITLE
            );
        }
    }

    public JPanel getContent() {
        return content;
    }

    public CurrentMemo getCurrentMemo() {
        return currentMemo;
    }
}
