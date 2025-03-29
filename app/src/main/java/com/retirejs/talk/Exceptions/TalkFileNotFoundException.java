package com.retirejs.talk.Exceptions;

public class TalkFileNotFoundException extends TalkFileException {
    public TalkFileNotFoundException(String filename) {
        super("TalkFile not found: " + filename);
    }
}
