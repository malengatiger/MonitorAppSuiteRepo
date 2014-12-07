package com.com.boha.monitor.library.util;

/**
 * Created by aubreyM on 2014/10/12.
 */

/**
 * Created by aubreyM on 2014/05/11.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class GCMUtil {
    public interface  GCMUtilListener {
        public void onDeviceRegistered(String id);
        public void onGCMError();
    }
    static Context ctx;
    static GCMUtilListener gcmUtilListener;
    static String registrationID, msg;
    static final String LOG = "GCMUtil";
    static GoogleCloudMessaging gcm;

    public static void startGCMRegistration(Context context, GCMUtilListener listener) {
        ctx = context;
        gcmUtilListener = listener;
        new GCMTask().execute();
    }
    public static final String GCM_SENDER_ID = "635788281460";

    static class GCMTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Log.e(LOG, "... startin GCM registration");
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(ctx);
                }
                registrationID = gcm.register(GCM_SENDER_ID);
                msg = "Device registered, registration ID = \n" + registrationID;
                SharedUtil.storeRegistrationId(ctx, registrationID);
                RequestDTO w = new RequestDTO();
                w.setRequestType(RequestDTO.SEND_GCM_REGISTRATION);
                w.setGcmRegistrationID(registrationID);
                WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
                    @Override
                    public void onMessage(ResponseDTO response) {
                        if (response.getStatusCode() == 0) {
                            Log.w(LOG, "############ Device registered on server GCM regime");
                        }
                    }

                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onError(final String message) {
                        Log.e(LOG, "############ Device failed to register on server GCM regime\n" + message);

                    }
                });
                Log.i(LOG, msg);

            } catch (IOException e) {
                return Constants.ERROR_SERVER_COMMS;
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i(LOG, "onPostExecute... ending GCM registration");
            if (result > 0) {
                gcmUtilListener.onGCMError();
                ErrorUtil.handleErrors(ctx, result);
                return;
            }
            gcmUtilListener.onDeviceRegistered(registrationID);
            Log.i(LOG, "onPostExecute GCM device registered OK");
        }

    }




    public static final int SHOW_GOOGLE_PLAY_DIALOG = 1, GOOGLE_PLAY_ERROR = 2, OK = 3;







}
