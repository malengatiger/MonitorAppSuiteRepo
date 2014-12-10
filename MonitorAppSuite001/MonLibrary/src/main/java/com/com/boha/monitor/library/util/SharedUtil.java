package com.com.boha.monitor.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by aubreyM on 2014/10/12.
 */
public class SharedUtil {
    static final Gson gson = new Gson();
    public static final String
            COMPANY_STAFF_JSON = "companyStaff",
            COMPANY_JSON = "company",
            GCM_REGISTRATION_ID = "gcm",
            SESSION_ID = "sessionID",
            LOG = "SharedUtil",
            REMINDER_TIME = "reminderTime",
            APP_VERSION = "appVersion";
    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    public static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(LOG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(GCM_REGISTRATION_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
        Log.e(LOG, "GCM registrationId saved in prefs! Yebo!!!");
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String registrationId = prefs.getString(GCM_REGISTRATION_ID, null);
        if (registrationId == null) {
            Log.i(LOG, "GCM Registration ID not found on device.");
            return null;
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = SharedUtil.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(LOG, "App version changed.");
            return null;
        }
        return registrationId;
    }
    public static void saveReminderTime(Context ctx, Date date) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong(REMINDER_TIME, date.getTime());
        ed.commit();
        Log.e("SharedUtil", "%%%%% reminderTime: " + date + " saved in SharedPreferences");
    }
    public static Date getReminderTime(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        long t = sp.getLong(REMINDER_TIME, 0);
        if (t == 0) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.roll(Calendar.DAY_OF_YEAR, false);
            return cal.getTime();
        }
        return new Date(t);
    }
    public static void saveSessionID(Context ctx, String sessionID) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SESSION_ID, sessionID);
        ed.commit();
        Log.e("SharedUtil", "%%%%% SessionID: " + sessionID + " saved in SharedPreferences");
    }

    public static String getSessionID(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        return sp.getString(SESSION_ID, null);
    }
    public static void saveCompanyStaff(Context ctx, CompanyStaffDTO dto) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String x = gson.toJson(dto);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(COMPANY_STAFF_JSON, x);
        ed.commit();
        Log.e("SharedUtil", "%%%%% CompanyStaff: " + dto.getFirstName() + " " + dto.getLastName() + " saved in SharedPreferences");
    }

    public static CompanyStaffDTO getCompanyStaff(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String adm = sp.getString(COMPANY_STAFF_JSON, null);
        CompanyStaffDTO golfGroup = null;
        if (adm != null) {
            golfGroup = gson.fromJson(adm, CompanyStaffDTO.class);

        }
        return golfGroup;
    }
    public static void saveCompany(Context ctx, CompanyDTO dto) {

        CompanyDTO xx = new CompanyDTO();
        xx.setCompanyName(dto.getCompanyName());
        xx.setCompanyID(dto.getCompanyID());

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String x = gson.toJson(xx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(COMPANY_JSON, x);
        ed.commit();
        Log.e("SharedUtil", "%%%%% Company: " + dto.getCompanyName() + " saved in SharedPreferences");
    }

    public static CompanyDTO getCompany(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String adm = sp.getString(COMPANY_JSON, null);
        CompanyDTO co = null;
        if (adm != null) {
            co = gson.fromJson(adm, CompanyDTO.class);

        }
        return co;
    }
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
