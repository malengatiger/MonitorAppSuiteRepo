package com.com.boha.monitor.library.util;

import android.os.Environment;
import android.util.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by aubreyM on 2014/06/13.
 */
public class ImportUtil {

    public static List<File> getImportFilesOnSD() {
        File extDir = Environment.getExternalStorageDirectory();
        String state = Environment.getExternalStorageState();
        // TODO check thru all state possibilities
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return new ArrayList<File>();
        }

        ArrayList<File> fileList = new ArrayList<File>();

        @SuppressWarnings("unchecked")
        Iterator<File> iter = FileUtils.iterateFiles(extDir, new String[]{
                "csv", "txt"}, true);

        while (iter.hasNext()) {
            File file = iter.next();
            if (file.getName().startsWith("._")) {
                continue;
            }
            fileList.add(0,file);
            Log.d(LOG, "### Import File: " + file.getAbsolutePath());
        }

        Log.i(LOG, "### sd Import Files in list : " + fileList.size());
        return fileList;
    }
    public static List<File> getImportFiles() {
        File extDir = Environment.getRootDirectory();
        String state = Environment.getExternalStorageState();
        // TODO check thru all state possibilities
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return new ArrayList<File>();
        }

        ArrayList<File> fileList = new ArrayList<File>();
        @SuppressWarnings("unchecked")
        Iterator<File> iter = FileUtils.iterateFiles(extDir, new String[]{
                "csv"}, true);

        while (iter.hasNext()) {
            File file = iter.next();
            if (file.getName().startsWith("._")) {
                continue;
            }
            fileList.add(file);
            Log.d(LOG, "### disk Import File: " + file.getAbsolutePath());
        }

        Log.i(LOG, "### Import Files in list : " + fileList.size());
        return fileList;
    }

    static final String LOG = "ImportUtil";

}
