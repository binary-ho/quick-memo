package com.quickmemo.plugin.service;

import com.intellij.openapi.components.*;
import com.quickmemo.plugin.memo.Memo;
import com.quickmemo.plugin.memo.Memos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Service(Service.Level.PROJECT)
@State(
    name = "QuickMemoContent",
    storages = {@Storage("quickMemo.xml")}
)
public final class MemoService implements PersistentStateComponent<Memos> {
    private static final String WELCOME_MEMO = """
    Free your Brain, Just Memo It
    생각을 뇌에서 놓아주세요. 그냥 메모합시당
    
    default-keymap
    - macOS: Command + Option + M
    - windows/linux: Ctrl + M
    """;

    private Memos memos;

    public MemoService() {
        this.memos = new Memos();
    }

    // TODO
    public String createMemo(String content) {
        return "";
    }

    // TODO
    public void updateMemo(String id, String content) {
    }

    public void deleteMemo(String id) {
        memos.remove(id);
    }

    public List<Memo> getAllMemos() {
        return memos.getAllMemos();
    }

    public void initializeDefaultMemoIfEmpty() {
        if (memos.isEmpty()) {
            createMemo(WELCOME_MEMO);
        }
    }

    @Override
    public @Nullable Memos getState() {
        return memos;
    }

    @Override
    public void loadState(@NotNull Memos memos) {
        this.memos = memos;
    }
}
