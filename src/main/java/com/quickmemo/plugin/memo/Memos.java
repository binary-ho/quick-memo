package com.quickmemo.plugin.memo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// TODO: 그냥 State가 Map을 가지고 있으면 안되는건가
public class Memos {
    private final Map<String, Memo> memos;

    public Memos() {
        this.memos = new HashMap<>();
    }

    public List<Memo> getAll() {
        return new LinkedList<>(memos.values());
    }

    public void add(Memo memo) {
        memos.putIfAbsent(memo.id(), memo);
    }

    public void addAll(List<Memo> memos) {
        for (Memo memo : memos) {
            add(memo);
        }
    }

    public void update(Memo memo) {
        memos.put(memo.id(), memo);
    }

    public void remove(String id) {
        memos.remove(id);
    }

    public boolean isExist(String id) {
        return memos.containsKey(id);
    }

    public boolean isEmpty() {
        return memos.isEmpty();
    }
}
