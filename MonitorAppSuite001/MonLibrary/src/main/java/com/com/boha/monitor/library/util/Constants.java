package com.com.boha.monitor.library.util;

public interface Constants 
{
	static final int NETWORK_SOURCE =  0;
	static final int DATABASE_SOURCE = 1; 
	
	//
	static final int ERROR_ENCODING = 900; 
	static final int ERROR_DATABASE = 901;
	static final int ERROR_NETWORK_UNAVAILABLE = 902;
	static final int ERROR_SERVER_COMMS = 903;
	static final int ERROR_DUPLICATE = 904;
	
	static final int ERROR_COMPRESSION_FAILURE = 904;
	static final String COMPRESSION_ERROR = "Server unable to compress response";

	static final String COUNTRIES_LOADED =  "countriesLoaded";
	
	static final String SPLASH_INDEX =  "splashIndex";
    static final String LAST_LATITUDE =  "latitude";
    static final String LAST_LONGITUDE =  "longitude";
    static final String PROVINCE_JSON =  "provJSON";
    static final String ADMINISTRATOR_JSON =  "AdminJSON";
    static final String APPUSER_JSON =  "appUserJSON";
    static final String PLAYER_JSON =  "PlayerJSON";
    static final String PARENT_JSON =  "ParentJSON";
    static final String SCORER_JSON =  "ScorerJSON";
    static final String COUNTRY_JSON =  "countryJSON";
    static final String GOLF_GROUP_JSON =  "GolfGroupJSON";
    static final String CURRENT_TOURNAMENT =  "tournJSON";
    static final String LEADERBOARD =  "lbJSON";
    static final String LEADERBOARD_CARRIERS =  "lbCarriersJSON";
    static final String TOURNAMENT_LIST =  "tournamentsJSON";
    static final String TOURNAMENT_PLAYER_LIST =  "tournamentPlayerJSON";
    static final String GOLF_GROUPS =  "tgGroupJSON";
    static final String PLAYER_LIST =  "playersJSON";
    static final String SCORER_LIST =  "scorersJSON";
    static final String ADMIN_LIST =  "adminsJSON";
	
	static final String IMAGE_URI =  "imageURI";
    static final String THUMB_URI =  "thumbUri";
    static final String NEAREST_CLUBS =  "nearestClubsJSON";
	static final String EMAIL =  "EMAIL";
    static final String VIDEO_CLIPS =  "videoClips";
	static final String PASSWORD =  "PSWD";
	
	static final String IS_NEW_GROUP = "isNewGroup";
	
}
