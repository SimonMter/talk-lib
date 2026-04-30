package com.retirejs.talk.io;

import com.retirejs.talk.io.chunk.*;
import com.retirejs.talk.model.TalkFile;
import com.retirejs.talk.util.ChecksumUtil;
import com.retirejs.talk.util.Constants;

import java.io.*;
import java.util.Arrays;

public class TalkFileReader {

    private final ChunkDecoderRegistry registry = new ChunkDecoderRegistry();

    public TalkFileReader() {
        registerDefaultDecoders();
    }

    private void registerDefaultDecoders() {
        registry.register(new MetaChunkDecoder());
        registry.register(new SoundChunkDecoder());
        registry.register(new ImageChunkDecoder());
        registry.register(new TagChunkDecoder());
    }

    public TalkFile read(byte[] fileData) throws IOException {

        if (fileData.length < 32) {
            throw new IOException("File too small to contain checksum");
        }

        byte[] data = Arrays.copyOfRange(fileData, 0, fileData.length - 32);
        byte[] expectedHash = Arrays.copyOfRange(fileData, fileData.length - 32, fileData.length);

        byte[] actualHash = ChecksumUtil.sha256(data);

        if (!Arrays.equals(expectedHash, actualHash)) {
            throw new IOException("Checksum mismatch (file corrupted)");
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

        byte[] magicBytes = new byte[4];
        in.readFully(magicBytes);
        String magic = new String(magicBytes);

        if (!magic.equals(Constants.MAGIC)) {
            throw new IOException("Invalid file format (magic mismatch)");
        }

        int version = in.readInt();

        if (version != Constants.VERSION) {
            throw new IOException("Unsupported version: " + version);
        }

        TalkFile talkFile = new TalkFile("temp");

        while (in.available() > 0) {

            byte[] idBytes = new byte[4];
            in.readFully(idBytes);
            String chunkId = new String(idBytes);

            int size = in.readInt();

            if (size < 0 || size > in.available()) {
                throw new IOException("Invalid chunk size: " + size);
            }

            byte[] chunkData = new byte[size];
            in.readFully(chunkData);

            ChunkDecoder decoder = registry.get(chunkId);

            if (decoder != null) {
                decoder.decode(chunkData, talkFile);
            } else {
                //unknown chunk
            }
        }

        return talkFile;
    }
}