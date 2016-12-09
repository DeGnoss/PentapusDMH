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

    public static final String DMGVUL_DATA_KEY = "dmgvul";
    public static final String DMGRES_DATA_KEY = "dmgres";
    public static final String DMGIM_DATA_KEY = "dmgim";
    public static final String CONIM_DATA_KEY = "conim";

    public static final String STSTR_DATA_KEY = "ststr";
    public static final String STDEX_DATA_KEY = "stdex";
    public static final String STCON_DATA_KEY = "stcon";
    public static final String STINT_DATA_KEY = "stint";
    public static final String STWIS_DATA_KEY = "stwis";
    public static final String STCHA_DATA_KEY = "stcha";

    public static final String SENSES_DATA_KEY = "senses";
    public static final String WISDOM_DATA_KEY = "wisdom";
    public static final String ISCALCULATEDFROMWISDOM_DATA_KEY = "isNotCalculatedFromWisdom";

    public static final String LANGUAGES_DATA_KEY = "languages";








    public SkillsPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return SkillsFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        String acrobatics = mData.getString(SkillsPage.ACROBATICS_DATA_KEY);
        String animalhandling = mData.getString(SkillsPage.ANIMALHANDLING_DATA_KEY);
        String arcana = mData.getString(SkillsPage.ARCANA_DATA_KEY);
        String  athletics = mData.getString(SkillsPage.ATHLETICS_DATA_KEY);
        String deception = mData.getString(SkillsPage.DECEPTION_DATA_KEY);
        String history = mData.getString(SkillsPage.HISTORY_DATA_KEY);
        String insight = mData.getString(SkillsPage.INSIGHT_DATA_KEY);
        String intimidation = mData.getString(SkillsPage.INTIMIDATION_DATA_KEY);
        String investigation = mData.getString(SkillsPage.INVESTIGATION_DATA_KEY);
        String medicine = mData.getString(SkillsPage.MEDICINE_DATA_KEY);
        String nature = mData.getString(SkillsPage.NATURE_DATA_KEY);
        String perception = mData.getString(SkillsPage.PERCEPTION_DATA_KEY);
        String performance = mData.getString(SkillsPage.PERFORMANCE_DATA_KEY);
        String persuasion = mData.getString(SkillsPage.PERSUASION_DATA_KEY);
        String religion = mData.getString(SkillsPage.RELIGION_DATA_KEY);
        String sleightofhand = mData.getString(SkillsPage.SLEIGHTOFHAND_DATA_KEY);
        String stealth = mData.getString(SkillsPage.STEALTH_DATA_KEY);
        String survival = mData.getString(SkillsPage.SURVIVAL_DATA_KEY);

        String stStr = mData.getString(SkillsPage.STSTR_DATA_KEY);
        String stDex = mData.getString(SkillsPage.STDEX_DATA_KEY);
        String stCon = mData.getString(SkillsPage.STCON_DATA_KEY);
        String stInt = mData.getString(SkillsPage.STINT_DATA_KEY);
        String stWis = mData.getString(SkillsPage.STWIS_DATA_KEY);
        String stCha = mData.getString(SkillsPage.STCHA_DATA_KEY);

        String skills = buildSkillString(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival);
        String savingthrows = buildSavingThrowString(stStr, stDex, stCon, stInt, stWis, stCha);

        dest.add(new ReviewItem("Saving Throws", savingthrows, getKey(), -1));
        dest.add(new ReviewItem("Skills", skills, getKey(), -1));
        dest.add(new ReviewItem("Damage Vulnerabilities", mData.getString(SkillsPage.DMGVUL_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Damage Resistances", mData.getString(SkillsPage.DMGRES_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Damage Immunities", mData.getString(SkillsPage.DMGIM_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Condition Immunities", mData.getString(SkillsPage.CONIM_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Senses", mData.getString(SkillsPage.SENSES_DATA_KEY), getKey(), -1));


    }

    @Override
    public String getAvatarUri() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    private String buildSkillString(String acrobatics, String animalHandling, String arcana, String athletics, String deception, String history, String insight, String intimidation, String investigation, String medicine, String nature, String perception, String performance, String persuasion, String religion, String sleightofHand, String stealth, String survival) {
        String skills ="";
        if (acrobatics != null && !acrobatics.isEmpty()) {
            if(Integer.valueOf(acrobatics) >= 0){
                skills = "Acrobatics +" + acrobatics;
            }else{
                skills = "Acrobatics " + acrobatics;
            }
        }
        if (animalHandling != null && !animalHandling.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Animal Handling ";
            } else {
                skills = "Animal Handling ";
            }
            if (Integer.valueOf(animalHandling) >= 0) {
                skills = skills + "+" + animalHandling;
            } else{
                skills = skills + animalHandling;
            }
        }
        if (arcana != null && !arcana.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Arcana ";
            } else {
                skills = "Arcana ";
            }
            if (Integer.valueOf(arcana) >= 0) {
                skills = skills + "+" + arcana;
            } else{
                skills = skills + arcana;
            }
        }
        if (athletics != null && !athletics.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Athletics ";
            } else {
                skills = "Athletics ";
            }
            if (Integer.valueOf(athletics) >= 0) {
                skills = skills + "+" + athletics;
            } else{
                skills = skills + athletics;
            }
        }
        if (deception != null && !deception.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Deception ";
            } else {
                skills = "Deception ";
            }
            if (Integer.valueOf(deception) >= 0) {
                skills = skills + "+" + deception;
            } else{
                skills = skills + deception;
            }
        }
        if (history != null && !history.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", History ";
            } else {
                skills = "History ";
            }
            if (Integer.valueOf(history) >= 0) {
                skills = skills + "+" + history;
            } else{
                skills = skills + history;
            }
        }
        if (insight != null && !insight.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Insight ";
            } else {
                skills = "Insight ";
            }
            if (Integer.valueOf(insight) >= 0) {
                skills = skills + "+" + insight;
            } else{
                skills = skills + insight;
            }
        }
        if (intimidation != null && !intimidation.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Intimidation ";
            } else {
                skills = "Intimidation ";
            }
            if (Integer.valueOf(intimidation) >= 0) {
                skills = skills + "+" + intimidation;
            } else{
                skills = skills + intimidation;
            }
        }
        if (investigation != null && !investigation.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Investigation ";
            } else {
                skills = "Investigation ";
            }
            if (Integer.valueOf(investigation) >= 0) {
                skills = skills + "+" + investigation;
            } else{
                skills = skills + investigation;
            }
        }
        if (medicine != null && !medicine.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Medicine ";
            } else {
                skills = "Medicine ";
            }
            if (Integer.valueOf(medicine) >= 0) {
                skills = skills + "+" + medicine;
            } else{
                skills = skills + medicine;
            }
        }
        if (nature != null && !nature.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Nature ";
            } else {
                skills = "Nature ";
            }
            if (Integer.valueOf(nature) >= 0) {
                skills = skills + "+" + nature;
            } else{
                skills = skills + nature;
            }
        }
        if (perception != null && !perception.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Perception ";
            } else {
                skills = "Perception ";
            }
            if (Integer.valueOf(perception) >= 0) {
                skills = skills + "+" + perception;
            } else{
                skills = skills + perception;
            }
        }
        if (performance != null && !performance.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Performance ";
            } else {
                skills = "Performance ";
            }
            if (Integer.valueOf(performance) >= 0) {
                skills = skills + "+" + performance;
            } else{
                skills = skills + performance;
            }
        }
        if (persuasion != null && !persuasion.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Persuasion ";
            } else {
                skills = "Persuasion ";
            }
            if (Integer.valueOf(persuasion) >= 0) {
                skills = skills + "+" + persuasion;
            } else{
                skills = skills + persuasion;
            }
        }
        if (religion != null && !religion.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Religion ";
            } else {
                skills = "Religion ";
            }
            if (Integer.valueOf(religion) >= 0) {
                skills = skills + "+" + religion;
            } else{
                skills = skills + religion;
            }
        }
        if (sleightofHand != null && !sleightofHand.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Sleight of Hand ";
            } else {
                skills = "Sleight of Hand ";
            }
            if (Integer.valueOf(sleightofHand) >= 0) {
                skills = skills + "+" + sleightofHand;
            } else{
                skills = skills + sleightofHand;
            }
        }
        if (stealth != null && !stealth.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Stealth ";
            } else {
                skills = "Stealth ";
            }
            if (Integer.valueOf(stealth) >= 0) {
                skills = skills + "+" + stealth;
            } else{
                skills = skills + stealth;
            }
        }
        if (survival != null && !survival.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Survival ";
            } else {
                skills = "Survival ";
            }
            if (Integer.valueOf(survival) >= 0) {
                skills = skills + "+" + survival;
            } else{
                skills = skills + survival;
            }
        }
        return skills;
    }


    private String buildSavingThrowString(String stStr, String stDex, String stCon, String stInt, String stWis, String stCha){
        String savingThrows = "";
        if (stStr != null && !stStr.isEmpty()) {
            savingThrows = "Str ";
            if (Integer.valueOf(stStr) >= 0) {
                savingThrows = savingThrows + "+" + stStr;
            } else{
                savingThrows = savingThrows + stStr;
            }
        }
        if (stDex != null && !stDex.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ", Dex ";
            } else {
                savingThrows = "Dex ";
            }
            if (Integer.valueOf(stDex) >= 0) {
                savingThrows = savingThrows + "+" + stDex;
            } else{
                savingThrows = savingThrows + stDex;
            }
        }
        if (stCon != null && !stCon.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ", Con ";
            } else {
                savingThrows = "Con ";
            }
            if (Integer.valueOf(stCon) >= 0) {
                savingThrows = savingThrows + "+" + stCon;
            } else{
                savingThrows = savingThrows + stCon;
            }
        }
        if (stInt != null && !stInt.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ", Int ";
            } else {
                savingThrows = "Int ";
            }
            if (Integer.valueOf(stInt) >= 0) {
                savingThrows = savingThrows + "+" + stInt;
            } else{
                savingThrows = savingThrows + stInt;
            }
        }
        if (stWis != null && !stWis.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ",  Wis ";
            } else {
                savingThrows = "Wis ";
            }
            if (Integer.valueOf(stWis) >= 0) {
                savingThrows = savingThrows + "+" + stWis;
            } else{
                savingThrows = savingThrows + stWis;
            }
        }
        if (stCha != null && !stCha.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ", Cha ";
            } else {
                savingThrows = "Cha ";
            }
            if (Integer.valueOf(stCha) >= 0) {
                savingThrows = savingThrows + "+" + stCha;
            } else{
                savingThrows = savingThrows + stCha;
            }
        }
        return savingThrows;
    }
}
