package com.retirejs.talk.io.chunk;

import com.retirejs.talk.model.TalkFile;
import com.retirejs.talk.util.StringCodec;

import java.io.*;

public class MetaChunk implements Chunk{
    private final TalkFile file;

    public MetaChunk(TalkFile file){
        this.file = file;
    }

    @Override
    public String id(){
        return ChunkType.META;
    }

    @Override
    public byte[] encode(){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new  DataOutputStream(baos);

            out.writeLong(file.getUuid().getMostSignificantBits());
            out.writeLong(file.getUuid().getLeastSignificantBits());

            out.writeLong(file.getCreatedTimestamp());
            out.writeLong(file.getModifiedTimestamp());

            StringCodec.writeString(out, file.getName());
            return baos.toByteArray();
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
