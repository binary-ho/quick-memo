package com.quickmemo.plugin.infrastructure;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.quickmemo.plugin.memo.Memo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Service(Service.Level.PROJECT)
@State(
        name = "QuickMemoContent",
        storages = {@Storage("quickMemo.xml")}
)
public final class MemoState implements PersistentStateComponent<Memos> {
    private Memos state = new Memos();

    public List<Memo> getAll() {
        return state.memos.values().stream()
                .map(data -> new Memo(data.id, data.content, data.createdAt))
                .toList();
    }

    public void add(Memo memo) {
        if (!state.memos.containsKey(memo.id())) {
            state.memos.put(memo.id(), new MemoLocalState(memo.id(), memo.content(), memo.createdAt()));
        }
    }

    public void update(Memo memo) {
        MemoLocalState data = state.memos.get(memo.id());
        if (data != null) {
            data.content = memo.content();
        }
    }

    public void remove(String id) {
        state.memos.remove(id);
    }

    public boolean isExist(String id) {
        return state.memos.containsKey(id);
    }

    public boolean isEmpty() {
        return state.memos.isEmpty();
    }

    @Override
    public @Nullable Memos getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull Memos state) {
        this.state = state;
    }
}
