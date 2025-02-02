package com.quickmemo.plugin.application;

import com.quickmemo.plugin.memo.Memo;

import java.util.List;

public interface MemoRepository {
    Memo save(Memo memo);
    void remove(Memo memo);
    List<Memo> findAll();
    boolean isEmpty();
}
