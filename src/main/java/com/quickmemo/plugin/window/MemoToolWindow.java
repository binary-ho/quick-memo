package com.quickmemo.plugin.window;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
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
    private JBPopup memoListPopup;

    private final JBTextArea textArea;
    private Memo currentMemo;

    public MemoToolWindow(Project project) {
        this.memoService = getMemoService(project);
        this.content = new JPanel(new BorderLayout());
        
        // 메모 목록 준비
        this.listModel = new DefaultListModel<>();
        this.memos = new JBList<>(listModel);
        memos.setCellRenderer(new MemoListCellRenderer());

        // 메모 내용 영역
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
        DefaultActionGroup leftGroup = new DefaultActionGroup();
        DefaultActionGroup rightGroup = new DefaultActionGroup();
        
        // 새 메모 버튼과 삭제 버튼 (왼쪽)
        leftGroup.add(new AnAction("New Memo", "Create new memo", AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                createNewMemo();
            }
        });

        leftGroup.add(new AnAction("Delete Memo", "Delete selected memo", AllIcons.General.Remove) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                if (currentMemo != null) {
                    int result = Messages.showYesNoDialog(
                            "정말 삭제하시겠습니까?",
                            "메모 삭제",
                            "삭제!",
                            "취소",
                            Messages.getQuestionIcon());
                    
                    if (result == Messages.YES) {
                        deleteSelectedMemo();
                    }
                }
            }
        });

        // 메모 목록 보기 버튼 (오른쪽)
        rightGroup.add(new AnAction("Show Memo List", "Show all memos", AllIcons.Actions.Minimap) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                showMemoListPopup();
            }
        });
        
        ActionToolbar leftToolbar = ActionManager.getInstance()
                .createActionToolbar("MemoToolbarLeft", leftGroup, true);
        ActionToolbar rightToolbar = ActionManager.getInstance()
                .createActionToolbar("MemoToolbarRight", rightGroup, true);
        
        leftToolbar.setTargetComponent(content);
        rightToolbar.setTargetComponent(content);
        
        // 툴바 설정
        leftToolbar.setMiniMode(true);
        rightToolbar.setMiniMode(true);
        
        // 툴바 컴포넌트의 여백 제거
        JComponent rightToolbarComponent = rightToolbar.getComponent();
        rightToolbarComponent.setBorder(JBUI.Borders.empty());
        rightToolbarComponent.setOpaque(false);
        
        // 툴바를 담을 패널 (여백 없이)
        JPanel toolbarPanel = new JPanel() {
            @Override
            public void addNotify() {
                super.addNotify();
                if (rightToolbarComponent.getParent() instanceof JComponent parent) {
                    parent.setBorder(JBUI.Borders.empty());
                    parent.setOpaque(false);
                }
            }
        };
        toolbarPanel.setLayout(new BorderLayout(0, 0));
        toolbarPanel.setBorder(JBUI.Borders.empty());
        toolbarPanel.setOpaque(false);
        toolbarPanel.add(leftToolbar.getComponent(), BorderLayout.WEST);
        toolbarPanel.add(rightToolbar.getComponent(), BorderLayout.EAST);
        
        // 레이아웃 구성
        content.add(toolbarPanel, BorderLayout.NORTH);
        content.add(new JBScrollPane(textArea), BorderLayout.CENTER);
        
        // 첫 번째 메모 선택
        refreshMemoList();
        if (!listModel.isEmpty()) {
            selectMemoById(listModel.getElementAt(0).id());
        }
    }

    private void showMemoListPopup() {
        if (memoListPopup != null && memoListPopup.isVisible()) {
            memoListPopup.cancel();
            return;
        }

        // 메모 목록 패널 생성
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JBScrollPane(memos), BorderLayout.CENTER);
        
        // 팝업 생성
        memoListPopup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(listPanel, memos)
                .setTitle("Memo List")
                .setMovable(true)
                .setResizable(true)
                .setMinSize(new Dimension(200, 300))
                .createPopup();

        // 리스트 버튼 기준으로 팝업 표시
        Component toolbarComponent = content.getComponent(0);
        Component rightToolbar = ((JPanel)toolbarComponent).getComponent(1); // East 컴포넌트
        memoListPopup.showUnderneathOf(rightToolbar);
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
                if (memoListPopup != null && memoListPopup.isVisible()) {
                    memoListPopup.cancel();
                }
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
