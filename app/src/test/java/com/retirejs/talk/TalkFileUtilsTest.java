package com.retirejs.talk;

import org.junit.Test;
import static org.junit.Assert.*;

import com.retirejs.talk.Exceptions.TalkFileException;

import java.io.File;
import java.util.List;
import java.util.Arrays;

public class TalkFileUtilsTest {

    // Test case for saving and loading a TalkFile
    @Test
    public void testSaveAndLoadTalkFile() {
        // Arrange: Set up mock data to save
        String name = "TestTalkFile";
        List<byte[]> sounds = Arrays.asList(new byte[]{1, 2, 3}, new byte[]{4, 5, 6});
        List<Float> probabilities = Arrays.asList(0.7f, 0.3f);
        List<byte[]> profilePictures = Arrays.asList(new byte[]{10, 20, 30});
        List<String> tags = Arrays.asList("funny", "test");

        // Act: Save the TalkFile using your library
        TalkFile savedTalkFile = TalkFileUtils.saveTalkFile(null, name, sounds, probabilities, profilePictures, tags);

        // Save the file to a specific folder (mock or use a test folder path)
        File testFolder = new File("path/to/test/directory");
        String fileName = savedTalkFile.getName() + ".talk";

        // Save using TalkFileManager (you'll need to adjust TalkFileManager to not rely on Android context)
        TalkFileManager talkFileManager = new TalkFileManager(testFolder);
        talkFileManager.saveTalkFile(savedTalkFile);

        // Act: Load the TalkFile back
        TalkFile loadedTalkFile = null;
        try {
            loadedTalkFile = talkFileManager.loadTalkFile(savedTalkFile.getName());
        } catch (TalkFileException e) {
            fail("Failed to load TalkFile: " + e.getMessage());
        }

        // Assert: Check that the loaded TalkFile is the same as the saved one
        assertNotNull(loadedTalkFile);
        assertEquals(savedTalkFile.getName(), loadedTalkFile.getName());
        assertEquals(savedTalkFile.getSounds(), loadedTalkFile.getSounds());
        assertEquals(savedTalkFile.getProbabilities(), loadedTalkFile.getProbabilities());
        assertEquals(savedTalkFile.getProfilePictures(), loadedTalkFile.getProfilePictures());
        assertEquals(savedTalkFile.getTags(), loadedTalkFile.getTags());
    }

    // Test case for MP3 conversion (dummy file)
    @Test
    public void testConvertMp3ToBytes() {
        File mp3File = new File("path/to/dummy.mp3");

        try {
            byte[] mp3Bytes = TalkFileUtils.convertMp3ToBytes(mp3File);
            assertNotNull(mp3Bytes);
            assertTrue(mp3Bytes.length > 0); // Check that conversion returned bytes
        } catch (Exception e) {
            fail("MP3 Conversion failed: " + e.getMessage());
        }
    }
}
