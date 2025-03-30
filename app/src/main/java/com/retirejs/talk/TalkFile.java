package com.retirejs.talk;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TalkFile {
    public static final int CURRENT_VERSION = 2;
    private int fileVersion;
    private UUID uuid;
    private long createdTimeStamp;
    private long lastModifiedTimeStamp;
    private String name;
    private List<byte[]> sounds = new ArrayList<>();
    private List<Float> probabilities = new ArrayList<>();
    private List<byte[]> profilePicture = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private byte[] checksum;

    public TalkFile(String name) {
        this.fileVersion = CURRENT_VERSION;
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.createdTimeStamp = System.currentTimeMillis();
        this.lastModifiedTimeStamp = System.currentTimeMillis();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(long createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public long getLastModifiedTimeStamp() {
        return lastModifiedTimeStamp;
    }

    public void setLastModifiedTimeStamp(long lastModifiedTimeStamp) {
        this.lastModifiedTimeStamp = lastModifiedTimeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<byte[]> getSounds() {
        return sounds;
    }

    public void addSound(byte[] soundData, float probability) {
        sounds.add(soundData);
        probabilities.add(probability);
    }

    public List<Float> getProbabilities() {
        return probabilities;
    }

    public List<byte[]> getProfilePictures() {
        return profilePicture;
    }

    public void addProfilePicture(byte[] profilePicture) {
        this.profilePicture.add(profilePicture);
    }

    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public byte[] getChecksum() {
        return checksum;
    }

    public void setChecksum(byte[] checksum) {
        this.checksum = checksum;
    }

    public int getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(int fileVersion) {
        this.fileVersion = fileVersion;
    }
}
