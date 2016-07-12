package by.hmarka.alexey.incognito.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import by.hmarka.alexey.incognito.IncognitoApplication;

/**
 * Created by lashket on 12.7.16.
 */
public class SharedPreferenceHelper {

    private static final String KEY_RADIUS = "RADIUS";
    private static final String KEY_IMEI = "IMEI";

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




}
