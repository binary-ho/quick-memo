package com.quickmemo.plugin.ui.memo;

import com.quickmemo.plugin.memo.Memo;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.quickmemo.plugin.ui.memo.EmptyMemo.EMPTY_MEMO;

public class SelectedMemo {
    private static final Memo UNSELECTED_MEMO = EMPTY_MEMO;
    private Memo selectedMemo;

    private static final SelectedMemo UNSELECTED = new SelectedMemo(UNSELECTED_MEMO);
    private static final SelectedMemo INSTANCE = UNSELECTED;

    private final List<Consumer<SelectedMemo>> listeners = new LinkedList<>();

    private SelectedMemo(Memo selectedMemo) {
        this.selectedMemo = selectedMemo;
    }

    public static SelectedMemo getInstance() {
        return INSTANCE;
    }

    public void addListener(Consumer<SelectedMemo> listener) {
        listeners.add(listener);
    }

    public void update(Memo memo) {
        synchronized (this) {
            this.selectedMemo = memo;
            if (memo == null || memo.getId() == null) {
                this.selectedMemo = UNSELECTED_MEMO;
            }
            notifyListeners(this);
        }
    }

    private void notifyListeners(SelectedMemo selectedMemo) {
        for (Consumer<SelectedMemo> listener : listeners) {
            listener.accept(selectedMemo);
        }
    }

    public boolean isUnselected() {
        Memo memo = this.getMemo();
        return memo == null || memo.equals(UNSELECTED_MEMO);
    }

    public Memo getMemo() {
        return selectedMemo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SelectedMemo that = (SelectedMemo) obj;
        return this.getMemo().equals(that.getMemo());
    }
}
