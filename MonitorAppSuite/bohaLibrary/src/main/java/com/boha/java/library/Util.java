package com.boha.java.library;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

public class Util {

    public static String uncompressGZip(ByteBuffer bytes) throws Exception {
        System.out.println("ZipUtil - ############# packed length: " + bytes.capacity());
        GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes.array()));
        OutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = gzipInputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        gzipInputStream.close();
        out.close();

        String res = out.toString();
        System.out.println("ZipUtil - ############# unpacked length: " + res.length());
        return res;


    }
}
