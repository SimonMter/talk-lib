package com.retirejs.talk;

import java.io.*;

public class TalkFileManager {
    private final File storageDir;

    public TalkFileManager(File rootDir) {
        this.storageDir = rootDir;
        if(!storageDir.exists()) storageDir.mkdirs();
    }

    public void saveTalkFile(TalkFile talkFile){
        File file = new File(storageDir, talkFile.name + ".talk");

        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))){

            //Write name
            dos.writeInt(talkFile.name.length());
            dos.writeUTF(talkFile.name);

            //Write sound count
            dos.writeInt(talkFile.sounds.size());
            for(int i = 0; i < talkFile.sounds.size(); i++){
                byte[] soundData = talkFile.sounds.get(i);
                dos.writeInt(soundData.length);
                dos.write(soundData);
                dos.writeFloat(talkFile.probabilities.get(i));
            }

            //Write profile picture
            if(talkFile.profilePicture != null) {
                dos.writeInt(talkFile.profilePicture.length);
                dos.write(talkFile.profilePicture);
            }else{
                dos.writeInt(0);
            }
            dos.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public TalkFile loadTalkFile(String fileName){
        File file = new File(storageDir, fileName + ".talk");
        if (!file.exists()) return null;

        try(DataInputStream dis = new DataInputStream(new FileInputStream(file))){
            int nameLength = dis.readInt();
            String name = dis.readUTF();

            TalkFile talkFile = new TalkFile(name);
            int soundCount = dis.readInt();

            for(int i = 0; i < soundCount; i++){
                int soundLength = dis.readInt();
                byte[] soundData = new byte[soundLength];
                dis.readFully(soundData);
                float probability = dis.readFloat();

                talkFile.addSound(soundData, probability);
            }

            int imageLength = dis.readInt();
            if(imageLength > 0){
                byte[] image = new byte[imageLength];
                dis.readFully(image);
                talkFile.setProfilePicture(image);
            }
            return talkFile;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
