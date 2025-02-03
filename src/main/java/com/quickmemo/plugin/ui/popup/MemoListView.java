package com.quickmemo.plugin.ui.popup;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.quickmemo.plugin.memo.Memo;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class MemoListView extends JPanel {
    private final DefaultListModel<Memo> listModel;
    private final JBList<Memo> memoList;

    private static final String EMPTY_STATE_MESSAGE = "Empty";

    public MemoListView(DefaultListCellRenderer memoListTitleRenderer) {
        super(new BorderLayout());
        this.listModel = new DefaultListModel<>();
        this.memoList = createList(memoListTitleRenderer);
    }

    public void updateMemoList(List<Memo> memos) {
        super.removeAll();

        if (memos.isEmpty()) {
            showEmptyState();
            return;
        }

        updateMemoSortedByCreatedAt(memos);
        super.add(new JBScrollPane(memoList), BorderLayout.CENTER);
        super.revalidate();
        super.repaint();
    }

    private JBList<Memo> createList(DefaultListCellRenderer memoListTitleRenderer) {
        JBList<Memo> list = new JBList<>(listModel);
        list.setCellRenderer(memoListTitleRenderer);
        return list;
    }

    private void showEmptyState() {
        JBLabel emptyLabel = new JBLabel(EMPTY_STATE_MESSAGE, SwingConstants.CENTER);
        emptyLabel.setFont(emptyLabel.getFont().deriveFont((float) JBUI.scale(14)));
        emptyLabel.setForeground(JBUI.CurrentTheme.Label.disabledForeground());
        super.add(emptyLabel, BorderLayout.CENTER);
    }

    private void updateMemoSortedByCreatedAt(List<Memo> memos) {
        listModel.clear();
        memos.stream()
            .sorted((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()))
            .forEach(listModel::addElement);
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        memoList.addListSelectionListener(listener);
    }

    public Memo getSelectedMemo() {
        return memoList.getSelectedValue();
    }

    public JBList<Memo> getList() {
        return memoList;
    }
}
