package com.com.boha.monitor.library.toolbox;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.boha.monitor.library.R;
import com.com.boha.monitor.library.MonApp;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.WebCheck;
import com.com.boha.monitor.library.util.WebCheckResult;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import static com.com.boha.monitor.library.util.Util.showErrorToast;

/**
 * Utility class to encapsulate calls to the remote server via the Volley Networking library.
 * Uses BohaVolleyListener to inform caller on status of the communications request
 *
 * @author Aubrey Malabie
 */
public class BaseVolley {

    /**
     * Informs whoever implements this interface when a communications request is concluded
     */
    public interface BohaVolleyListener {
        public void onResponseReceived(ResponseDTO response);

        public void onVolleyError(VolleyError error);
    }

    private static void setVolley(Context ctx) {
        requestQueue = BohaVolley.getRequestQueue(ctx);
    }

    static BohaVolleyListener bohaVolleyListener;

    public static boolean checkNetworkOnDevice(Context context) {
        ctx = context;
        WebCheckResult r = WebCheck.checkNetworkAvailability(ctx);
        if (r.isNetworkUnavailable()) {
            showErrorToast(
                    ctx,
                    ctx.getResources().getString(
                            com.boha.monitor.library.R.string.error_network_unavailable)
            );
            return false;
        }

        return true;
    }

    /**
     * This method gets a Volley based communications request started
     *
     * @param suffix   the suffix pointing to the destination servlet
     * @param request  the request object in JSON format
     * @param context  the Activity context
     * @param listener the listener implementor who wants to know abdout call status
     */
    public static void getRemoteData(String suffix, RequestDTO request,
                                     Context context, MonApp app, BohaVolleyListener listener) {

        ctx = context;
        bohaVolleyListener = listener;
        if (requestQueue == null) {
            Log.w(LOG, "------------- getRemoteData requestQueue is null - get from app");
            requestQueue = app.getRequestQueue();
        }
        String json = null, jj = null;
        Gson gson = new Gson();
        try {
            jj = gson.toJson(request);
            json = URLEncoder.encode(jj, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        retries = 0;
        String x = Statics.URL + suffix + json;
        Log.i(LOG, "...sending remote request: ....size: " + x.length() + "...>\n" + Statics.URL + suffix + jj);
        bohaRequest = new BohaRequest(Method.POST, x,
                onSuccessListener(), onErrorListener());
        bohaRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(120),
                0, 0));
        requestQueue.add(bohaRequest);
    }
    /**
     * This method gets a Volley based communications request started
     *
     * @param suffix   the suffix pointing to the destination servlet
     * @param request  the request object in JSON format
     * @param context  the Activity context
     * @param listener the listener implementor who wants to know abdout call status
     */
    public static void getRemoteData(String suffix, RequestDTO request,
                                     Context context, BohaVolleyListener listener) {

        ctx = context;
        bohaVolleyListener = listener;
        if (requestQueue == null) {
            Log.w(LOG, "getRemoteData requestQueue is null, getting it ...: ");
            requestQueue = BohaVolley.getRequestQueue(ctx);
        } else {
            Log.e(LOG, "********** getRemoteData requestQueue is NOT NULL - Kool");
        }
        String json = null, jj = null;

        Gson gson = new Gson();
        try {
            jj = gson.toJson(request);
            json = URLEncoder.encode(jj, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        retries = 0;
        String x = Statics.URL + suffix + json;
        Log.i(LOG, "...sending remote request: ....size: "+ x.length() +"...>\n"  + Statics.URL + suffix + jj);
        bohaRequest = new BohaRequest(Method.POST, x,
                onSuccessListener(), onErrorListener());
        bohaRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(120),
                0, 0));
        requestQueue.add(bohaRequest);
    }

