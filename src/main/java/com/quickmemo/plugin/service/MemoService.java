package com.quickmemo.plugin.service;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service(Service.Level.PROJECT)
@State(
    name = "QuickMemoContent",
    storages = {@Storage("quickMemo.xml")}
)
public final class MemoService implements PersistentStateComponent<MemoService.State> {
    private State state = new State();

    public static class State {
        public String content = "";
    }

    public static MemoService getInstance(@NotNull Project project) {
        return project.getService(MemoService.class);
    }

    @Override
    public @Nullable State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.state = state;
    }

    public void saveMemo(String content) {
        state.content = content;
    }

    public String loadMemo() {
        return state.content;
    }
}
