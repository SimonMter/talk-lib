package com.retirejs.talk.io.chunk;

import com.retirejs.talk.model.TalkFile;

public interface ChunkDecoder{
    String id();

    void decode(byte[] data, TalkFile target);
}
