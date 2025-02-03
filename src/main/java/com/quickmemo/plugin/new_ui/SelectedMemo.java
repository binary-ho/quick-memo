package com.quickmemo.plugin.new_ui;

import com.quickmemo.plugin.memo.Memo;

import java.time.LocalDateTime;

public class SelectedMemo {
    private static final Memo ENPTY_MEMO = Memo.from("", LocalDateTime.now());
    private Memo selectedMemo = ENPTY_MEMO;

    private static final SelectedMemo UNSELECTED = new SelectedMemo(ENPTY_MEMO);
    private static final SelectedMemo INSTANCE = UNSELECTED;

    public static SelectedMemo getInstance() {
        return INSTANCE;
    }

    public void update(Memo memo) {
        synchronized (this) {
            this.selectedMemo = memo;
        }
    }

    public boolean isUnselected() {
        return this.equals(UNSELECTED);
    }

    public Memo getMemo() {
        return selectedMemo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SelectedMemo that = (SelectedMemo) obj;
        return this.selectedMemo.equals(that.selectedMemo);
    }

    private SelectedMemo(Memo selectedMemo) {
        this.selectedMemo = selectedMemo;
    }
}
