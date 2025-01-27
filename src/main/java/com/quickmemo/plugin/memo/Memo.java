package com.quickmemo.plugin.memo;

public class Memo {
    private final String id;
    private final String content;
    private final long createdAt;

//    public Memo(String content) {
//        this.id = UUID.randomUUID().toString();
//        this.content = content;
//        this.createdAt = System.currentTimeMillis();
//    }

    public Memo(String id, String content, long createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Memo updateContentAndGetNewMemo(String content) {
        return new Memo(this.id, content, this.createdAt);
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
