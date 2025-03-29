package com.retirejs.talk.Exceptions;

public class TalkFileCorruptedException extends TalkFileException {
    public TalkFileCorruptedException(String filename) {
        super("TalkFile corrupted: " + filename);
    }
}
