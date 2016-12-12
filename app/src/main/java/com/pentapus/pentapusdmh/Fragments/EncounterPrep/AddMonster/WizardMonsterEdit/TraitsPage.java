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
    public static final String T3NAME_DATA_KEY = "t3name";
    public static final String T3DESC_DATA_KEY = "t3desc";
    public static final String T4NAME_DATA_KEY = "t4name";
    public static final String T4DESC_DATA_KEY = "t4desc";
    public static final String T5NAME_DATA_KEY = "t5name";
    public static final String T5DESC_DATA_KEY = "t5desc";

    public static final String SC_DATA_KEY = "spellcasting";
    public static final String SCDESCRIPTION_DATA_KEY = "scdescription";
    public static final String SClEVEL_DATA_KEY = "sclevel";
    public static final String SCABILITY_DATA_KEY = "scability";
    public static final String SCMOD_DATA_KEY = "scmod";
    public static final String SCDC_DATA_KEY = "scdc";
    public static final String SCCLASS_DATA_KEY = "scclass";
    public static final String SCSPELLSKNOWNSTRING_DATA_KEY = "scspellsknownstring";
    public static final String SCSPELLSKNOWN_DATA_KEY = "scspellsknown";
    public static final String SCSLOTS_DATA_KEY = "scslots";


    public static final String INNATE_DATA_KEY = "innate";
    public static final String INNATEDESCRIPTION_DATA_KEY = "innatedescription";
    public static final String INNATEABILITY_DATA_KEY = "innateability";
    public static final String INNATEMOD_DATA_KEY = "innatemod";
    public static final String INNATEDC_DATA_KEY = "innatedc";
    public static final String INNATESPELLSKNOWNSTRING_DATA_KEY = "innatespellsknownstring";
    public static final String INNATESPELLSKNOWN_DATA_KEY = "innatespellsknown";

    public static final String NAME_DATA_KEY = "name";








    public TraitsPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return TraitsFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        if(mData.getString(T1NAME_DATA_KEY) != null && !mData.getString(T1NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(T1NAME_DATA_KEY), mData.getString(T1DESC_DATA_KEY), getKey(), -1));
        }
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
        }
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
