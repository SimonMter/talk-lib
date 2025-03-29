package com.retirejs.talk;

import java.util.ArrayList;
import java.util.List;

public class TalkFile {
    public String name;
    public List<byte[]> sounds = new ArrayList<>();
    public List<Float> probabilities = new ArrayList<>();
    public byte[] profilePicture;

    public TalkFile(String name) {
        this.name = name;
    }

    public void addSound(byte[] soundData, float probability) {
        sounds.add(soundData);
        probabilities.add(probability);
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
}
