package com.com.boha.monitor.library.util;


import com.boha.malengagolf.library.R;

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

    public static final String WEBSOCKET_URL = "ws://192.168.1.111:8050/mwp/";
    public static final String URL = "http://192.168.1.111:8050/mwp/";
    public static final String IMAGE_URL = "http://192.168.1.111:8050/mwp/";

    public static final String INVITE_DESTINATION = "https://play.google.com/store/apps/details?id=";
    public static final String INVITE_ADMIN = INVITE_DESTINATION + "com.boha.malengagolf.admin";
    public static final String INVITE_PLAYER = INVITE_DESTINATION + "com.boha.malengagolf.player";
    public static final String INVITE_SCORER = INVITE_DESTINATION + "com.boha.malengagolf.scorer";
    public static final String INVITE_LEADERBORAD = INVITE_DESTINATION + "com.boha.malengagolf.leaderboard";

    public static final String SERVLET_ADMIN = "admin?JSON=";
    public static final String SERVLET_SCORER = "scorer?JSON=";
    public static final String SERVLET_PLAYER = "player?JSON=";
    public static final String SERVLET_PARENT = "parent?JSON=";
    public static final String SERVLET_PHOTO = "photo?JSON=";
    public static final String SERVLET_LOADER = "loader?JSON=";

    public static final String UPLOAD_URL_REQUEST = "uploadUrl?";
    public static final String UPLOAD_BLOB = "uploadBlob?";
    public static final String CRASH_REPORTS_URL = URL + "crash?";


    public static final String SCORER_ENDPOINT = "wsscorer",
            ADMIN_ENDPOINT = "wsadmin",
            PLAYER_ENDPOINT = "wsplayer",
            LEADERBOARD_ENDPOINT = "wsleaderboard";

    public static final String SESSION_ID = "sessionID";
    public static final int CRASH_STRING = R.string.crash_toast;


}
