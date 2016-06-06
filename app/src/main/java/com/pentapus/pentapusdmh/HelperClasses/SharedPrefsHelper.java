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
        editor.apply();
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
        editor.apply();
    }
    public static int loadEncounterId(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getInt("encounter", 0); //0 is the default value
    }

    public static String loadEncounterName(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getString("encounterName", ""); //0 is the default value
    }

    public static void savePHBFilter(Context context, boolean isEnabled){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("spellsource_phb" ,isEnabled);
        editor.apply();
    }

    public static boolean loadPHBFilter(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getBoolean("spellsource_phb", true); //0 is the default value
    }

    public static void saveEEFilter(Context context, boolean isEnabled){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("spellsource_ee" ,isEnabled);
        editor.apply();
    }

    public static boolean loadEEFilter(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getBoolean("spellsource_ee", true); //0 is the default value
    }

    public static void saveSCAGFilter(Context context, boolean isEnabled){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("spellsource_scag" ,isEnabled);
        editor.apply();
    }

    public static boolean loadSCAGFilter(Context context){
        sp = context.getApplicationContext().getSharedPreferences(settings, 0);
        return sp.getBoolean("spellsource_scag", true); //0 is the default value
    }

}
