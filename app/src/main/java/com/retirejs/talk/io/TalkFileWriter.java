package com.retirejs.talk.io;

import com.retirejs.talk.io.chunk.*;
import com.retirejs.talk.model.TalkFile;
import com.retirejs.talk.util.ChecksumUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TalkFileWriter{
    public byte[] write(TalkFile file){
        try{
            ByteArrayOutputStream rawOut = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(rawOut);

            out.writeBytes("TALK");
            out.writeInt(3);

            List<Chunk> chunks = buildChunks(file);

            for(Chunk c : chunks){
                byte[] data = c.encode();

                out.writeBytes(c.id());
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
