package com.quickmemo.plugin.window;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.quickmemo.plugin.application.MemoService;
import com.quickmemo.plugin.infrastructure.MemoState;
import com.quickmemo.plugin.infrastructure.MemoStateRepository;
import com.quickmemo.plugin.memo.Memo;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class MemoToolWindow {
    private final JPanel content;
    private final MemoService memoService;

    private final DefaultListModel<Memo> listModel;
    private final JBList<Memo> memos;

    private final JBTextArea textArea;
    private Memo currentMemo;

    public MemoToolWindow(Project project) {
        this.memoService = getMemoService(project);
        this.content = new JPanel(new BorderLayout());
        
        // 왼쪽 패널: 메모 목록
        this.listModel = new DefaultListModel<>();
        this.memos = new JBList<>(listModel);
        memos.setCellRenderer(new MemoListCellRenderer());

        // 오른쪽 패널: 메모 내용
        this.textArea = new JBTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        // 메모 선택 시 내용 표시
        memos.addListSelectionListener(this::onMemoSelected);
        
        // 메모 내용 변경 시 자동 저장
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

        // 툴바 추가
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AnAction("New Memo", "Create new memo", AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                createNewMemo();
            }
        });

        actionGroup.add(new AnAction("Delete Memo", "Delete selected memo", AllIcons.General.Remove) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                deleteSelectedMemo();
            }
        });
        
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("MemoToolbar", actionGroup, true);
        
        // 레이아웃 구성
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JBScrollPane(memos),
                new JBScrollPane(textArea));
        splitPane.setDividerLocation(200);
        
        content.add(toolbar.getComponent(), BorderLayout.NORTH);
        content.add(splitPane, BorderLayout.CENTER);
        
        // 첫 번째 메모 선택
        if (!listModel.isEmpty()) {
            memos.setSelectedIndex(0);
        }
    }

    private @NotNull MemoService getMemoService(Project project) {
        final MemoService memoService;
        MemoState state = project.getService(MemoState.class);
        MemoStateRepository repository = new MemoStateRepository(state);
        memoService = new MemoService(repository);
        return memoService;
    }

    private void createNewMemo() {
        String id = memoService.createEmptyMemo();
        refreshMemoList();
        selectMemoById(id);
    }

    private void deleteSelectedMemo() {
        if (currentMemo != null) {
            memoService.deleteMemo(currentMemo);
            refreshMemoList();
            if (!listModel.isEmpty()) {
                memos.setSelectedIndex(0);
            } else {
                textArea.setText("");
                currentMemo = null;
            }
        }
    }

    public void refreshMemoList() {
        List<Memo> allMemos = this.memoService.getAllMemos();
        listModel.clear();
        allMemos.forEach(this.listModel::addElement);
    }

    private void onMemoSelected(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            Memo selectedMemo = memos.getSelectedValue();
            if (selectedMemo != null) {
                currentMemo = selectedMemo;
                textArea.setText(selectedMemo.content());
            }
        }
    }

    private void selectMemoById(String id) {
        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.getElementAt(i).id().equals(id)) {
                memos.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveContent() {
        if (currentMemo != null) {
            currentMemo = new Memo(currentMemo.id(), textArea.getText(), currentMemo.createdAt());
            memoService.updateMemo(currentMemo);
            refreshMemoList();
            memos.repaint();
        }
    }

    public JPanel getContent() {
        return content;
    }
}
