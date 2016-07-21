package by.hmarka.alexey.incognito.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import by.hmarka.alexey.incognito.BuildConfig;
import by.hmarka.alexey.incognito.IncognitoApplication;

/**
 * Created by lashket on 12.7.16.
 */
public class SharedPreferenceHelper {

    private static final String KEY_RADIUS = "RADIUS";
    private static final String KEY_IMEI = "IMEI";
    private static final String KEY_LAT = "LAT";
    private static final String KEY_LNG = "LNG";
    private static final String KEY_ACCESS_TYPE = "ACCESSTYPE";
    private static final String KEY_MAX_POST_LENGHT = "POSTLENGHT";

    private static SharedPreferences prefs;

    public static void setImei(String imei) {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_IMEI, imei);
        editor.commit();
    }


    public static String getImei() {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        return prefs.getString(KEY_IMEI, "");
    }

    public static void setRadius(String radius) {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_RADIUS, radius);
        editor.commit();
    }

    public static String getRadius() {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        return prefs.getString(KEY_RADIUS, "");
    }

    public static void setLocationLattitude(String lat) {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LAT, lat);
        editor.commit();
    }

    public static String getLocationLattitude() {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        return prefs.getString(KEY_LAT, "");
    }

    public static void setLocationLongitude(String lng) {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LNG, lng);
        editor.commit();
    }

    public static String getLocationLongitude() {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        return prefs.getString(KEY_LNG, "");
    }

    public static void setAccessType(String accessType) {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACCESS_TYPE, accessType);
        editor.commit();
    }

    public static String getAccessType() {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        return prefs.getString(KEY_ACCESS_TYPE, "");
    }

    public static void setMaxPostLenght(int maxLenght) {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_ACCESS_TYPE, maxLenght);
        editor.commit();
    }

    public static int getMaxPostLenght() {
        prefs = PreferenceManager.getDefaultSharedPreferences(IncognitoApplication.getInstance());
        return prefs.getInt(KEY_MAX_POST_LENGHT, 500);
    }

}
