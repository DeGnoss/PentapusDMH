package com.pentapus.pentapusdmh.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Koni on 28.03.2016.
 */
public class SharedPrefsHelper {

    private static String settings = "prefFilePentapus";
    static SharedPreferences sp;

    public static void saveCampaign(Context context, int value, String name){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("campaign" ,value);
        editor.putString("campaignName", name);
        editor.commit();
    }
    public static int loadCampaignId(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getInt("campaign", 0); //0 is the default value
    }

    public static String loadCampaignName(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getString("campaignName", ""); //0 is the default value
    }

    public static void saveEncounter(Context context, int value, String name){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("encounter" ,value);
        editor.putString("encounterName", name);
        editor.commit();
    }
    public static int loadEncounterId(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getInt("encounter", 0); //0 is the default value
    }

    public static String loadEncounterName(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getString("encounterName", ""); //0 is the default value
    }

}
