package com.com.boha.monitor.library.util;

import android.util.Log;

import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by aubreyM on 2014/08/10.
 */
public class ZipUtil {
    static final int BUFFER = 2048;

    public static boolean unzip(File zippedFile, File unpackedFile) {
        Log.d("ZipU", "staring unzip");
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new
                    FileInputStream(zippedFile);
            ZipInputStream zis = new
                    ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("-----------------------Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];
                // write the files to the disk
                FileOutputStream fos = new
                        FileOutputStream(unpackedFile);
                dest = new
                        BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER))
                        != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String unpack(File zippedFile, File unpackedFile) {
        long start = System.currentTimeMillis();
        FileInputStream is;
        ZipInputStream zis;
        String content = null;
        try {
            is = new FileInputStream(zippedFile);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                FileOutputStream fout = new FileOutputStream(unpackedFile);
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }
            zis.close();
            content = new Scanner(unpackedFile).useDelimiter("\\A").next();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        long end = System.currentTimeMillis();
        System.out.println("###----> Unpack zip zippedFile, elapsed ms: " + (end - start));
        return content;
    }

    public static String uncompressGZip(ByteBuffer bytes) throws Exception {
        Log.w("ZipUtil", "############# packed length: " + getKilobytes(bytes.capacity()));
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
        Log.i("ZipUtil", "############# unpacked length, in KB: " + getKilobytes(res.length()));
        return res;


    }
    public static String getKilobytes(int bytes) {
        BigDecimal m = new BigDecimal(bytes).divide(new BigDecimal(1024));
        return df.format(m.doubleValue()) + " KB";
    }
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,##0.00");
    public static void unpack(ByteBuffer bb, WebSocketUtil.WebSocketListener listener) throws ZipException {
        //notify listener
        ResponseDTO response = unpackBytes(bb);
        Log.e(LOG, "##### unpack - telling listener that response object is ready after unpack");
        listener.onMessage(response);
    }

    public static ResponseDTO unpackBytes(ByteBuffer bb) throws ZipException {
        Log.d(LOG, "##### unpack - starting to unpack byte buffer: " + bb.capacity());
        InputStream is = new ByteArrayInputStream(bb.array());
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
        ResponseDTO response = null;
        Log.d(LOG, "##### before the try .....");
        try {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[BUFFER];
                int count;
                while ((count = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                }
                String filename = ze.getName();
                byte[] bytes = baos.toByteArray();
                String json = new String(bytes);
                Log.e(LOG, "#### Downloaded file: " + filename + ", length: " + json.length()
                        + "\n" + json);

                response = gson.fromJson(json, ResponseDTO.class);
            }
        } catch (Exception e) {
            Log.e(LOG, "Failed to unpack byteBuffer", e);
            throw new ZipException();
        } finally {
            try {
                zis.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new ZipException();
            }
        }
        if (response == null) throw new ZipException();
        return response;
    }
    static final String LOG = ZipUtil.class.getSimpleName();
    static final Gson gson = new Gson();

    public static class ZipException extends Exception {

    }

}
