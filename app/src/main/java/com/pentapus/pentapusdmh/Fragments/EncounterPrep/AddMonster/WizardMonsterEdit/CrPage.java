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

public class CrPage extends Page {
    public static final String CR_DATA_KEY = "cr";
    public static final String XP_DATA_KEY = "xp";
    public static final String SPELLCASTER_DATA_KEY = "spellcaster";
    public static final String SPELLS_DATA_KEY = "spells";
    CrFragment fragment;


    public CrPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        fragment = CrFragment.create(getKey());
        return fragment;
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("CR", mData.getString(CR_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("XP", mData.getString(XP_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Spells known", mData.getString(SPELLS_DATA_KEY), getKey(), -1));
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
