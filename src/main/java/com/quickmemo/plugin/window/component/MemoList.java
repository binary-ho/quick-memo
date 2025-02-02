package com.quickmemo.plugin.window.component;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.window.MemoListTitleRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class MemoList extends JPanel {
    private final DefaultListModel<Memo> listModel;
    private final JBList<Memo> memoList;

    public MemoList() {
        this.listModel = new DefaultListModel<>();
        this.memoList = new JBList<>(listModel);
        memoList.setCellRenderer(new MemoListTitleRenderer());
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JBScrollPane(memoList));
    }

    public void setMemos(List<Memo> memos) {
        listModel.clear();
        memos.stream()
            .sorted((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()))
            .forEach(listModel::addElement);
        memoList.revalidate();
        memoList.repaint();
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        memoList.addListSelectionListener(listener);
    }

    public Memo getSelectedMemo() {
        return memoList.getSelectedValue();
    }

    public void setSelectedIndex(int index) {
        memoList.setSelectedIndex(index);
    }

    public boolean isEmpty() {
        return listModel.isEmpty();
    }

    public Memo getElementAt(int index) {
        return listModel.getElementAt(index);
    }

    public int getListSize() {
        return listModel.getSize();
    }

    public JBList<Memo> getList() {
        return memoList;
    }
}
