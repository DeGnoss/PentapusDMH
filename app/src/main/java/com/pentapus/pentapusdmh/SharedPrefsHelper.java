package com.pentapus.pentapusdmh;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Koni on 28.03.2016.
 */
public class SharedPrefsHelper {

    private static String settings = "sharedPrefs";
    static SharedPreferences sp;

    public static void saveCampaign(Context context, int value){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("campaign" ,value);
        editor.commit();
    }
    public static int loadCampaign(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getInt("campaign", 0); //0 is the default value
    }

    public static void saveEncounter(Context context, int value){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("encounter" ,value);
        editor.commit();
    }
    public static int loadEncounter(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getInt("encounter", 0); //0 is the default value
    }

}
