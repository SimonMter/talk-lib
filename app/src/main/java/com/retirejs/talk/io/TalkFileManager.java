package com.retirejs.talk.io;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.retirejs.talk.model.TalkFile;

import java.io.*;

public class TalkFileManager {

    private final TalkFileWriter writer;
    private final TalkFileReader reader;

    public TalkFileManager() {
        this.writer = new TalkFileWriter();
        this.reader = new TalkFileReader();
    }


    public void save(File file, TalkFile talkFile) throws IOException {

        byte[] data = writer.write(talkFile);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public TalkFile load(File file) throws IOException {

        if (!file.exists()) {
            throw new FileNotFoundException("Talk file not found: " + file.getAbsolutePath());
        }

        byte[] data;

        try (FileInputStream fis = new FileInputStream(file)) {
            data = fis.readAllBytes();
        }

        return reader.read(data);
    }
}
