package com.quickmemo.plugin.application;

import com.quickmemo.plugin.memo.Memo;

import java.util.List;

public interface MemoRepository {
    void save(Memo memo);
    void remove(Memo memo);
    List<Memo> findAll();
    boolean isEmpty();
}
