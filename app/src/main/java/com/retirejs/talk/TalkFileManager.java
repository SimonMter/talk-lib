package com.retirejs.talk;

import com.retirejs.talk.Exceptions.TalkFileCorruptedException;
import com.retirejs.talk.Exceptions.TalkFileException;
import com.retirejs.talk.Exceptions.TalkFileNotFoundException;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TalkFileManager {
    private final File storageDir;

    public TalkFileManager(File rootDir) {
        this.storageDir = rootDir;
        if (!storageDir.exists()) storageDir.mkdirs();
    }
    public void saveTalkFile(TalkFile talkFile) {
        File file = new File(storageDir, talkFile.getName() + ".talk");

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            //VERSION
            dos.writeInt(talkFile.getFileVersion());

            //UUID
            byte[] uuidBytes = toBytes(talkFile.getUuid());
            dos.write(uuidBytes);

            //TIMESTAMPS
            dos.writeLong(talkFile.getCreatedTimeStamp());
            dos.writeLong(talkFile.getLastModifiedTimeStamp());

            //NAME
            dos.writeUTF(talkFile.getName());

            //SOUNDS
            dos.writeInt(talkFile.getSounds().size());
            for (int i = 0; i < talkFile.getSounds().size(); i++) {
                byte[] soundData = talkFile.getSounds().get(i);
                dos.writeInt(soundData.length);
                dos.write(soundData);
                dos.writeFloat(talkFile.getProbabilities().get(i));
            }

            //PROFILE PICTURES (multiple support in Version 2)
            dos.writeInt(talkFile.getProfilePictures().size());
            for(byte[] picture : talkFile.getProfilePictures()){
                dos.writeInt(picture.length);
                dos.write(picture);
            }

            //TAGS
            dos.writeInt(talkFile.getTags().size());
            for(String tag : talkFile.getTags()){
                dos.writeUTF(tag);
            }

            //CHECKSUM (SHA-256 hash)
            byte[] checksum = talkFile.getChecksum();
            if(checksum == null){
                checksum = calculateChecksum(file);
                talkFile.setChecksum(checksum);
            }
            dos.write(checksum);

            dos.flush();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public TalkFile loadTalkFile(String fileName) throws TalkFileException {
        File file = new File(storageDir, fileName + ".talk");
        if (!file.exists()) throw new TalkFileNotFoundException(fileName);

        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            //READ VERSION
            int fileVersion = dis.readInt();
            if (fileVersion > TalkFile.CURRENT_VERSION) {
                throw new TalkFileException("TalkFile version " + fileVersion + " is not supported.");
            }
            //READ UUID
            UUID uuid = null;
            if(fileVersion >= 2){
                byte[] uuidBytes = new byte[16];
                dis.readFully(uuidBytes);
                uuid = toUUID(uuidBytes);
            }

            long createdTimeStamp = 0;
            long lastModifiedTimeStamp = 0;
            if(fileVersion >= 2){
                createdTimeStamp = dis.readLong();
                lastModifiedTimeStamp = dis.readLong();
            }

            //NAME
            String loadedName = dis.readUTF();
            TalkFile talkFile = new TalkFile(loadedName);
            talkFile.setFileVersion(fileVersion);

            //SET UUID AND TIMESTAMPS IF VERSION 2
            if(fileVersion >= 2){
                talkFile.setUuid(uuid);
                talkFile.setCreatedTimeStamp(createdTimeStamp);
                talkFile.setLastModifiedTimeStamp(lastModifiedTimeStamp);
            }

            //READ SOUNDS
            int soundCount = dis.readInt();
            for (int i = 0; i < soundCount; i++) {
                int soundLength = dis.readInt();
                byte[] sound = new byte[soundLength];
                dis.readFully(sound);
                float probability = dis.readFloat();
                talkFile.addSound(sound, probability);
            }

            int pictureCount = dis.readInt();
            for (int i = 0; i < pictureCount; i++) {
                int pictureLength = dis.readInt();
                byte[] picture = new byte[pictureLength];
                dis.readFully(picture);
                talkFile.addProfilePicture(picture);
            }

            //READ TAGS
            if(fileVersion >= 2){
                int tagCount = dis.readInt();
                for (int i = 0; i < tagCount; i++) {
                    String tag = dis.readUTF();
                    talkFile.addTag(tag);
                }
            }

            //READ CHECKSUM
            byte[] checksum = null;
            if(fileVersion >= 2){
                checksum = new byte[32];
                dis.readFully(checksum);
                talkFile.setChecksum(checksum);
            }
            // VERIFY CHECKSUM
            //TODO: REMOVE FALSE AND FIX CHECKSUM MANAGEMENT
            if (fileVersion >= 2 && false) {
                byte[] computedChecksum = computeChecksum(talkFile);
                if (!Arrays.equals(computedChecksum, checksum)) {
                    throw new TalkFileCorruptedException(fileName + " Cause: Checksum mismatch.");
                }
            }


            return talkFile;
        } catch (IOException e) {
            throw new TalkFileCorruptedException(fileName);
        }
    }


    private byte[] toBytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }
    private byte[] calculateChecksum(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesRead);
            }
        }
        return digest.digest();
    }
    private UUID toUUID(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long mostSigBits = buffer.getLong();
        long leastSigBits = buffer.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    private byte[] computeChecksum(TalkFile talkFile) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);

        dos.writeUTF(talkFile.getName());

        byte[] content = byteStream.toByteArray();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(content);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("SHA-256 algorithm not available.", e);
        }
    }
}
