package com.quickmemo.plugin.infrastructure;

public class MemoLocalState {
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
