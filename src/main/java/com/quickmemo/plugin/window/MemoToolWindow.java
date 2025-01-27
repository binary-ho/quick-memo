package com.quickmemo.plugin.window;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBList;
import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.service.MemoService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class MemoToolWindow {
    private final JPanel content;
    private final MemoService memoService;
    private final JBList<Memo> memoList;
    private final JBTextArea textArea;
    private final DefaultListModel<Memo> listModel;
    private String currentMemoId;

    public MemoToolWindow(Project project) {
        memoService = project.getService(MemoService.class);
        content = new JPanel(new BorderLayout());
        
        // 초기 메모 생성
        memoService.initializeDefaultMemoIfEmpty();
        
        // 왼쪽 패널: 메모 목록
        listModel = new DefaultListModel<>();
        memoList = new JBList<>(listModel);
        memoList.setCellRenderer(new MemoListCellRenderer());
        refreshMemoList();
        
        // 오른쪽 패널: 메모 내용
        textArea = new JBTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        // 메모 선택 시 내용 표시
        memoList.addListSelectionListener(this::onMemoSelected);
        
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
                new JBScrollPane(memoList),
                new JBScrollPane(textArea));
        splitPane.setDividerLocation(200);
        
        content.add(toolbar.getComponent(), BorderLayout.NORTH);
        content.add(splitPane, BorderLayout.CENTER);
        
        // 첫 번째 메모 선택
        if (!listModel.isEmpty()) {
            memoList.setSelectedIndex(0);
        }
    }

    private void createNewMemo() {
        String id = memoService.createMemo("New Memo");
        refreshMemoList();
        selectMemoById(id);
    }

    private void deleteSelectedMemo() {
        if (currentMemoId != null) {
            memoService.deleteMemo(currentMemoId);
            refreshMemoList();
            if (!listModel.isEmpty()) {
                memoList.setSelectedIndex(0);
            } else {
                textArea.setText("");
                currentMemoId = null;
            }
        }
    }

    private void refreshMemoList() {
        listModel.clear();
        List<Memo> memos = memoService.getAllMemos();
        memos.forEach(listModel::addElement);
    }

    private void onMemoSelected(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            Memo selectedMemo = memoList.getSelectedValue();
            if (selectedMemo != null) {
                currentMemoId = selectedMemo.getId();
                textArea.setText(selectedMemo.getContent());
            }
        }
    }

    private void selectMemoById(String id) {
        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.getElementAt(i).getId().equals(id)) {
                memoList.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveContent() {
        if (currentMemoId != null) {
            memoService.updateMemo(currentMemoId, textArea.getText());
            refreshMemoList();
            memoList.repaint();
        }
    }

    public JPanel getContent() {
        return content;
    }

    private static class MemoListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Memo memo) {
                String title = memo.getContent().lines().findFirst().orElse("").trim();
                if (title.isEmpty()) {
                    title = "(빈 메모)";
                }
                setText(title);
            }
            return this;
        }
    }
}
