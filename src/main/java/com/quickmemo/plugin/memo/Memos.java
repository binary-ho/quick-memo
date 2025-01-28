package com.quickmemo.plugin.memo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.quickmemo.plugin.memo.MemoConstants.MAX_MEMO_COUNT;

public class Memos {
    private final Map<String, Memo> memos;

    public Memos() {
        this.memos = new HashMap<>();
    }

    public List<Memo> getAll() {
        return new LinkedList<>(memos.values());
    }

    public void add(Memo memo) {
        validateMemoLimit(memos.size());
        memos.putIfAbsent(memo.id(), memo);
    }

    public void addAll(List<Memo> memos) {
        int totalSize = this.memos.size() + memos.size();
        validateMemoLimit(totalSize);
        for (Memo memo : memos) {
            add(memo);
        }
    }

    private void validateMemoLimit(int memoSize) {
        if (memoSize >= MAX_MEMO_COUNT) {
            throw new MemoLimitExceededException(
                String.format("Cannot add more memos. Maximum limit is %d memos", MAX_MEMO_COUNT)
            );
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

    public int size() {
        return memos.size();
    }

    public int remainingCapacity() {
        return MAX_MEMO_COUNT - memos.size();
    }
}
