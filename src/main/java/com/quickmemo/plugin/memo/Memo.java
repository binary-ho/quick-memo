package com.quickmemo.plugin.memo;

import java.time.LocalDateTime;

import static com.quickmemo.plugin.memo.MemoConstants.MAX_CONTENT_SIZE_BYTES;

public class Memo {
    private final String id;
    private final String content;
    private final LocalDateTime createdAt;

    private static final String EMPTY_ID = "";

    public Memo(String id, String content, LocalDateTime createdAt) {
        validateCreatedAt(createdAt);
        validateContentSize(content);
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Memo createFrom(String content, LocalDateTime createdAt) {
        return new Memo(EMPTY_ID, content, createdAt);
    }

    public Memo copyWithContent(String content) {
        return new Memo(this.id, content, this.createdAt);
    }

    public Memo copyWithId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        return new Memo(id, this.content, this.createdAt);
    }

    private void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null || createdAt.toString().isBlank()) {
            throw new IllegalArgumentException("createdAt is required");
        }
    }

    private static void validateContentSize(String content) {
        byte[] contentBytes = content.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (contentBytes.length >= MAX_CONTENT_SIZE_BYTES) {
            throw new IllegalArgumentException(
                String.format("Content size exceeds maximum limit of %d bytes (current: %d bytes)",
                MAX_CONTENT_SIZE_BYTES, contentBytes.length)
            );
        }
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Memo memo = (Memo) obj;
        return this.id.equals(memo.id);
    }
}
