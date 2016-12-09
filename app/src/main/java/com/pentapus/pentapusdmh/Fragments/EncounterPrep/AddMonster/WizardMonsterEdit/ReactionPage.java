package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.support.v4.app.Fragment;

import com.wizardpager.wizard.model.ModelCallbacks;
import com.wizardpager.wizard.model.Page;
import com.wizardpager.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Koni on 11.11.2016.
 */

public class ReactionPage extends Page {
    public static final String R1NAME_DATA_KEY = "r1name";
    public static final String R1DESC_DATA_KEY = "r1desc";
    public static final String R2NAME_DATA_KEY = "r2name";
    public static final String R2DESC_DATA_KEY = "r2desc";
    public static final String R3NAME_DATA_KEY = "r3name";
    public static final String R3DESC_DATA_KEY = "r3desc";
    public static final String R4NAME_DATA_KEY = "r4name";
    public static final String R4DESC_DATA_KEY = "r4desc";
    public static final String R5NAME_DATA_KEY = "r5name";
    public static final String R5DESC_DATA_KEY = "r5desc";





    public ReactionPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return ReactionFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        if(mData.getString(R1NAME_DATA_KEY) != null && !mData.getString(R1NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(R1NAME_DATA_KEY), mData.getString(R1DESC_DATA_KEY), getKey(), -1));
        }
        /*
        if(mData.getString(T2NAME_DATA_KEY) != null && !mData.getString(T2NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(T2NAME_DATA_KEY), mData.getString(T2DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(T3NAME_DATA_KEY) != null && !mData.getString(T3NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(T3NAME_DATA_KEY), mData.getString(T3DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(T4NAME_DATA_KEY) != null && !mData.getString(T4NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(T4NAME_DATA_KEY), mData.getString(T4DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(T5NAME_DATA_KEY) != null && !mData.getString(T5NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(T5NAME_DATA_KEY), mData.getString(T5DESC_DATA_KEY), getKey(), -1));
        }*/
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
