package com.com.boha.monitor.library.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aubreyM on 2014/06/30.
 */
public class CacheUtil {

    public interface CacheUtilListener {
        public void onFileDataDeserialized(ResponseDTO response);

        public void onDataCached();

        public void onError();
    }

    static CacheUtilListener listener;
    public static final int CACHE_DATA = 1, CACHE_COUNTRIES = 3, CACHE_PHOTOS = 4, CACHE_PROJECT = 5;
    static int dataType;
    static Integer projectID;
    static ResponseDTO response;
    static Context ctx;
    static final String JSON_DATA = "data.json", JSON_COUNTRIES = "countries.json",
            JSON_PROJECT_DATA = "project_data", JSON_PHOTO = "photos.json";


    public static void cacheData(Context context, ResponseDTO r, int type, CacheUtilListener cacheUtilListener) {
        dataType = type;
        response = r;
        response.setLastCacheDate(new Date());
        listener = cacheUtilListener;
        ctx = context;
        new CacheTask().execute();
    }

    public static void cacheProjectData(Context context, ResponseDTO r, int type, Integer pID, CacheUtilListener cacheUtilListener) {
        dataType = type;
        response = r;
        response.setLastCacheDate(new Date());
        listener = cacheUtilListener;
        projectID = pID;
        ctx = context;
        new CacheTask().execute();
    }

    public static void getCachedData(Context context, int type, CacheUtilListener cacheUtilListener) {
        dataType = type;
        listener = cacheUtilListener;
        ctx = context;
        new CacheRetrieveTask().execute();
    }

    public static void getCachedProjectData(Context context, int type, Integer id, CacheUtilListener cacheUtilListener) {
        Log.d(LOG, "################ getting cached project data ..................");
        dataType = type;
        listener = cacheUtilListener;
        ctx = context;
        projectID = id;
        new CacheRetrieveTask().execute();
    }


    static class CacheTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            String json = null;
            File file = null;
            FileOutputStream outputStream;
            try {
                switch (dataType) {

                    case CACHE_PROJECT:
                        json = gson.toJson(response);
                        outputStream = ctx.openFileOutput(JSON_PROJECT_DATA + projectID + ".json", Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = ctx.getFileStreamPath(JSON_PROJECT_DATA + projectID + ".json");
                        if (file != null) {
                            Log.e(LOG, "......Project cache json written to disk,  - path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;
                    case CACHE_PHOTOS:
                        json = gson.toJson(response);
                        outputStream = ctx.openFileOutput(JSON_PHOTO, Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = ctx.getFileStreamPath(JSON_PHOTO);
                        if (file != null) {
                            Log.e(LOG, "......Photo cache json written to disk,  - path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;
                    case CACHE_DATA:
                        json = gson.toJson(response);
                        outputStream = ctx.openFileOutput(JSON_DATA, Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = ctx.getFileStreamPath(JSON_DATA);
                        if (file != null) {
                            Log.e(LOG, "......Data cache json written to disk,  - path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;
                    case CACHE_COUNTRIES:
                        json = gson.toJson(response);
                        outputStream = ctx.openFileOutput(JSON_COUNTRIES, Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = ctx.getFileStreamPath(JSON_COUNTRIES);
                        if (file != null) {
                            Log.e(LOG, "......Data cache json written to disk,  - path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;

                    default:
                        Log.e(LOG, "######### NOTHING done ...");
                        break;

                }

            } catch (IOException e) {
                Log.e(LOG, "Failed to cache data", e);
                return 9;
            }
            return 0;
        }

        private void write(FileOutputStream outputStream, String json) throws IOException {
            outputStream.write(json.getBytes());
            outputStream.close();
        }

        @Override
        protected void onPostExecute(Integer v) {
            if (listener != null) {
                if (v > 0) {
                    listener.onError();
                } else
                    listener.onDataCached();
            }
        }
    }

    static class CacheRetrieveTask extends AsyncTask<Void, Void, ResponseDTO> {

        private ResponseDTO getData(FileInputStream stream) throws IOException {
            String json = getStringFromInputStream(stream);
            ResponseDTO response = gson.fromJson(json, ResponseDTO.class);
            return response;
        }

        @Override
        protected ResponseDTO doInBackground(Void... voids) {
            ResponseDTO response = null;
            FileInputStream stream;
            try {
                switch (dataType) {
                    case CACHE_PROJECT:
                        stream = ctx.openFileInput(JSON_PROJECT_DATA + projectID + ".json");
                        response = getData(stream);
                        break;
                    case CACHE_PHOTOS:
                        stream = ctx.openFileInput(JSON_PHOTO);
                        response = getData(stream);
                        break;
                    case CACHE_DATA:
                        stream = ctx.openFileInput(JSON_DATA);
                        response = getData(stream);
                        break;
                    case CACHE_COUNTRIES:
                        stream = ctx.openFileInput(JSON_COUNTRIES);
                        response = getData(stream);
                        break;

                }

            } catch (FileNotFoundException e) {
                Log.d(LOG,"############# cache file not found. not initialised yet. no problem, creating responseDTO");
                if (dataType == CACHE_PHOTOS) {
                    response = new ResponseDTO();
                    PhotoCache pc = new PhotoCache();
                    pc.setPhotoUploadList(new ArrayList<PhotoUploadDTO>());
                    response.setPhotoCache(pc);
                }

            } catch (IOException e) {
                Log.v(LOG, "------------ Failed to retrieve cache", e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(ResponseDTO v) {
            if (v != null) {
                Log.i(LOG, "$$$$$$$$$$$$ cached data retrieved");
                listener.onFileDataDeserialized(v);
            } else {
                Log.e(LOG,"------ No cache, util returns null response object");
                listener.onError();
            }

        }
    }


    private static String getStringFromInputStream(InputStream is) throws IOException {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } finally {
            if (br != null) {
                br.close();
            }
        }
        String json = sb.toString();
        return json;

    }

    static final String LOG = "CacheUtil";
    static final Gson gson = new Gson();
}
