package com.quickmemo.plugin.memo;

import static com.quickmemo.plugin.memo.MemoConstants.MAX_CONTENT_SIZE_BYTES;

public record Memo(String id, String content, String createdAt) {
    public Memo {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }

        if (content == null) {
            throw new IllegalArgumentException("content is required");
        }

        if (createdAt == null || createdAt.isBlank()) {
            throw new IllegalArgumentException("createdAt is required");
        }

        validateContentSize(content);
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
}
