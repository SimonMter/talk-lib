package com.retirejs.talk.io.chunk;

import com.retirejs.talk.model.TalkFile;

import java.io.*;

public class SoundChunkDecoder implements ChunkDecoder {

    @Override
    public String id() {
        return ChunkType.SOUND;
    }

    @Override
    public void decode(byte[] data, TalkFile target) {
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

            int count = in.readInt();

            for (int i = 0; i < count; i++) {
                int len = in.readInt();
                float prob = in.readFloat();

                byte[] soundData = new byte[len];
                in.readFully(soundData);

                target.addSound(soundData, prob);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
