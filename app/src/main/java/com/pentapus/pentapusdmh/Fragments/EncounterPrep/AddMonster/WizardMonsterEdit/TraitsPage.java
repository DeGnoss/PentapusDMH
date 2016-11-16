package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.support.v4.app.Fragment;

import com.wizardpager.wizard.model.ModelCallbacks;
import com.wizardpager.wizard.model.Page;
import com.wizardpager.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Koni on 11.11.2016.
 */

public class TraitsPage extends Page {
    public static final String T1NAME_DATA_KEY = "t1name";
    public static final String T1DESC_DATA_KEY = "t1desc";
    public static final String T2NAME_DATA_KEY = "t2name";
    public static final String T2DESC_DATA_KEY = "t2desc";





    public TraitsPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return TraitsFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Trait 1 Name", mData.getString(T1NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Trait 1 Description", mData.getString(T1DESC_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Trait 2 Name", mData.getString(T2NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Trait 2 Description", mData.getString(T2DESC_DATA_KEY), getKey(), -1));
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
