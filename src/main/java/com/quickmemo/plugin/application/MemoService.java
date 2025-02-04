package com.quickmemo.plugin.application;

import com.quickmemo.plugin.memo.Memo;

import java.time.LocalDateTime;
import java.util.List;

public final class MemoService {
    private final MemoRepository memoRepository;
    private static final String WELCOME_MEMO = """
    Welcome!
    
    default-keymap:
    - macOS: Option + M
    - windows/linux: Ctrl + Q
    """;
    private static final String EMPTY_MEMO = "";

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
        initializeDefaultMemoIfEmpty();
    }

    public void createNewMemo(String content, LocalDateTime createdAt) {
        Memo memo = Memo.createFrom(content, createdAt);
        memoRepository.save(memo);
    }

    public Memo createEmptyMemo(LocalDateTime createdAt) {
        Memo memo = Memo.createFrom(EMPTY_MEMO, createdAt);
        return memoRepository.save(memo);
    }

    public void updateMemo(Memo memo) {
        memoRepository.save(memo);
    }

    public void deleteMemo(Memo memo) {
        memoRepository.remove(memo);
    }

    public List<Memo> getAllMemos() {
        return memoRepository.findAll();
    }

    private void initializeDefaultMemoIfEmpty() {
        if (memoRepository.isEmpty()) {
            LocalDateTime createdAt = LocalDateTime.now();
            createNewMemo(WELCOME_MEMO, createdAt);
        }
    }
}
