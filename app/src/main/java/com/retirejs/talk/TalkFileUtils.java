package com.retirejs.talk;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.retirejs.talk.Exceptions.TalkFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

public class TalkFileUtils {
    /**
     * Saves a TalkFile in the Downloads directory.
     * @param context - Android context for accessing storage.
     * @param name - TalkFile name.
     * @param soundDataList - List of sound byte arrays.
     * @param probabilities - Probabilities corresponding to each sound.
     * @param profilePictures - List of profile picture byte arrays.
     * @param tags - List of string tags.
     * @return The saved TalkFile object.
     */
    public static TalkFile saveTalkFile(Context context, String name, List<byte[]> soundDataList, List<Float> probabilities, List<byte[]> profilePictures, List<String> tags) {
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        TalkFileManager talkFileManager = new TalkFileManager(downloadsFolder);

        TalkFile talkFile = new TalkFile(name);

        for(int i = 0; i < soundDataList.size(); i++){
            byte[] soundData = soundDataList.get(i);
            float probability = probabilities.get(i);
            talkFile.addSound(soundData, probability);
        }

        for(byte[] profilePicture : profilePictures){
            talkFile.addProfilePicture(profilePicture);
        }

        for(String tag : tags){
            talkFile.addTag(tag);
        }

        talkFile.setUuid(UUID.randomUUID());
        talkFile.setLastModifiedTimeStamp(System.currentTimeMillis());
        return talkFile;
    }

    /**
     * Loads a TalkFile from the Downloads directory.
     * @param context - Android context.
     * @param fileName - Name of the TalkFile to load.
     * @return The loaded TalkFile object.
     * @throws TalkFileException if the file is not found or corrupted.
     */
    public static TalkFile loadTalkFile(Context context, String fileName) throws TalkFileException {
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        TalkFileManager talkFileManager = new TalkFileManager(downloadsFolder);

        return talkFileManager.loadTalkFile(fileName);
    }

    public static boolean deleteTalkFile(Context context, String fileName){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".talk");
        return file.exists() && file.delete();
    }

    /**
     * Converts an MP3 file into a byte array.
     * @param mp3File - The MP3 file to convert.
     * @return Byte array of the MP3 data.
     * @throws IOException if file reading fails.
     */
    public static byte[] convertMp3ToBytes(File mp3File) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Files.readAllBytes(mp3File.toPath());
        }
        return new byte[0];
    }
}
