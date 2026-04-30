package com.retirejs.talk.io.chunk;

import java.util.HashMap;
import java.util.Map;

public class ChunkDecoderRegistry{
    
    private final Map<String, ChunkDecoder> decoders = new HashMap<>();

    public void register(ChunkDecoder decoder){
        decoders.put(decoder.id(), decoder);
    }

    public ChunkDecoder get(String id){
        return decoders.get(id);
    }

}
