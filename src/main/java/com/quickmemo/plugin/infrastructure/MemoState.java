package com.quickmemo.plugin.infrastructure;

import com.intellij.openapi.components.*;
import com.quickmemo.plugin.memo.Memo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Service(Service.Level.PROJECT)
@State(
        name = "QuickMemoContent",
        storages = {@Storage("quickMemo.xml")}
)
public final class MemoState implements PersistentStateComponent<MemoState.State> {
    private State state = new State();

    public static class State {
        public List<MemoLocalState> memos = new ArrayList<>();
    }

    public static class MemoLocalState {
        public String id;
        public String content;
        public String createdAt;

        public MemoLocalState() {
        }

        public MemoLocalState(String id, String content, String createdAt) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
        }
    }

    public List<Memo> getAll() {
        return state.memos.stream()
                .map(data -> new Memo(data.id, data.content, data.createdAt))
                .toList();
    }

    public void add(Memo memo) {
        for (int i = 0; i < state.memos.size(); i++) {
            if (state.memos.get(i).id.equals(memo.id())) {
                return;
            }
        }
        state.memos.add(new MemoLocalState(memo.id(), memo.content(), memo.createdAt()));
    }

    public void update(Memo memo) {
        for (MemoLocalState data : state.memos) {
            if (data.id.equals(memo.id())) {
                data.content = memo.content();
                break;
            }
        }
    }

    public void remove(String id) {
        state.memos.removeIf(data -> data.id.equals(id));
    }

    public boolean isExist(String id) {
        return state.memos.stream().anyMatch(data -> data.id.equals(id));
    }

    public boolean isEmpty() {
        return state.memos.isEmpty();
    }

    @Override
    public @Nullable State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.state = state;
    }
}
