package com.retirejs.talk.exceptions;

import java.io.File;

public class TalkFileNotFoundException extends TalkFileException {

    public TalkFileNotFoundException(File file) {
        super("Talk file not found: " + file.getAbsolutePath());
    }
}