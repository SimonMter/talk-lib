
package com.retirejs.talk.model;

public class SoundEntry {
    
    private final byte[] data;
    private final float probability;

    public SoundEntry(byte[] data, float probability){
        this.data = data;
        this.probability = probability;
    }

    public byte[] getData(){
        return data;
    }

    public float getProbability(){
        return probability;
    }
}
