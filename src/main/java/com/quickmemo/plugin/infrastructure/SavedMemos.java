package com.quickmemo.plugin.infrastructure;

import java.util.ArrayList;
import java.util.List;

public class SavedMemos {
    public List<SavedMemo> memos = new ArrayList<>();

    public static SavedMemos from(List<SavedMemo> memos) {
        SavedMemos savedMemos = new SavedMemos();
        savedMemos.memos = memos;
        return savedMemos;
    }

    public static class SavedMemo {
        public String id;
        public String content;
        public String createdAt;

        public SavedMemo() {
        }

        public static SavedMemo of(String id, String content, String createdAt) {
            SavedMemo savedMemo = new SavedMemo();
            savedMemo.id = id;
            savedMemo.content = content;
            savedMemo.createdAt = createdAt;
            return savedMemo;
        }
    }
}
