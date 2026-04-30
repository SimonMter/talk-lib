package com.retirejs.talk.exceptions;

public class TalkFileCorruptedException extends TalkFileException {

    public TalkFileCorruptedException(String reason) {
        super("Talk file corrupted: " + reason);
    }
}