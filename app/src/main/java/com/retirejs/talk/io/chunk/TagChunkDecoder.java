package com.retirejs.talk.io.chunk;

import com.retirejs.talk.model.TalkFile;
import com.retirejs.talk.util.StringCodec;

import java.io.*;

public class TagChunkDecoder implements ChunkDecoder {

    @Override
    public String id() {
        return ChunkType.TAGS;
    }

    @Override
    public void decode(byte[] data, TalkFile target) {
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

            int count = in.readInt();

            for (int i = 0; i < count; i++) {
                String tag = StringCodec.readString(in);
                target.addTag(tag);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
