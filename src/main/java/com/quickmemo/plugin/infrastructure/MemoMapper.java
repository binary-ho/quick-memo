package com.quickmemo.plugin.infrastructure;

import com.quickmemo.plugin.memo.Memo;

import java.time.LocalDateTime;

public class MemoMapper {
    public static SavedMemos.SavedMemo toSavedMemo(Memo memo) {
        return SavedMemos.SavedMemo.of(
                memo.getId(),
                memo.getContent(),
                memo.getCreatedAt().toString()
        );
    }

    public static Memo toMemo(SavedMemos.SavedMemo savedMemo) {
        LocalDateTime createdAt = LocalDateTime.parse(savedMemo.createdAt);
        return new Memo(savedMemo.id, savedMemo.content, createdAt);
    }
}
