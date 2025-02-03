package com.quickmemo.plugin.new_ui.memo;

import com.quickmemo.plugin.memo.Memo;

import java.time.LocalDateTime;

public class EmptyMemo {
    public static final Memo EMPTY_MEMO = createEmptyMemo();
    private static Memo createEmptyMemo() {
        return Memo.createFrom("", LocalDateTime.now());
    }
}
