package com.com.boha.monitor.library.util;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.boha.monitor.library.R;

public class Statics {

    /*
     * REMOTE URL - bohamaker back end - production
     */
    //
//    public static final String WEBSOCKET_URL = "ws://bohamaker.com:51490/golf/";
//    public static final String URL = "http://bohamaker.com:51490/golf/";
//    public static final String IMAGE_URL = "http://bohamaker.com:51490/golf_images/";

    //google cloud http://mggolf-303.appspot.com/golf?JSON={requestType:38,golfGroupID:21}
    //public static final String URL = "http://mggolf-303.appspot.com/";
//10.20.36.8
//    public static final String WEBSOCKET_URL = "ws://10.20.36.8:8080/mwp/";
//    public static final String URL = "http://10.20.36.8:8080/mwp/";
//    public static final String IMAGE_URL = "http://10.20.36.8:8080/";

    public static final String WEBSOCKET_URL = "ws://192.168.1.111:8080/mwp/";
    public static final String URL = "http://192.168.1.111:8080/mwp/";
    public static final String IMAGE_URL = "http://192.168.1.111:8080/";

    public static final String INVITE_DESTINATION = "https://play.google.com/store/apps/details?id=";
    public static final String INVITE_ADMIN = INVITE_DESTINATION + "com.boha.malengagolf.admin";
    public static final String INVITE_PLAYER = INVITE_DESTINATION + "com.boha.malengagolf.player";
    public static final String INVITE_SCORER = INVITE_DESTINATION + "com.boha.malengagolf.scorer";
    public static final String INVITE_LEADERBORAD = INVITE_DESTINATION + "com.boha.malengagolf.leaderboard";

    public static final String UPLOAD_URL_REQUEST = "uploadUrl?";
    public static final String UPLOAD_BLOB = "uploadBlob?";
    public static final String CRASH_REPORTS_URL = URL + "crash?";


    public static final String
            PROJECT_ENDPOINT = "wsproject",
            COMPANY_ENDPOINT = "wscompany";

    public static final String SESSION_ID = "sessionID";
    public static final int CRASH_STRING = R.string.crash_toast;

    public static void setDroidFontBold(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "DroidSerif-Bold");
        txt.setTypeface(font);
    }

    public static void setRobotoFontBoldCondensed(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-BoldCondensed.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoFontRegular(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Regular.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoFontLight(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Light.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoFontBold(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Bold.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoItalic(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Italic.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoRegular(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Regular.ttf");
        txt.setTypeface(font);
    }

}
