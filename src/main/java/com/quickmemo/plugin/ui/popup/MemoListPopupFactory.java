package com.quickmemo.plugin.ui.popup;

import com.quickmemo.plugin.application.MemoService;
import com.quickmemo.plugin.ui.memo.SelectedMemo;

public class MemoListPopupFactory {
    public static MemoListPopup create(MemoService memoService, SelectedMemo selectedMemo) {
        MemoListPopupRenderer memoListPopupRenderer = new MemoListPopupRenderer();
        MemoListView listView = new MemoListView(memoListPopupRenderer);
        return new MemoListPopup(memoService, listView, selectedMemo::update);
    }
}
