package com.retirejs.talk;

import com.retirejs.talk.io.TalkFileManager;
import com.retirejs.talk.model.TalkFile;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

public class TalkFileTest {

    // -----------------------------
    // helper
    // -----------------------------
    private TalkFileManager manager() {
        return new TalkFileManager();
    }

    private File tempFile() throws Exception {
        return File.createTempFile("test", ".talk");
    }

    // -----------------------------
    // 1. BASIC SAVE / LOAD
    // -----------------------------
    @Test
    public void testSaveAndLoadBasic() throws Exception {

        TalkFile tf = new TalkFile("example");
        tf.addSound(new byte[]{1, 2, 3}, 0.9f);
        tf.addTag("robot");

        File file = tempFile();

        manager().save(file, tf);
        TalkFile loaded = manager().load(file);

        assertEquals("example", loaded.getName());
        assertEquals(1, loaded.getSounds().size());
        assertEquals(1, loaded.getTags().size());
    }

    // -----------------------------
    // 2. MULTIPLE SOUNDS
    // -----------------------------
    @Test
    public void testMultipleSounds() throws Exception {

        TalkFile tf = new TalkFile("multi-sound");

        tf.addSound(new byte[]{1, 2, 3}, 0.5f);
        tf.addSound(new byte[]{4, 5, 6, 7}, 0.8f);
        tf.addSound(new byte[]{9}, 1.0f);

        File file = tempFile();

        manager().save(file, tf);
        TalkFile loaded = manager().load(file);

        assertEquals(3, loaded.getSounds().size());
    }

    // -----------------------------
    // 3. MULTIPLE TAGS
    // -----------------------------
    @Test
    public void testMultipleTags() throws Exception {

        TalkFile tf = new TalkFile("tags");

        tf.addTag("robot");
        tf.addTag("calm");
        tf.addTag("ai");

        File file = tempFile();

        manager().save(file, tf);
        TalkFile loaded = manager().load(file);

        assertEquals(3, loaded.getTags().size());
    }

    // -----------------------------
    // 4. DATA INTEGRITY (IMPORTANT)
    // -----------------------------
    @Test
    public void testSoundDataIntegrity() throws Exception {

        byte[] original = new byte[]{10, 20, 30, 40};

        TalkFile tf = new TalkFile("integrity");
        tf.addSound(original, 0.77f);

        File file = tempFile();

        manager().save(file, tf);
        TalkFile loaded = manager().load(file);

        assertArrayEquals(
                original,
                loaded.getSounds().get(0).getData()
        );
    }

    // -----------------------------
    // 5. PROBABILITY CHECK
    // -----------------------------
    @Test
    public void testProbability() throws Exception {

        TalkFile tf = new TalkFile("prob");

        tf.addSound(new byte[]{1, 2, 3}, 0.42f);

        File file = tempFile();

        manager().save(file, tf);
        TalkFile loaded = manager().load(file);

        assertEquals(
                0.42f,
                loaded.getSounds().get(0).getProbability(),
                0.0001f
        );
    }

    // -----------------------------
    // 6. EMPTY FILE EDGE CASE
    // -----------------------------
    @Test
    public void testEmptyFile() throws Exception {

        TalkFile tf = new TalkFile("empty");

        File file = tempFile();

        manager().save(file, tf);
        TalkFile loaded = manager().load(file);

        assertEquals("empty", loaded.getName());
        assertEquals(0, loaded.getSounds().size());
        assertEquals(0, loaded.getTags().size());
    }
    @Test
    public void stressTestLargeFile() throws Exception {

        TalkFile tf = TalkTestUtils.randomTalkFile(50000, 20000);

        File file = File.createTempFile("stress", ".talk");

        TalkFileManager manager = new TalkFileManager();

        manager.save(file, tf);
        TalkFile loaded = manager.load(file);

        assertEquals(tf.getSounds().size(), loaded.getSounds().size());
        assertEquals(tf.getTags().size(), loaded.getTags().size());
    }
    @Test
    public void stressTestHugeSound() throws Exception {

        TalkFile tf = new TalkFile("huge");

        byte[] big = new byte[75_000_000]; // 5MB
        new Random().nextBytes(big);

        tf.addSound(big, 0.99f);

        File file = File.createTempFile("huge", ".talk");

        TalkFileManager manager = new TalkFileManager();

        manager.save(file, tf);
        TalkFile loaded = manager.load(file);

        assertArrayEquals(
                big,
                loaded.getSounds().get(0).getData()
        );
    }
    @Test(expected = Exception.class)
    public void testCorruptedFileDetection() throws Exception {

        TalkFile tf = TalkTestUtils.randomTalkFile(10, 10);

        File file = File.createTempFile("corrupt", ".talk");

        TalkFileManager manager = new TalkFileManager();

        manager.save(file, tf);

        byte[] data = java.nio.file.Files.readAllBytes(file.toPath());

        byte[] corrupted = TalkTestUtils.corrupt(data);

        java.nio.file.Files.write(file.toPath(), corrupted);

        manager.load(file); // MUST FAIL
    }
    @Test(expected = IOException.class)
    public void testUnknownChunkIgnored() throws Exception {

        TalkFile tf = new TalkFile("forward");

        tf.addTag("test");

        File file = File.createTempFile("forward", ".talk");

        TalkFileManager manager = new TalkFileManager();

        manager.save(file, tf);

        byte[] data = java.nio.file.Files.readAllBytes(file.toPath());

        // Inject fake chunk at end
        byte[] fakeChunk = "FAKE".getBytes();

        byte[] modified = new byte[data.length + fakeChunk.length];

        System.arraycopy(data, 0, modified, 0, data.length);
        System.arraycopy(fakeChunk, 0, modified, data.length, fakeChunk.length);

        java.nio.file.Files.write(file.toPath(), modified);

        TalkFile loaded = manager.load(file);

        // Should still work normally
        assertEquals("forward", loaded.getName());
    }
    @Test
    public void fuzzTestRandomFiles() throws Exception {

        TalkFileManager manager = new TalkFileManager();

        for (int i = 0; i < 50; i++) {

            TalkFile tf = TalkTestUtils.randomTalkFile(
                    new Random().nextInt(50),
                    new Random().nextInt(20)
            );

            File file = File.createTempFile("fuzz", ".talk");

            manager.save(file, tf);
            TalkFile loaded = manager.load(file);

            assertEquals(tf.getSounds().size(), loaded.getSounds().size());
            assertEquals(tf.getTags().size(), loaded.getTags().size());
        }
    }
}