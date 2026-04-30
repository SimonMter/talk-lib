package com.retirejs.talk.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StringCodec {
    
    public static void writeString(DataOutputStream out, String s) throws IOException{
        byte[] data = s.getBytes(StandardCharsets.UTF_8);
        out.writeInt(data.length);
        out.write(data);
    }

    public static String readString(DataInputStream in) throws IOException{
        int len = in.readInt();
        byte[] data = new byte[len];
        in.readFully(data);
        return new String(data, StandardCharsets.UTF_8);
    }
}
