package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.support.v4.app.Fragment;

import com.wizardpager.wizard.model.ModelCallbacks;
import com.wizardpager.wizard.model.Page;
import com.wizardpager.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Koni on 11.11.2016.
 */

public class ActionPage extends Page {
    public static final String A1NAME_DATA_KEY = "a1name";
    public static final String A1DESC_DATA_KEY = "a1desc";
    public static final String A1MOD_DATA_KEY = "a1mod";
    public static final String A1ROLL1_DATA_KEY = "a1roll1";
    public static final String A1ROLL2_DATA_KEY = "a1roll2";
    public static final String A1TYPE1_DATA_KEY = "a1type1";
    public static final String A1TYPE2_DATA_KEY = "a1type2";
    public static final String A1AUTO_DATA_KEY = "a1auto";
    public static final String A1ADD_DATA_KEY = "a1add";

    public ActionPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return ActionCreationFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Action 1 Name", mData.getString(A1NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Action 1 Description", mData.getString(A1DESC_DATA_KEY), getKey(), -1));
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
