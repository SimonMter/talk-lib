package com.retirejs.talk.exceptions;

public class TalkFileFormatException extends TalkFileException {

    public TalkFileFormatException(String message) {
        super("Invalid Talk file format: " + message);
    }
}