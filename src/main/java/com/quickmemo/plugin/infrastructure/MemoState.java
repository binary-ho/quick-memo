package com.quickmemo.plugin.infrastructure;

import com.intellij.openapi.components.*;
import com.quickmemo.plugin.memo.Memo;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Service(Service.Level.PROJECT)
@State(
        name = "QuickMemoContent",
        storages = {@Storage("quickMemo.xml")}
)

public final class MemoState implements PersistentStateComponent<SavedMemos> {
    private final Memos memos = new Memos();

    public List<Memo> getAll() {
        return state.memos.stream()
                .map(data -> new Memo(data.id, data.content, data.createdAt))
                .toList();
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

    public boolean isExist(String id) {
        return state.memos.stream().anyMatch(data -> data.id.equals(id));
    }

    @Override
    public @NotNull SavedMemos getState() {
        return SavedMemos.from(convertSavedMemos());
    }

    private @NotNull List<SavedMemos.SavedMemo> convertSavedMemos() {
        return memos.getAll()
                .stream()
                .map(memo -> SavedMemos.SavedMemo.of(
                        memo.getId(),
                        memo.getContent(),
                        memo.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public void loadState(@NotNull SavedMemos state) {
        state.memos.stream()
            .map(saved -> new Memo(saved.id, saved.content, saved.createdAt))
            .forEach(memos::update);
    }
}
