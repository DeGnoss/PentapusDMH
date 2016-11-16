package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.wizardpager.wizard.model.ModelCallbacks;
import com.wizardpager.wizard.model.Page;
import com.wizardpager.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Koni on 11.11.2016.
 */

public class AbilitiesPage extends Page {
    public static final String STR_DATA_KEY = "str";
    public static final String DEX_DATA_KEY = "dex";
    public static final String CON_DATA_KEY = "con";
    public static final String INT_DATA_KEY = "int";
    public static final String WIS_DATA_KEY = "wis";
    public static final String CHA_DATA_KEY = "cha";

    public static final String HP_DATA_KEY = "hp";
    public static final String HITDICE_DATA_KEY = "hitdice";

    public static final String AC_DATA_KEY = "ac";
    public static final String ACTYPE_DATA_KEY = "actype";

    public static final String STSTR_DATA_KEY = "ststr";
    public static final String STDEX_DATA_KEY = "stdex";
    public static final String STCON_DATA_KEY = "stcon";
    public static final String STINT_DATA_KEY = "stint";
    public static final String STWIS_DATA_KEY = "stwis";
    public static final String STCHA_DATA_KEY = "stcha";



    public AbilitiesPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return AbilitiesFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("STR", mData.getString(STR_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("DEX", mData.getString(DEX_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("CON", mData.getString(CON_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("INT", mData.getString(INT_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("WIS", mData.getString(WIS_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("CHA", mData.getString(CHA_DATA_KEY), getKey(), -1));

        dest.add(new ReviewItem("HP", mData.getString(HP_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("HITDICE", mData.getString(HITDICE_DATA_KEY), getKey(), -1));

        dest.add(new ReviewItem("AC", mData.getString(AC_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("AC TYPE", mData.getString(ACTYPE_DATA_KEY), getKey(), -1));

        dest.add(new ReviewItem("ST STR", mData.getString(STSTR_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("ST DEX", mData.getString(STDEX_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("ST CON", mData.getString(STCON_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("ST INT", mData.getString(STINT_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("ST WIS", mData.getString(STWIS_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("ST CHA", mData.getString(STCHA_DATA_KEY), getKey(), -1));
    }

    @Override
    public String getAvatarUri() {
        return null;
    }


    @Override
    public boolean isCompleted() {
        return true;
    }
}
