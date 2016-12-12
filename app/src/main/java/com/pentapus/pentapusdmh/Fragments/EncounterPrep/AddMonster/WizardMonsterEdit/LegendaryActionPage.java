package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.support.v4.app.Fragment;

import com.wizardpager.wizard.model.ModelCallbacks;
import com.wizardpager.wizard.model.Page;
import com.wizardpager.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Koni on 11.11.2016.
 */

public class LegendaryActionPage extends Page {
    public static final String LMULTIATTACK_DATA_KEY = "lmultiattack";

    public static final String LA1NAME_DATA_KEY = "la1name";
    public static final String LA1DESC_DATA_KEY = "la1desc";
    public static final String LA1MOD_DATA_KEY = "la1mod";
    public static final String LA1ROLL1_DATA_KEY = "la1roll1";
    public static final String LA1ROLL2_DATA_KEY = "la1roll2";
    public static final String LA1TYPE1_DATA_KEY = "la1type1";
    public static final String LA1TYPE2_DATA_KEY = "la1type2";
    public static final String LA1AUTO_DATA_KEY = "la1auto";
    public static final String LA1ADD_DATA_KEY = "la1add";

    public static final String LA2NAME_DATA_KEY = "la2name";
    public static final String LA2DESC_DATA_KEY = "la2desc";
    public static final String LA2MOD_DATA_KEY = "la2mod";
    public static final String LA2ROLL1_DATA_KEY = "la2roll1";
    public static final String LA2ROLL2_DATA_KEY = "la2roll2";
    public static final String LA2TYPE1_DATA_KEY = "la2type1";
    public static final String LA2TYPE2_DATA_KEY = "la2type2";
    public static final String LA2AUTO_DATA_KEY = "la2auto";
    public static final String LA2ADD_DATA_KEY = "la2add";

    public static final String LA3NAME_DATA_KEY = "la3name";
    public static final String LA3DESC_DATA_KEY = "la3desc";
    public static final String LA3MOD_DATA_KEY = "la3mod";
    public static final String LA3ROLL1_DATA_KEY = "la3roll1";
    public static final String LA3ROLL2_DATA_KEY = "la3roll2";
    public static final String LA3TYPE1_DATA_KEY = "la3type1";
    public static final String LA3TYPE2_DATA_KEY = "la3type2";
    public static final String LA3AUTO_DATA_KEY = "la3auto";
    public static final String LA3ADD_DATA_KEY = "la3add";

    public static final String LA4NAME_DATA_KEY = "la4name";
    public static final String LA4DESC_DATA_KEY = "la4desc";
    public static final String LA4MOD_DATA_KEY = "la4mod";
    public static final String LA4ROLL1_DATA_KEY = "la4roll1";
    public static final String LA4ROLL2_DATA_KEY = "la4roll2";
    public static final String LA4TYPE1_DATA_KEY = "la4type1";
    public static final String LA4TYPE2_DATA_KEY = "la4type2";
    public static final String LA4AUTO_DATA_KEY = "la4auto";
    public static final String LA4ADD_DATA_KEY = "la4add";

    public static final String LA5NAME_DATA_KEY = "la5name";
    public static final String LA5DESC_DATA_KEY = "la5desc";
    public static final String LA5MOD_DATA_KEY = "la5mod";
    public static final String LA5ROLL1_DATA_KEY = "la5roll1";
    public static final String LA5ROLL2_DATA_KEY = "la5roll2";
    public static final String LA5TYPE1_DATA_KEY = "la5type1";
    public static final String LA5TYPE2_DATA_KEY = "la5type2";
    public static final String LA5AUTO_DATA_KEY = "la5auto";
    public static final String LA5ADD_DATA_KEY = "la5add";

    public LegendaryActionPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return LegendaryActionFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        if(mData.getString(LMULTIATTACK_DATA_KEY) != null && !mData.getString(LMULTIATTACK_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem("Legendary Actions", mData.getString(LMULTIATTACK_DATA_KEY), getKey(), -1));
        }

        if(mData.getString(LA1NAME_DATA_KEY) != null && !mData.getString(LA1NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(LA1NAME_DATA_KEY), mData.getString(LA1DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(LA2NAME_DATA_KEY) != null && !mData.getString(LA2NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(LA2NAME_DATA_KEY), mData.getString(LA2DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(LA3NAME_DATA_KEY) != null && !mData.getString(LA3NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(LA3NAME_DATA_KEY), mData.getString(LA3DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(LA4NAME_DATA_KEY) != null && !mData.getString(LA4NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(LA4NAME_DATA_KEY), mData.getString(LA4DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(LA5NAME_DATA_KEY) != null && !mData.getString(LA5NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(LA5NAME_DATA_KEY), mData.getString(LA5DESC_DATA_KEY), getKey(), -1));
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
