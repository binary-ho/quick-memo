package com.quickmemo.plugin.memo;

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
    }
}
