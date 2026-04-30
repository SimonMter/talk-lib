package com.retirejs.talk.exceptions;

public class TalkFileCorruptedException extends RuntimeException {
    public TalkFileCorruptedException(String message) {
        super(message);
    }
}
