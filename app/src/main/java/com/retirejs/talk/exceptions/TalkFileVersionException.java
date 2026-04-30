package com.retirejs.talk.exceptions;

public class TalkFileVersionException extends TalkFileException {

    public TalkFileVersionException(int version) {
        super("Unsupported Talk file version: " + version);
    }
}