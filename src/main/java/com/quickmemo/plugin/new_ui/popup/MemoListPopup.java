package com.quickmemo.plugin.new_ui.popup;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.quickmemo.plugin.application.MemoService;
import com.quickmemo.plugin.constant.DialogConstants;
import com.quickmemo.plugin.memo.Memo;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class MemoListPopup {
    private final MemoListView listView;
    private JBPopup popupLayout;
    private final MemoService memoService;

    private static final String EMPTY_STATE_NO_MEMOS_IN_LIST = "Empty";
    private static final int MIN_WIDTH = 200;
    private static final int MIN_HEIGHT = 300;

    public MemoListPopup(MemoService memoService, MemoListView listView, Consumer<Memo> onMemoSelected) {
        this.listView = listView;
        this.memoService = memoService;
        addOnMemoSelected(this.listView, onMemoSelected);
    }

    private void addOnMemoSelected(MemoListView listView, Consumer<Memo> onMemoSelected) {
        listView.addListSelectionListener(event -> {
            if (event.getValueIsAdjusting()) {
                return;
            }

            Memo selectedMemo = listView.getSelectedMemo();
            if (selectedMemo != null) {
                onMemoSelected.accept(selectedMemo);
                closePopupIfOpen();
            }
        });
    }

    // TODO: 입력이 RightToolbar 위치가 들어오면 된다.
    public void open(Component anchor) {
        closePopupIfOpen();

        List<Memo> allMemos = memoService.getAllMemos();
        listView.updateMemoList(allMemos);

        JPanel listPanel = getListPanel(allMemos, listView.getList());
        JBList<Memo> list = listView.getList();

        popupLayout = createPopup(listPanel, list);
        popupLayout.showUnderneathOf(anchor);
    }

    private JPanel getListPanel(List<Memo> allMemos, JBList<Memo> list) {
        if (allMemos.isEmpty()) {
            return getEmptyListPanel();
        }

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JBScrollPane(list), BorderLayout.CENTER);
        return listPanel;
    }

    private static JPanel getEmptyListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        JBLabel emptyListLabel = new JBLabel(EMPTY_STATE_NO_MEMOS_IN_LIST, SwingConstants.CENTER);
        emptyListLabel.setFont(emptyListLabel.getFont().deriveFont((float) JBUI.scale(14)));
        emptyListLabel.setForeground(JBUI.CurrentTheme.Label.disabledForeground());
        listPanel.add(emptyListLabel, BorderLayout.CENTER);
        return listPanel;
    }

    private JBPopup createPopup(JPanel listPanel, JBList<Memo> list) {
        return JBPopupFactory.getInstance()
                .createComponentPopupBuilder(listPanel, list)
                .setTitle(DialogConstants.MEMO_LIST_TITLE)
                .setMovable(true)
                .setResizable(true)
                .setMinSize(new Dimension(MIN_WIDTH, MIN_HEIGHT))
                .createPopup();
    }

    private void closePopupIfOpen() {
        if (popupLayout != null && popupLayout.isVisible()) {
            popupLayout.cancel();
        }
    }
}
