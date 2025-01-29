package com.quickmemo.plugin.window;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.quickmemo.plugin.application.MemoService;
import com.quickmemo.plugin.constant.ActionConstants;
import com.quickmemo.plugin.constant.DialogConstants;
import com.quickmemo.plugin.infrastructure.MemoState;
import com.quickmemo.plugin.infrastructure.MemoStateRepository;
import com.quickmemo.plugin.memo.CurrentMemo;
import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.memo.MemoLimitExceededException;
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

    private final JPanel centerPanel;
    private final JBTextArea textArea;

    private static final JBLabel EMPTY_LABEL = getEmptyLabel();
    private CurrentMemo currentMemo = CurrentMemo.UNSELECTED;

    // Layout
    private static final String LAYOUT_MEMO = "MEMO";
    private static final String LAYOUT_EMPTY = "EMPTY";

    // Toolbar Names
    private static final String TOOLBAR_LEFT = "MemoToolbarLeft";
    private static final String TOOLBAR_RIGHT = "MemoToolbarRight";

    // Empty State
    private static final String EMPTY_STATE_NO_MEMO = "Click '+' to write it out";
    private static final String EMPTY_STATE_NO_MEMOS_IN_LIST = "Empty";

    public MemoToolWindow(Project project) {
        this.memoService = getMemoService(project);
        this.content = new JPanel(new BorderLayout());
        
        // 메모 목록 준비
        this.listModel = new DefaultListModel<>();
        this.memos = new JBList<>(listModel);
        memos.setCellRenderer(new MemoListCellRenderer());

        // 중앙 패널 (CardLayout 사용)
        this.centerPanel = new JPanel(new CardLayout());
        
        // 메모 내용 영역
        this.textArea = new JBTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(JBUI.Borders.empty(8, 10));

        // 중앙 패널에 컴포넌트 추가
        centerPanel.add(new JBScrollPane(textArea), LAYOUT_MEMO);
        centerPanel.add(EMPTY_LABEL, LAYOUT_EMPTY);
        
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
        leftGroup.add(new AnAction(ActionConstants.ACTION_NEW_MEMO, ActionConstants.ACTION_NEW_MEMO_DESC, AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                createNewMemo();
            }
        });

        leftGroup.add(new AnAction(ActionConstants.ACTION_DELETE_MEMO, ActionConstants.ACTION_DELETE_MEMO_DESC, AllIcons.General.Remove) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                if (currentMemo.isSelected()) {
                    int result = Messages.showYesNoDialog(
                            DialogConstants.DELETE_MEMO_CONFIRM_CONTENT,
                            DialogConstants.DELETE_MEMO_CONFIRM_TITLE,
                            DialogConstants.DELETE_MEMO_CONFIRM_YES,
                            DialogConstants.DELETE_MEMO_CONFIRM_NO,
                            Messages.getQuestionIcon());
                    
                    if (result == Messages.YES) {
                        deleteSelectedMemo();
                    }
                }
            }
        });

        // 메모 목록 보기 버튼 (오른쪽)
        rightGroup.add(new AnAction(ActionConstants.ACTION_SHOW_LIST, ActionConstants.ACTION_SHOW_LIST_DESC, AllIcons.Actions.Minimap) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                showMemoListPopup();
            }
        });
        
        ActionToolbar leftToolbar = ActionManager.getInstance()
                .createActionToolbar(TOOLBAR_LEFT, leftGroup, true);
        ActionToolbar rightToolbar = ActionManager.getInstance()
                .createActionToolbar(TOOLBAR_RIGHT, rightGroup, true);
        
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
        content.add(centerPanel, BorderLayout.CENTER);
        
        // 첫 번째 메모 선택
        refreshMemoList();
        if (!listModel.isEmpty()) {
            selectMemoById(listModel.getElementAt(0).id());
        } else {
            showEmptyState();
        }
    }

    private static @NotNull JBLabel getEmptyLabel() {
        JBLabel emptyLabel = new JBLabel(EMPTY_STATE_NO_MEMO, SwingConstants.CENTER);
        emptyLabel.setFont(emptyLabel.getFont().deriveFont((float) JBUI.scale(14)));
        emptyLabel.setForeground(JBUI.CurrentTheme.Label.disabledForeground());
        return emptyLabel;
    }

    private void showMemoListPopup() {
        if (memoListPopup != null && memoListPopup.isVisible()) {
            memoListPopup.cancel();
            return;
        }

        // 메모 목록 패널 생성
        JPanel listPanel = new JPanel(new BorderLayout());
        
        List<Memo> currentMemos = memoService.getAllMemos();
        if (currentMemos.isEmpty()) {
            JBLabel emptyListLabel = new JBLabel(EMPTY_STATE_NO_MEMOS_IN_LIST, SwingConstants.CENTER);
            emptyListLabel.setFont(emptyListLabel.getFont().deriveFont((float) JBUI.scale(14)));
            emptyListLabel.setForeground(JBUI.CurrentTheme.Label.disabledForeground());
            listPanel.add(emptyListLabel, BorderLayout.CENTER);
        } else {
            refreshMemoList();
            listPanel.add(new JBScrollPane(memos), BorderLayout.CENTER);
        }
        
        // 팝업 생성
        memoListPopup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(listPanel, memos)
                .setTitle(DialogConstants.MEMO_LIST_TITLE)
                .setMovable(true)
                .setResizable(true)
                .setMinSize(new Dimension(200, 300))
                .createPopup();

        // 리스트 버튼 기준으로 팝업 표시
        Component toolbarComponent = content.getComponent(0);
        Component rightToolbar = ((JPanel)toolbarComponent).getComponent(1);
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
        try {
            String id = memoService.createEmptyMemo();
            refreshMemoList();
            selectMemoById(id);
            textArea.requestFocus();
        } catch (MemoLimitExceededException e) {
            Messages.showWarningDialog(
                DialogConstants.MEMO_LIMIT_REACHED_WARNING_MESSAGE,
                DialogConstants.MEMO_LIMIT_REACHED_WARNING_TITLE
            );
        }
    }

    private void deleteSelectedMemo() {
        if (currentMemo.isSelected()) {
            Memo memoToDelete = currentMemo.getMemo();
            memoService.deleteMemo(memoToDelete);
            
            SwingUtilities.invokeLater(() -> {
                currentMemo = CurrentMemo.UNSELECTED;
                refreshMemoList();
                
                // 실제 메모 상태 다시 확인
                List<Memo> remainingMemos = memoService.getAllMemos();
                if (!remainingMemos.isEmpty()) {
                    memos.setSelectedIndex(0);
                    showMemoState();
                } else {
                    textArea.setText("");
                    showEmptyState();
                }
            });
        }
    }

    private void showMemoState() {
        ((CardLayout) centerPanel.getLayout()).show(centerPanel, LAYOUT_MEMO);
    }

    private void showEmptyState() {
        ((CardLayout) centerPanel.getLayout()).show(centerPanel, LAYOUT_EMPTY);
    }

    public void refreshMemoList() {
        List<Memo> allMemos = this.memoService.getAllMemos();
        listModel.clear();
        if (!allMemos.isEmpty()) {
            allMemos.stream()
                    .sorted((m1, m2) -> m2.createdAt().compareTo(m1.createdAt())) // 최신순 정렬
                    .forEach(this.listModel::addElement);
            memos.revalidate();
            memos.repaint();
        }
    }

    private void onMemoSelected(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            Memo selectedMemo = memos.getSelectedValue();
            if (selectedMemo != null) {
                currentMemo = CurrentMemo.of(selectedMemo);
                textArea.setText(currentMemo.getMemo().content());
                showMemoState();
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
        try {
            if (currentMemo.isSelected()) {
                Memo updatedMemo = new Memo(
                    currentMemo.getMemo().id(),
                    textArea.getText(),
                    currentMemo.getMemo().createdAt()
                );
                memoService.updateMemo(updatedMemo);
                refreshMemoList();
                memos.repaint();
            }
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
}
