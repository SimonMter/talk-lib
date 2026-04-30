package com.retirejs.talk.io.chunk;

import com.retirejs.talk.model.TalkFile;
import com.retirejs.talk.util.StringCodec;

import java.io.*;
import java.util.List;

public class TagChunk implements Chunk {

    private final TalkFile file;

    public TagChunk(TalkFile file) {
        this.file = file;
    }

    @Override
    public String id() {
        return ChunkType.TAGS; // "TAG "
    }

    @Override
    public byte[] encode() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);

            List<String> tags = file.getTags();

            out.writeInt(tags.size());

            for (String tag : tags) {
                StringCodec.writeString(out, tag);
            }

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
