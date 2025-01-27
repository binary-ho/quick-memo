package com.quickmemo.plugin.memo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Memos {
    private final Map<String, Memo> memos;

    public Memos() {
        this.memos = new HashMap<>();
    }

    public void add(Memo memo) {
        memos.put(memo.getId(), memo);
    }

    public void remove(String key) {
        memos.remove(key);
    }

    public List<Memo> getAllMemos() {
        return new LinkedList<>(memos.values());
    }

    public boolean isEmpty() {
        return memos.isEmpty();
    }
}
