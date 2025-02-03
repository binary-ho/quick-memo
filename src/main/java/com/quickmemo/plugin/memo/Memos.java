package com.quickmemo.plugin.memo;

import java.util.*;

import static com.quickmemo.plugin.memo.MemoConstants.MAX_MEMO_COUNT;

public class Memos {
    private final Map<String, Memo> memos;

    public Memos() {
        this.memos = new HashMap<>();
    }

    public List<Memo> getAll() {
        return new LinkedList<>(memos.values());
    }

    public Memo add(Memo memo) {
        if (isIdNotEmpty(memo)) {
            return memo;
        }
        validateMemoLimit(memos.size());

        Memo assignedId = assignId(memo);
        memos.putIfAbsent(assignedId.getId(), assignedId);
        return assignedId;
    }

    // TODO: ID 생성기를 주입 받도록 변경
    private Memo assignId(Memo memo) {
        return memo.copyWithId(UUID.randomUUID().toString());
    }

    private boolean isIdNotEmpty(Memo memo) {
        return !memo.getId().isEmpty();
    }

    private void validateMemoLimit(int memoSize) {
        if (memoSize >= MAX_MEMO_COUNT) {
            throw new MemoLimitExceededException(
                String.format("Cannot add more memos. Maximum limit is %d memos", MAX_MEMO_COUNT)
            );
        }
    }

    public Memo update(Memo memo) {
        validateIdNotEmpty(memo);
        memos.put(memo.getId(), memo);
        return memo;
    }

    public void remove(Memo memo) {
        validateIdNotEmpty(memo);
        memos.remove(memo.getId());
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

    private void validateIdNotEmpty(Memo memo) {
        if (memo.getId().isEmpty()) {
            throw new IllegalArgumentException("Memo ID is required");
        }
    }
}
