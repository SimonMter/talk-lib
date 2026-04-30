
package com.retirejs.talk.util;

import java.io.*;

public class ByteUtils{
    public static void writeInt(DataOutputStream out, int v) throws IOException {
        out.writeInt(v);
    }

    public static int readInt(DataInputStream in) throws IOException {
        return in.readInt();
    }


    public static void writeLong(DataOutputStream out, long v) throws IOException {
        out.writeLong(v);
    }

    public static long readLong(DataInputStream in) throws IOException {
        return in.readLong();
    }
}
