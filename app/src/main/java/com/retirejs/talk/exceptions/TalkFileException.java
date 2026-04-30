package com.retirejs.talk.exceptions;

public class TalkFileException extends Exception {

    public TalkFileException(String message) {
        super(message);
    }

    public TalkFileException(String message, Throwable cause) {
        super(message, cause);
    }
}