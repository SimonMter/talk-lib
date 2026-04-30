package com.retirejs.talk;

import com.retirejs.talk.model.TalkFile;

import java.util.Random;

public class TalkTestUtils {

    private static final Random random = new Random();

    public static TalkFile randomTalkFile(int soundCount, int tagCount) {

        TalkFile tf = new TalkFile("random-" + random.nextInt(10000));

        // sounds
        for (int i = 0; i < soundCount; i++) {
            byte[] data = new byte[random.nextInt(1024) + 1];
            random.nextBytes(data);

            float prob = random.nextFloat();

            tf.addSound(data, prob);
        }

        // tags
        for (int i = 0; i < tagCount; i++) {
            tf.addTag("tag-" + random.nextInt(10000));
        }

        return tf;
    }

    public static byte[] corrupt(byte[] input) {
        byte[] copy = input.clone();
        for (int i = 0; i < 10; i++) {
            int pos = random.nextInt(copy.length);
            copy[pos] ^= 0xFF; // flip bits
        }
        return copy;
    }
}