package com.retirejs.talk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TalkFile {
   private UUID uuid;

   //Metadata
    private long createdTimestamp;
    private long modifiedTimestamp;
    private String  name;

    private final List<SoundEntry> sounds = new ArrayList<>();
    private final List<byte[]> profilePictures = new ArrayList<>();
    private final List<String> tags = new ArrayList<>();

    public TalkFile(String name){
        this.uuid = UUID.randomUUID();
        this.name = name;
        
        long now = System.currentTimeMillis();
        this.createdTimestamp = now;
        this.modifiedTimestamp = now;
    }

    public TalkFile(UUID uuid, String name, long created, long modified){
        this.uuid = uuid;
        this.name = name;
        this.createdTimestamp = created;
        this.modifiedTimestamp = modified;
    }


    //Sound Handling

    public void addSound(byte[] data, float probability){
        sounds.add(new SoundEntry(data, probability));
        touch();
    }
    
    public void removeSound(int index){
        sounds.remove(index);
        touch();
    }

    public List<SoundEntry> getSounds(){
        return sounds;
    }

    // Profile Handling

    public void addProfilePicture(byte[] data){
        profilePictures.add(data);
        touch();
    }

    public List<byte[]> getProfilePictures(){
        return profilePictures;
    }

    public void removeProfilePicture(int index){
        profilePictures.remove(index);
        touch();
    }

    // Tags

    public void addTag(String tag){
        if(!tags.contains(tag)){
            tags.add(tag);
            touch();
        }
    }

    public void removetag(String tag){
        tags.remove(tag);
        touch();
    }

    public List<String> getTags(){
        return tags;
    }

    

    //Helpers

    private void touch(){
        this.modifiedTimestamp = System.currentTimeMillis();
    }




    public UUID getUuid(){
        return uuid;
    }

    public String getName(){
        return name;
    }

    public long getCreatedTimestamp(){
        return createdTimestamp;
    }

    public long getModifiedTimestamp(){
        return modifiedTimestamp;
    }


    public void setName(String name){
        this.name = name;
        touch();
    }

}
