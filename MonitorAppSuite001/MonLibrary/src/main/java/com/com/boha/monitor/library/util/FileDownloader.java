package com.com.boha.monitor.library.util;

/**
 * Created by aubreyM on 14/11/22.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader {
    private static final int  MEGABYTE = 1024 * 1024;
    public interface FileDownloaderListener {
        public void onFileDownloaded(File file);
        public void onError();
    }
    static Integer ccID, cID, pID;
    static FileDownloaderListener listener;
    static File pdfFile;

    public static void downloadContractorClaimPDF(Context ctx,
    Integer contractorClaimID, Integer companyID, Integer projectID,
        FileDownloaderListener mFileDownloaderListener){
        ccID = contractorClaimID;
        cID = companyID;
        pID = projectID;
        listener = mFileDownloaderListener;
        new DownTask().execute();
    }
    static class DownTask extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... params) {
            try {
                File directory = Environment.getExternalStorageDirectory();
                pdfFile = new File(directory,"contractorClaim.pdf");
                StringBuilder sb = new StringBuilder();
                sb.append(Statics.PDF_URL);
                sb.append("company").append(cID).append("/");
                sb.append("project").append(pID).append("/");
                sb.append("contractorClaims").append("/");
                sb.append("contractorClaim").append(ccID);
                sb.append(".pdf");

                URL url = new URL(sb.toString());
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                int totalSize = urlConnection.getContentLength();
                System.out.println("##### downloaded content size of pdf: " + totalSize);

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
                System.out.println("##### pdf file: " + pdfFile.getAbsolutePath()
                + " length: " + pdfFile.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return 9;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return 9;
            } catch (IOException e) {
                e.printStackTrace();
                return 9;
            }
            return 0;
        }
        @Override
        public void onPostExecute(Integer res) {
            if (res > 0) {
               listener.onError();
                return;
            }
            listener.onFileDownloaded(pdfFile);
        }
    }

}
