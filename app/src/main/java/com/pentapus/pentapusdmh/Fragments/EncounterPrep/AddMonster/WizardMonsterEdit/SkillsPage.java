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






    public SkillsPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return SkillsFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        int acrobatics = mData.getInt(SkillsPage.ACROBATICS_DATA_KEY);
        int animalhandling = mData.getInt(SkillsPage.ANIMALHANDLING_DATA_KEY);
        int arcana = mData.getInt(SkillsPage.ARCANA_DATA_KEY);
        int  athletics = mData.getInt(SkillsPage.ATHLETICS_DATA_KEY);
        int deception = mData.getInt(SkillsPage.DECEPTION_DATA_KEY);
        int history = mData.getInt(SkillsPage.HISTORY_DATA_KEY);
        int insight = mData.getInt(SkillsPage.INSIGHT_DATA_KEY);
        int intimidation = mData.getInt(SkillsPage.INTIMIDATION_DATA_KEY);
        int investigation = mData.getInt(SkillsPage.INVESTIGATION_DATA_KEY);
        int medicine = mData.getInt(SkillsPage.MEDICINE_DATA_KEY);
        int nature = mData.getInt(SkillsPage.NATURE_DATA_KEY);
        int perception = mData.getInt(SkillsPage.PERCEPTION_DATA_KEY);
        int performance = mData.getInt(SkillsPage.PERFORMANCE_DATA_KEY);
        int persuasion = mData.getInt(SkillsPage.PERSUASION_DATA_KEY);
        int religion = mData.getInt(SkillsPage.RELIGION_DATA_KEY);
        int sleightofhand = mData.getInt(SkillsPage.SLEIGHTOFHAND_DATA_KEY);
        int stealth = mData.getInt(SkillsPage.STEALTH_DATA_KEY);
        int survival = mData.getInt(SkillsPage.SURVIVAL_DATA_KEY);

        String skills = buildSkillString(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival);

        dest.add(new ReviewItem("Skills", skills, getKey(), -1));
        dest.add(new ReviewItem("Damage Vulnerabilities", mData.getString(SkillsPage.DMGVUL_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Damage Resistances", mData.getString(SkillsPage.DMGRES_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Damage Immunities", mData.getString(SkillsPage.DMGIM_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Condition Immunities", mData.getString(SkillsPage.CONIM_DATA_KEY), getKey(), -1));


    }

    @Override
    public String getAvatarUri() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    private String buildSkillString(int acrobatics, int animalHandling, int arcana, int athletics, int deception, int history, int insight, int intimidation, int investigation, int medicine, int nature, int perception, int performance, int persuasion, int religion, int sleightofHand, int stealth, int survival) {
        String skills ="";
        if(acrobatics != 0){
            skills = "Acrobatics ";
            if(acrobatics>0){
                skills = skills + "+" + acrobatics;
            }else if(acrobatics<0){
                skills = skills + "-" + acrobatics;
            }
        }
        if(animalHandling != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Animal Handling ";
            }else{
                skills = "Animal Handling ";
            }
            if(animalHandling>0){
                skills = skills + "+" + animalHandling;
            }else if(animalHandling<0){
                skills = skills + "-" + animalHandling;
            }
        }
        if(arcana != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Arcana ";
            }else{
                skills = "Arcana ";
            }
            if(arcana>0){
                skills = skills + "+" + arcana;
            }else if(arcana<0){
                skills = skills + "-" + arcana;
            }
        }
        if(athletics != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Athletics ";
            }else{
                skills = "Athletics ";
            }
            if(athletics>0){
                skills = skills + "+" + athletics;
            }else if(athletics<0){
                skills = skills + "-" + athletics;
            }
        }
        if(deception != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Deception ";
            }else{
                skills = "Deception ";
            }
            if(deception>0){
                skills = skills + "+" + deception;
            }else if(deception<0){
                skills = skills + "-" + deception;
            }
        }
        if(history != 0){
            if(!skills.isEmpty()){
                skills = skills + ", History ";
            }else{
                skills = "History ";
            }
            if(history>0){
                skills = skills + "+" + history;
            }else if(history<0){
                skills = skills + "-" + history;
            }
        }
        if(insight != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Insight ";
            }else{
                skills = "Insight ";
            }
            if(insight>0){
                skills = skills + "+" + insight;
            }else if(insight<0){
                skills = skills + "-" + insight;
            }
        }
        if(intimidation != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Intimidation ";
            }else{
                skills = "Intimidation ";
            }
            if(intimidation>0){
                skills = skills + "+" + intimidation;
            }else if(intimidation<0){
                skills = skills + "-" + intimidation;
            }
        }
        if(investigation != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Investigation ";
            }else{
                skills = "Investigation ";
            }
            if(investigation>0){
                skills = skills + "+" + investigation;
            }else if(investigation<0){
                skills = skills + "-" + investigation;
            }
        }
        if(medicine != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Medicine ";
            }else{
                skills = "Medicine ";
            }
            if(medicine>0){
                skills = skills + "+" + medicine;
            }else if(medicine<0){
                skills = skills + "-" + medicine;
            }
        }
        if(nature != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Nature ";
            }else{
                skills = "Nature ";
            }
            if(nature>0){
                skills = skills + "+" + nature;
            }else if(nature<0){
                skills = skills + "-" + nature;
            }
        }
        if(perception != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Perception ";
            }else{
                skills = "Perception ";
            }
            if(perception>0){
                skills = skills + "+" + perception;
            }else if(perception<0){
                skills = skills + "-" + perception;
            }
        }
        if(performance != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Performance ";
            }else{
                skills = "Performance ";
            }
            if(performance>0){
                skills = skills + "+" + performance;
            }else if(performance<0){
                skills = skills + "-" + performance;
            }
        }
        if(persuasion != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Persuasion ";
            }else{
                skills = "Persuasion ";
            }
            if(persuasion>0){
                skills = skills + "+" + persuasion;
            }else if(persuasion<0){
                skills = skills + "-" + persuasion;
            }
        }
        if(religion != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Religion ";
            }else{
                skills = "Religion ";
            }
            if(religion>0){
                skills = skills + "+" + religion;
            }else if(religion<0){
                skills = skills + "-" + religion;
            }
        }
        if(sleightofHand != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Sleight of Hand ";
            }else{
                skills = "Sleight of Hand ";
            }
            if(sleightofHand>0){
                skills = skills + "+" + sleightofHand;
            }else if(sleightofHand<0){
                skills = skills + "-" + sleightofHand;
            }
        }
        if(stealth != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Stealth ";
            }else{
                skills = "Stealth ";
            }
            if(stealth>0){
                skills = skills + "+" + stealth;
            }else if(stealth<0){
                skills = skills + "-" + stealth;
            }
        }
        if(survival != 0){
            if(!skills.isEmpty()){
                skills = skills + ", Survival ";
            }else{
                skills = "Survival ";
            }
            if(survival>0){
                skills = skills + "+" + survival;
            }else if(survival<0){
                skills = skills + "-" + survival;
            }
        }
        return skills;
    }
}
