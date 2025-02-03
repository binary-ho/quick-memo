package com.quickmemo.plugin.new_ui;

import com.quickmemo.plugin.memo.Memo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class SelectedMemo {
    private static final Memo EMPTY_MEMO = Memo.createFrom("", LocalDateTime.now());
    private Memo selectedMemo;

    private static final SelectedMemo UNSELECTED = new SelectedMemo(EMPTY_MEMO);
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
            notifyListeners(this);
        }
    }

    private void notifyListeners(SelectedMemo selectedMemo) {
        for (Consumer<SelectedMemo> listener : listeners) {
            listener.accept(selectedMemo);
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
}
