package com.retirejs.talk.io.chunk;

import com.retirejs.talk.model.TalkFile;

import java.io.*;
import java.util.List;

public class ImageChunk implements Chunk {

    private final TalkFile file;

    public ImageChunk(TalkFile file) {
        this.file = file;
    }

    @Override
    public String id() {
        return ChunkType.IMAGE;
    }

    @Override
    public byte[] encode() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);

            List<byte[]> images = file.getProfilePictures();

            out.writeInt(images.size());

            for (byte[] img : images) {
                out.writeInt(img.length);
                out.write(img);
            }

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
