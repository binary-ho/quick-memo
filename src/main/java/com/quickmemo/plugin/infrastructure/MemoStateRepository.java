package com.quickmemo.plugin.infrastructure;

import com.quickmemo.plugin.application.MemoRepository;
import com.quickmemo.plugin.memo.Memo;

import java.util.Collections;
import java.util.List;


public class MemoStateRepository implements MemoRepository {
    private final MemoState memoState;

    public MemoStateRepository(MemoState memoState) {
        this.memoState = memoState;
    }

    @Override
    public Memo save(Memo memo) {
        if (memoState.isExist(memo.getId())) {
            return memoState.update(memo);
        }
        return memoState.add(memo);
    }

    @Override
    public void remove(Memo memo) {
        memoState.remove(memo);
    }

    @Override
    public List<Memo> findAll() {
        List<Memo> memos = memoState.getAll();
        if (memos == null) {
            return Collections.emptyList();
        }
        return memos;
    }

    @Override
    public boolean isEmpty() {
        return memoState.isEmpty();
    }
}
