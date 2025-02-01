package com.quickmemo.plugin.infrastructure;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.memo.Memos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Service(Service.Level.PROJECT)
@State(
        name = "QuickMemoContent",
        storages = {@Storage("quickMemo.xml")}
)
public final class MemoState implements PersistentStateComponent<SavedMemos> {
    private final Memos memos = new Memos();

    public List<Memo> getAll() {
        return memos.getAll();
    }

    public Memo add(Memo memo) {
        return memos.add(memo);
    }

    public Memo update(Memo memo) {
        return memos.update(memo);
    }

    public void remove(Memo memo) {
        memos.remove(memo);
    }

    public boolean isExist(String id) {
        if (id == null) {
            return false;
        }
        return memos.isExist(id);
    }

    public boolean isEmpty() {
        return memos.isEmpty();
    }

    @Override
    public @NotNull SavedMemos getState() {
        return SavedMemos.from(convertSavedMemos());
    }

    private @NotNull List<SavedMemos.SavedMemo> convertSavedMemos() {
        return memos.getAll()
                .stream()
                .map(MemoMapper::toSavedMemo)
                .toList();
    }

    @Override
    public void loadState(@NotNull SavedMemos state) {
        state.memos.stream()
            .map(MemoMapper::toMemo)
            .forEach(memos::update);
    }
}
