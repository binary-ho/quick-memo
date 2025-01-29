package com.quickmemo.plugin.application;

import com.quickmemo.plugin.memo.Memo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class MemoService {
    private final MemoRepository memoRepository;
    private static final String WELCOME_MEMO = """
    default-keymap
    - macOS: Option + M
    - windows/linux: Ctrl + Q
    """;

    private static final String EMPTY_MEMO = "";

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
        initializeDefaultMemoIfEmpty();
    }

    // TODO: id를 할당하는 책임은 누가?
    public void createMemo(String content) {
        String id = UUID.randomUUID().toString();
        String createdAt = LocalDateTime.now().toString();

        Memo memo = new Memo(id, content, createdAt);
        memoRepository.save(memo);
    }

    public String createEmptyMemo() {
        String id = UUID.randomUUID().toString();
        String createdAt = LocalDateTime.now().toString();

        Memo memo = new Memo(id, EMPTY_MEMO, createdAt);
        memoRepository.save(memo);
        return id;
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
            createMemo(WELCOME_MEMO);
        }
    }
}
