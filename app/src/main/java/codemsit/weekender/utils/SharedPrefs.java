package codemsit.weekender.utils;

import android.content.Context;
import android.content.SharedPreferences;

import codemsit.weekender.App;


/**
 * Created by aman on 2/10/15.
 */
public class SharedPrefs {
    static String PREF_FILE_NAME = "sharedPrefs";
    static Context context = App.getAppContext();

    public static void setPrefs (String preferenceName,String preferenceValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();

    }

    public static void setPrefs (String preferenceName,boolean preferenceValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();

    }

    public static void setPrefs (String preferenceName,int preferenceValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(preferenceName, preferenceValue);
        editor.apply();

    }

    public static String getPrefs(String preferenceName,String defaultValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);

    }

    public static boolean getPrefs(String preferenceName,boolean defaultValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(preferenceName, defaultValue);

    }

    public static int getPrefs(String preferenceName,int defaultValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getInt(preferenceName, defaultValue);

    }

    public static void delPrefs (String preferenceName){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(preferenceName);
        editor.apply();

    }
}
