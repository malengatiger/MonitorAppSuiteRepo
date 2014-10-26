package com.com.boha.monitor.library;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.stream.HttpUrlGlideUrlLoader;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.toolbox.BitmapLruCache;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;

import java.io.File;
import java.io.InputStream;

/**
 * Created by aubreyM on 2014/05/17.
 * Copyright (c) 2014 Aubrey Malabie. All rights reserved.
 */


@ReportsCrashes(
        formKey = "",
        formUri = Statics.CRASH_REPORTS_URL,
        customReportContent = {ReportField.APP_VERSION_NAME, ReportField.APP_VERSION_CODE,
                ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.BRAND, ReportField.STACK_TRACE,
                ReportField.PACKAGE_NAME,
                ReportField.CUSTOM_DATA,
                ReportField.LOGCAT},
        socketTimeout = 10000
)
public class MonApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "############################ onCreate MonApp has started ---------------->");

        ACRA.init(this);
        CompanyDTO company = SharedUtil.getCompany(getApplicationContext());
        if (company != null) {
            ACRA.getErrorReporter().putCustomData("companyID", "" + company.getCompanyID());
            ACRA.getErrorReporter().putCustomData("companyName", company.getCompanyName());
        }

        Log.e(LOG, "###### ACRA Crash Reporting has been initiated");
        initializeVolley(getApplicationContext());

        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(4 * 1024 * 1024))
                .memoryCacheSize(4 * 1024 * 1024)
                .diskCacheSize(300 * 1024 * 1024)
                .diskCacheFileCount(300)
                .writeDebugLogs()
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        Log.w(LOG, "###### ImageLoaderConfiguration has been initialised");



        //Glide.get(this).register(GlideUrl.class, InputStream.class,
        //        new VolleyUrlLoader.Factory(requestQueue));

        Glide.get(this).register(GlideUrl.class, InputStream.class, new HttpUrlGlideUrlLoader.Factory());

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
       // imageLoader = new ImageLoader(requestQueue, bitmapLruCache);
        Log.i(LOG, "********** Yebo! Volley Networking has been initialized, cache size: " + (cacheSize / 1024) + " KB");

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
    }



    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public BitmapLruCache getBitmapLruCache() {
        return bitmapLruCache;
    }

    RequestQueue requestQueue;
    BitmapLruCache bitmapLruCache;
    static final String LOG = "MonApp";
}

