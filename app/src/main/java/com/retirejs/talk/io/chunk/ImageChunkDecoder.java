package com.retirejs.talk.io.chunk;

import com.retirejs.talk.model.TalkFile;

import java.io.*;

public class ImageChunkDecoder implements ChunkDecoder {

    @Override
    public String id() {
        return ChunkType.IMAGE;
    }

    @Override
    public void decode(byte[] data, TalkFile target) {
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

            int count = in.readInt();

            for (int i = 0; i < count; i++) {
                int len = in.readInt();

                byte[] img = new byte[len];
                in.readFully(img);

                target.addProfilePicture(img);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
