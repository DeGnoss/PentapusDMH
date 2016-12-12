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

public class BasicInfoPage extends Page {
    public static final String NAME_DATA_KEY = "monstername";
    public static final String TYPE_DATA_KEY = "type";
    public static final String ALIGNMENT_DATA_KEY = "alignment";
    public static final String IMAGEURI_DATA_KEY = "imageuri";
    public static final String SIZE_DATA_KEY = "size";
    BasicInfoFragment fragment;


    public BasicInfoPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        fragment = BasicInfoFragment.create(getKey());
        return fragment;
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Name", mData.getString(NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Type", mData.getString(TYPE_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Alignment", mData.getString(ALIGNMENT_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Size", mData.getString(SIZE_DATA_KEY), getKey(), -1));

    }

    @Override
    public String getAvatarUri() {
        return mData.getString(IMAGEURI_DATA_KEY);
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(NAME_DATA_KEY));
    }
}
