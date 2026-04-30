package com.retirejs.talk.io;

import com.retirejs.talk.io.chunk.*;
import com.retirejs.talk.model.TalkFile;
import com.retirejs.talk.util.ChecksumUtil;
import com.retirejs.talk.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TalkFileWriter{
    public byte[] write(TalkFile file){
        try{
            ByteArrayOutputStream rawOut = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(rawOut);

            out.writeBytes(Constants.MAGIC);
            out.writeInt(Constants.VERSION);

            List<Chunk> chunks = buildChunks(file);

            for(Chunk chunk : chunks){
                byte[] data = chunk.encode();

                byte[] idBytes = chunk.id().getBytes(java.nio.charset.StandardCharsets.US_ASCII);

                if (idBytes.length != 4) {
                    throw new IllegalStateException("Chunk ID must be exactly 4 bytes: " + chunk.id());
                }

                out.write(idBytes);
                out.writeInt(data.length);
                out.write(data);
            }

            out.flush();

            byte[] raw = rawOut.toByteArray();
            byte[] hash = ChecksumUtil.sha256(raw);
            
            ByteArrayOutputStream finalOut = new ByteArrayOutputStream();
            finalOut.write(raw);
            finalOut.write(hash);

            return finalOut.toByteArray();

        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    private List<Chunk> buildChunks(TalkFile file){
        List<Chunk> chunks = new ArrayList<>();

        chunks.add(new MetaChunk(file));
        chunks.add(new SoundChunk(file));
        chunks.add(new ImageChunk(file));
        chunks.add(new TagChunk(file));

        return chunks;
    }

}
