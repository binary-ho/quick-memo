package com.quickmemo.plugin.memo;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class CurrentMemo {
    private static final Memo EMPTY_MEMO = new Memo("empty", "", LocalDateTime.now().toString());
    public static final CurrentMemo UNSELECTED = new CurrentMemo(EMPTY_MEMO);
    
    private final Memo memo;

    private CurrentMemo(@NotNull Memo memo) {
        this.memo = memo;
    }

    public static CurrentMemo of(@NotNull Memo memo) {
        return new CurrentMemo(memo);
    }
    
    public boolean isSelected() {
        return this != UNSELECTED;
    }

    public Memo getMemo() {
        return memo;
    }
}
