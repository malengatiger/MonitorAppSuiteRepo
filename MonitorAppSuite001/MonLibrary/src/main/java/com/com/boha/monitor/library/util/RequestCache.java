package com.com.boha.monitor.library.util;

import android.content.Context;

import com.com.boha.monitor.library.dto.transfer.RequestDTO;

import java.util.List;

/**
 * Created by aubreyM on 14/11/29.
 */
public class RequestCache {
    public interface RequestCacheListener {

    }
    private static RequestCacheListener listener;
    private static Context ctx;
    private static RequestDTO request;
    private List<RequestDTO> requestList;

    public static void cacheRequest(Context c, RequestDTO r, RequestCacheListener l) {
        ctx = c;
        request = r;
        listener = l;
    }
    public static void getCachedRequests(Context c, RequestCacheListener l) {
        ctx = c;
        listener = l;
    }
}
