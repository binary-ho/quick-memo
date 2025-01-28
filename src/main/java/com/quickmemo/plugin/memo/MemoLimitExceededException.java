package com.quickmemo.plugin.memo;

public class MemoLimitExceededException extends RuntimeException {
    public MemoLimitExceededException(String message) {
        super(message);
    }
}
