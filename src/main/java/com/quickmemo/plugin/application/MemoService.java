package com.quickmemo.plugin.application;

import com.quickmemo.plugin.memo.Memo;

import java.time.LocalDateTime;
import java.util.List;

public final class MemoService {
    private final MemoRepository memoRepository;
    private static final String WELCOME_MEMO = """
    default-keymap:
    - macOS: Option + M
    - windows/linux: Ctrl + Q
    """;
    private static final String EMPTY_MEMO = "";

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
        initializeDefaultMemoIfEmpty();
    }

    // TODO: id를 할당하는 책임은 누가?
    public void createNewMemo(String content) {
        String createdAt = LocalDateTime.now().toString();
        Memo memo = Memo.from(content, createdAt);
        memoRepository.save(memo);
    }

    public String createEmptyMemo() {
        String createdAt = LocalDateTime.now().toString();

        Memo memo = Memo.from(EMPTY_MEMO, createdAt);
        Memo saved = memoRepository.save(memo);
        return saved.getId();
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
            createNewMemo(WELCOME_MEMO);
        }
    }
}
