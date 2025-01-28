package com.quickmemo.plugin.infrastructure;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.memo.Memos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Service(Service.Level.PROJECT)
@State(
        name = "QuickMemoContent",
        storages = {@Storage("quickMemoContent.xml")}
)
public final class MemoState implements PersistentStateComponent<Memos> {
    private final Memos memos;

    public MemoState() {
        memos = new Memos();
    }

    public List<Memo> getAll() {
        return memos.getAll();
    }

    public void add(Memo memo) {
        memos.add(memo);
    }

    public void update(Memo memo) {
        memos.update(memo);
    }

    public void remove(String id) {
        memos.remove(id);
    }

    public boolean isEmpty() {
        return memos.isEmpty();
    }

    public boolean isExist(String id) {
        return memos.isExist(id);
    }

    @Override
    public @Nullable Memos getState() {
        // TODO: 방어적 복사?
        return this.memos;
    }

    @Override
    public void loadState(@NotNull Memos loaded) {
        this.memos.addAll(loaded.getAll());
    }
}
