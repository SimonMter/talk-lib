package com.retirejs.talk;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import com.retirejs.talk.Exceptions.TalkFileException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

public class TalkFileUtils {
    /**
     * Saves a TalkFile in the specified directory.
     * @param directory - Directory to save the TalkFile.
     * @param name - TalkFile name.
     * @param soundDataList - List of sound byte arrays.
     * @param probabilities - Probabilities corresponding to each sound.
     * @param profilePictures - List of profile picture byte arrays.
     * @param tags - List of string tags.
     * @return The saved TalkFile object.
     */
    public static TalkFile saveTalkFile(File directory, String name, List<byte[]> soundDataList, List<Float> probabilities, List<byte[]> profilePictures, List<String> tags) {
        TalkFileManager talkFileManager = new TalkFileManager(directory);
        TalkFile talkFile = new TalkFile(name);

        for (int i = 0; i < soundDataList.size(); i++) {
            byte[] soundData = soundDataList.get(i);
            float probability = probabilities.get(i);
            talkFile.addSound(soundData, probability);
        }

        for (byte[] profilePicture : profilePictures) {
            talkFile.addProfilePicture(profilePicture);
        }

        for (String tag : tags) {
            talkFile.addTag(tag);
        }

        talkFile.setUuid(UUID.randomUUID());
        talkFile.setLastModifiedTimeStamp(System.currentTimeMillis());
        talkFileManager.saveTalkFile(talkFile); // Save the TalkFile to disk
        return talkFile;
    }

    /**
     * Loads a TalkFile from the specified directory.
     * @param directory - Directory to load the file from.
     * @param fileName - Name of the TalkFile to load.
     * @return The loaded TalkFile object.
     * @throws TalkFileException if the file is not found or corrupted.
     */
    public static TalkFile loadTalkFile(File directory, String fileName) throws TalkFileException {
        TalkFileManager talkFileManager = new TalkFileManager(directory);
        return talkFileManager.loadTalkFile(fileName);
    }

    public static boolean deleteTalkFile(File directory, String fileName) {
        File file = new File(directory, fileName + ".talk");
        return file.exists() && file.delete();
    }

    /**
     * Converts an MP3 file into a byte array.
     * @param directory - Directory containing the MP3 file.
     * @param fileName - Name of the MP3 file.
     * @return Byte array of the MP3 data.
     * @throws IOException if file reading fails.
     */
    public static byte[] convertMp3ToBytes(File directory, String fileName) throws IOException {
        File mp3File = new File(directory, fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Files.readAllBytes(mp3File.toPath());
        }
        return new byte[0];
    }

    /**
     * Converts an MP3 file into a byte array.
     * @param inputStream - InputStream of the MP3 file.
     * @return Byte array of the MP3 data.
     * @throws IOException if file reading fails.
     */
    /**
     * Converts an MP3 file into a byte array.
     * @param inputStream - InputStream of the MP3 file.
     * @return Byte array of the MP3 data.
     * @throws IOException if file reading fails.
     */
    public static byte[] convertMp3ToBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
        } finally {
            try {
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public static byte[] convertMp3ToBytesFromRaw(Context context, int resId) throws IOException {
        Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(resId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}