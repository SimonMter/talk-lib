package com.retirejs.talk.io.chunk;

import com.retirejs.talk.model.SoundEntry;
import com.retirejs.talk.model.TalkFile;

import java.io.*;
import java.util.List;

public class SoundChunk implements Chunk{
    
    private final TalkFile file;

    public SoundChunk(TalkFile file){
        this.file = file;
    }

    @Override
    public String id(){
        return ChunkType.SOUND;
    }

    @Override
    public byte[] encode(){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);

            List<SoundEntry> sounds =  file.getSounds();

            out.writeInt(sounds.size());

            for(SoundEntry sound : sounds){
                byte[] data = sound.getData();

                out.writeInt(data.length);
                out.writeFloat(sound.getProbability());
                out.write(data);
            }
            return baos.toByteArray();

        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