    public static void getUploadUrl(Context context, BohaVolleyListener listener) {

        ctx = context;
        bohaVolleyListener = listener;
        if (requestQueue == null) {
            Log.w(LOG, "getUploadUrl requestQueue is null, getting it ...: ");
            requestQueue = BohaVolley.getRequestQueue(ctx);
        } else {
            Log.e(LOG, "********** getUploadUrl requestQueue is NOT NULL - Kool");
        }

        retries = 0;
        String x = Statics.URL + Statics.UPLOAD_URL_REQUEST;
        Log.i(LOG, "...sending remote request: ....size: "+ x.length() +"...>\n"
                + x);
        bohaRequest = new BohaRequest(Method.GET, x,
                onSuccessListener(), onErrorListener());
        bohaRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(120),
                0, 0));
        requestQueue.add(bohaRequest);
    }
    public static void getRemoteData(String suffix, RequestDTO request,
                                     Context context, int timeOutSeconds, BohaVolleyListener listener) {

        ctx = context;
        bohaVolleyListener = listener;
        if (requestQueue == null) {
            Log.w(LOG, "getRemoteData requestQueue is null, getting it ...: ");
            requestQueue = BohaVolley.getRequestQueue(ctx);
        } else {
            Log.e(LOG, "********** getRemoteData requestQueue is NOT NULL - Kool");
        }
        String json = null, jj = null;

        Gson gson = new Gson();
        try {
            jj = gson.toJson(request);
            json = URLEncoder.encode(jj, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        retries = 0;
        String x = Statics.URL + suffix + json;
        Log.i(LOG, "...sending remote request: ...size: "+ x.length() +"...>\n"  + Statics.URL + suffix + jj);
        bohaRequest = new BohaRequest(Method.POST, x,
                onSuccessListener(), onErrorListener());
        bohaRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(timeOutSeconds),
                0, 0));
        requestQueue.add(bohaRequest);
    }

    private static Response.Listener<ResponseDTO> onSuccessListener() {
        return new Response.Listener<ResponseDTO>() {
            @Override
            public void onResponse(ResponseDTO r) {
                response = r;
                Log.e(LOG, "Yup! ...response object received, status code: " + r.getStatusCode());

                if (r.getStatusCode() > 0) {
                    try {
                        Log.w(LOG, response.getMessage());
                    } catch (Exception e) {}
                }
                bohaVolleyListener.onResponseReceived(response);

            }
        };
    }

    private static Response.ErrorListener onErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    retries++;
                    if (retries < MAX_RETRIES) {
                        waitABit();
                        Log.e(LOG, "onErrorResponse: Retrying after timeout error ...retries = " + retries);
                        requestQueue.add(bohaRequest);
                        return;
                    }
                }
                if (error instanceof NetworkError) {
                    Log.w(LOG, "onErrorResponse Network Error: ");
                    NetworkError ne = (NetworkError) error;
                    if (ne.networkResponse != null) {
                        Log.e(LOG, "volley networkResponse status code: "
                                + ne.networkResponse.statusCode);
                    }
                    retries++;
                    if (retries < MAX_RETRIES) {
                        waitABit();
                        Log.e(LOG, "onErrorResponse: Retrying after NetworkError ...retries = " + retries);
                        requestQueue.add(bohaRequest);
                        return;
                    }
                    Log.e(LOG, ctx.getResources().getString(
                            R.string.error_server_unavailable) + "\n" + error.toString());

                } else {
                    Log.e(LOG, ctx.getResources().getString(
                            R.string.error_server_comms) + error.toString());
                }
                bohaVolleyListener.onVolleyError(error);
            }
        };
    }

    private static void waitABit() {
        Log.d(LOG, "...going to sleep for: " + (SLEEP_TIME/1000) + " seconds before retrying.....");
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static ResponseDTO response;
    private static Context ctx;
    protected static BohaRequest bohaRequest;
    protected static RequestQueue requestQueue;
    protected ImageLoader imageLoader;
    protected static String suff;
    static final String LOG = "BaseVolley";
    static final int MAX_RETRIES = 10;
    static final long SLEEP_TIME = 3000;


    static int retries;


    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
