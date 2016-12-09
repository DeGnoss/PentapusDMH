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
    public static final String MULTIATTACK_DATA_KEY = "multiattack";

    public static final String A1NAME_DATA_KEY = "a1name";
    public static final String A1DESC_DATA_KEY = "a1desc";
    public static final String A1MOD_DATA_KEY = "a1mod";
    public static final String A1ROLL1_DATA_KEY = "a1roll1";
    public static final String A1ROLL2_DATA_KEY = "a1roll2";
    public static final String A1TYPE1_DATA_KEY = "a1type1";
    public static final String A1TYPE2_DATA_KEY = "a1type2";
    public static final String A1AUTO_DATA_KEY = "a1auto";
    public static final String A1ADD_DATA_KEY = "a1add";

    public static final String A2NAME_DATA_KEY = "a2name";
    public static final String A2DESC_DATA_KEY = "a2desc";
    public static final String A2MOD_DATA_KEY = "a2mod";
    public static final String A2ROLL1_DATA_KEY = "a2roll1";
    public static final String A2ROLL2_DATA_KEY = "a2roll2";
    public static final String A2TYPE1_DATA_KEY = "a2type1";
    public static final String A2TYPE2_DATA_KEY = "a2type2";
    public static final String A2AUTO_DATA_KEY = "a2auto";
    public static final String A2ADD_DATA_KEY = "a2add";

    public static final String A3NAME_DATA_KEY = "a3name";
    public static final String A3DESC_DATA_KEY = "a3desc";
    public static final String A3MOD_DATA_KEY = "a3mod";
    public static final String A3ROLL1_DATA_KEY = "a3roll1";
    public static final String A3ROLL2_DATA_KEY = "a3roll2";
    public static final String A3TYPE1_DATA_KEY = "a3type1";
    public static final String A3TYPE2_DATA_KEY = "a3type2";
    public static final String A3AUTO_DATA_KEY = "a3auto";
    public static final String A3ADD_DATA_KEY = "a3add";

    public static final String A4NAME_DATA_KEY = "a4name";
    public static final String A4DESC_DATA_KEY = "a4desc";
    public static final String A4MOD_DATA_KEY = "a4mod";
    public static final String A4ROLL1_DATA_KEY = "a4roll1";
    public static final String A4ROLL2_DATA_KEY = "a4roll2";
    public static final String A4TYPE1_DATA_KEY = "a4type1";
    public static final String A4TYPE2_DATA_KEY = "a4type2";
    public static final String A4AUTO_DATA_KEY = "a4auto";
    public static final String A4ADD_DATA_KEY = "a4add";

    public static final String A5NAME_DATA_KEY = "a5name";
    public static final String A5DESC_DATA_KEY = "a5desc";
    public static final String A5MOD_DATA_KEY = "a5mod";
    public static final String A5ROLL1_DATA_KEY = "a5roll1";
    public static final String A5ROLL2_DATA_KEY = "a5roll2";
    public static final String A5TYPE1_DATA_KEY = "a5type1";
    public static final String A5TYPE2_DATA_KEY = "a5type2";
    public static final String A5AUTO_DATA_KEY = "a5auto";
    public static final String A5ADD_DATA_KEY = "a5add";

    public ActionPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return ActionFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        if(mData.getString(MULTIATTACK_DATA_KEY) != null && !mData.getString(MULTIATTACK_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem("Multiattack", mData.getString(MULTIATTACK_DATA_KEY), getKey(), -1));
        }

        if(mData.getString(A1NAME_DATA_KEY) != null && !mData.getString(A1NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(A1NAME_DATA_KEY), mData.getString(A1DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(A2NAME_DATA_KEY) != null && !mData.getString(A2NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(A2NAME_DATA_KEY), mData.getString(A2DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(A3NAME_DATA_KEY) != null && !mData.getString(A3NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(A3NAME_DATA_KEY), mData.getString(A3DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(A4NAME_DATA_KEY) != null && !mData.getString(A4NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(A4NAME_DATA_KEY), mData.getString(A4DESC_DATA_KEY), getKey(), -1));
        }
        if(mData.getString(A5NAME_DATA_KEY) != null && !mData.getString(A5NAME_DATA_KEY).isEmpty()){
            dest.add(new ReviewItem(mData.getString(A5NAME_DATA_KEY), mData.getString(A5DESC_DATA_KEY), getKey(), -1));
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
