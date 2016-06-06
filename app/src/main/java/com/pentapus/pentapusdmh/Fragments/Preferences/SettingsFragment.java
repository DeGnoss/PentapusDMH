package com.pentapus.pentapusdmh.Fragments.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String PREFS_FILE = "prefFilePentapus";
    private Context myContext;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName(PREFS_FILE);
        prefMgr.setSharedPreferencesMode(Activity.MODE_PRIVATE);

        addPreferencesFromResource(R.xml.app_preferences);

        ((MainActivity) getActivity()).setFabVisibility(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");

        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            initSummary(getPreferenceScreen().getPreference(i));
            //getPreferenceScreen().getPreference(i).setOnPreferenceClickListener(this);
            //getPreferenceScreen().getPreference(i).setOnPreferenceChangeListener(this);
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    public static boolean isAutoRollEnabled(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return sp.getBoolean(context.getString(R.string.prefs_key_app_initiative_autoroll), false);
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceCategory) {
            PreferenceCategory pCat = (PreferenceCategory) p;
            for (int i = 0; i < pCat.getPreferenceCount(); i++) {
                initSummary(pCat.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        } else if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            p.setSummary(editTextPref.getText());
        }
    }
}
