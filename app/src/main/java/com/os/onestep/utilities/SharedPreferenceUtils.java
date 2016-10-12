package com.os.onestep.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by MeetBrahmbhatt1 on 31/01/16.
 */

public class SharedPreferenceUtils {

    public static void setLocalStorage(String key, String value, Context c)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, value);
        e.commit();
    }

    public static String getLocalStorage(String key, Context c)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getString(key, "");
    }

    public static void removeStorage(String key, Context c)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor e = sp.edit();
        e.remove(key);
        e.commit();
    }
}
