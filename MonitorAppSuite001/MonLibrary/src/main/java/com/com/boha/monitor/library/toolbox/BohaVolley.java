package com.com.boha.monitor.library.toolbox;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


/**
 * Utility class that creates Volley RequestQueues and ImageLoaders
 * 
 * @author AubreyM
 * 
 */
public class BohaVolley {
    private static RequestQueue mRequestQueue;
    private static RequestQueue mStatsRequestQueue;
    private static ImageLoader mImageLoader;
    private static ImageLoader mStatsImageLoader;
    private BohaVolley() {
    }


    /**
     * Set up Volley Networking; create RequestQueue and ImageLoader
     * @param context
     */
    public static RequestQueue initialize(Context context) {
    	Log.e(LOG, "initializing Volley Networking ...");
        mRequestQueue = Volley.newRequestQueue(context);
        mStatsRequestQueue = Volley.newRequestQueue(context);
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        
        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 4;
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
        mStatsImageLoader = new ImageLoader(mStatsRequestQueue, new BitmapLruCache(cacheSize));
        Log.i(LOG, "********** Yebo! Volley Networking has been initialized, cache size: " + (cacheSize/1024) + " KB");
        return mRequestQueue;
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            initialize(context);
        } 
        return mRequestQueue;
    }
   
    public static RequestQueue getmStatsRequestQueue(Context context) {
    	 if (mStatsRequestQueue == null) {
             initialize(context);
         } 
		return mStatsRequestQueue;
	}
    public static ImageLoader getStatsImageLoader(Context context) {
        if (mStatsImageLoader == null) {
           initialize(context); 
        } 
        return mStatsImageLoader;
    }
    public static ImageLoader getImageLoader(Context context) {
        if (mImageLoader == null) {
           initialize(context); 
        } 
        return mImageLoader;
    }
   
    static final String LOG = "BohaVolley";
	

	
}
