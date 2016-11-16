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

public class SkillsPage extends Page {
    public static final String ACROBATICS_DATA_KEY = "acrobatics";
    public static final String ANIMALHANDLING_DATA_KEY = "animalhandling";
    public static final String ARCANA_DATA_KEY = "arcana";
    public static final String ATHLETICS_DATA_KEY = "athletics";
    public static final String DECEPTION_DATA_KEY = "deception";
    public static final String HISTORY_DATA_KEY = "history";
    public static final String INSIGHT_DATA_KEY = "insight";
    public static final String INTIMIDATION_DATA_KEY = "intimidation";
    public static final String INVESTIGATION_DATA_KEY = "investigation";
    public static final String MEDICINE_DATA_KEY = "medicine";
    public static final String NATURE_DATA_KEY = "nature";
    public static final String PERCEPTION_DATA_KEY = "perception";
    public static final String PERFORMANCE_DATA_KEY = "performance";
    public static final String PERSUASION_DATA_KEY = "persuasion";
    public static final String RELIGION_DATA_KEY = "religion";
    public static final String SLEIGHTOFHAND_DATA_KEY = "sleightofhand";
    public static final String STEALTH_DATA_KEY = "stealth";
    public static final String SURVIVAL_DATA_KEY = "survival";




    public SkillsPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return SkillsFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Acrobatics", mData.getString(ACROBATICS_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Animal Handling", mData.getString(ANIMALHANDLING_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Arcana", mData.getString(ARCANA_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Athletics", mData.getString(ATHLETICS_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Deception", mData.getString(DECEPTION_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("History", mData.getString(HISTORY_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Insight", mData.getString(INSIGHT_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Intimidation", mData.getString(INTIMIDATION_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Investigation", mData.getString(INVESTIGATION_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Medicine", mData.getString(MEDICINE_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Nature", mData.getString(NATURE_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Perception", mData.getString(PERCEPTION_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Performance", mData.getString(PERFORMANCE_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Persuasion", mData.getString(PERSUASION_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Religion", mData.getString(RELIGION_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Sleight of Hand", mData.getString(SLEIGHTOFHAND_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Stealth", mData.getString(STEALTH_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Survival", mData.getString(SURVIVAL_DATA_KEY), getKey(), -1));
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
