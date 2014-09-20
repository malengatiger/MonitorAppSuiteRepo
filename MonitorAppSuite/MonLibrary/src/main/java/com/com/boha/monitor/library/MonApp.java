package com.com.boha.monitor.library;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.toolbox.BitmapLruCache;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.stream.HttpUrlGlideUrlLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;

import java.io.InputStream;

/**
 * Created by aubreyM on 2014/05/17.
 */

/**
 * Created by aubreyM on 2014/05/17.
 */

@ReportsCrashes(
        formKey = "",
        formUri = Statics.CRASH_REPORTS_URL,
        customReportContent = {ReportField.APP_VERSION_NAME, ReportField.APP_VERSION_CODE,
                ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.BRAND, ReportField.STACK_TRACE,
                ReportField.PACKAGE_NAME,
                ReportField.CUSTOM_DATA,
                ReportField.LOGCAT},
        socketTimeout = 3000
)
public class MonApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "############################ onCreate MonApp has started ---------------->");

        ACRA.init(this);
        Log.e(LOG, "###### ACRA Crash Reporting has been initiated");
        initializeVolley(getApplicationContext());

        //Glide.get(this).register(GlideUrl.class, InputStream.class,
        //        new VolleyUrlLoader.Factory(requestQueue));

        Glide.get(this).register(GlideUrl.class, InputStream.class, new HttpUrlGlideUrlLoader.Factory());
        Log.e(LOG, "###### Glide has been initialised");
//        Glide.with(this)
//                .load("")
//                .centerCrop()
//                .placeholder(R.drawable.boy)
//                .crossFade()
//                .into(myImageView);
    }

    /**
     * Set up Volley Networking; create RequestQueue and ImageLoader
     *
     * @param context
     */
    public void initializeVolley(Context context) {
        Log.e(LOG, "initializing Volley Networking ...");
        requestQueue = Volley.newRequestQueue(context);
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();

        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 8;
        bitmapLruCache = new BitmapLruCache(cacheSize);
        imageLoader = new ImageLoader(requestQueue, bitmapLruCache);
        Log.i(LOG, "********** Yebo! Volley Networking has been initialized, cache size: " + (cacheSize / 1024) + " KB");

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public BitmapLruCache getBitmapLruCache() {
        return bitmapLruCache;
    }

    ImageLoader imageLoader;
    RequestQueue requestQueue;
    BitmapLruCache bitmapLruCache;
    static final String LOG = "MonApp";
}

