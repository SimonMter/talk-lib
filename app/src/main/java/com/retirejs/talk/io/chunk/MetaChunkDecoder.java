package com.retirejs.talk.io.chunk;

import com.retirejs.talk.model.TalkFile;
import com.retirejs.talk.util.StringCodec;

import java.io.*;
import java.util.UUID;

public class MetaChunkDecoder implements ChunkDecoder{

    @Override
    public String id(){
        return ChunkType.META;
    }

    @Override
    public void decode(byte[] data, TalkFile target){
        try{
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
            
            long msb = in.readLong();
            long lsb = in.readLong();

            long created = in.readLong();
            long modified = in.readLong();
            String name = StringCodec.readString(in);
            
            target.setName(name);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
