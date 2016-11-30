package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

/**
 * Created by Koni on 11.11.2016.
 */

public class SkillsFragment extends Fragment {
    private static final String ARG_KEY = "basicinfo";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private SkillsPage mPage;
    private TextView tvSkills, labelSkills;
    private int acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival;
    private int MSG_SHOW_DIALOG = 1000, MSG_FINISH_DIALOG = 1001;
    SkillsFragment fragment;


    private ImageButton bChooseImage;
    private Uri myFile;
    private static int RESULT_CHOOSE_IMG = 2;

    public static SkillsFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        SkillsFragment fragment = new SkillsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SkillsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (SkillsPage) mCallbacks.onGetPage(mKey);
        fragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_skills, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());
        acrobatics = mPage.getData().getInt(SkillsPage.ACROBATICS_DATA_KEY);
        animalhandling = mPage.getData().getInt(SkillsPage.ANIMALHANDLING_DATA_KEY);
        arcana = mPage.getData().getInt(SkillsPage.ARCANA_DATA_KEY);
        athletics = mPage.getData().getInt(SkillsPage.ATHLETICS_DATA_KEY);
        deception = mPage.getData().getInt(SkillsPage.DECEPTION_DATA_KEY);
        history = mPage.getData().getInt(SkillsPage.HISTORY_DATA_KEY);
        insight = mPage.getData().getInt(SkillsPage.INSIGHT_DATA_KEY);
        intimidation = mPage.getData().getInt(SkillsPage.INTIMIDATION_DATA_KEY);
        investigation = mPage.getData().getInt(SkillsPage.INVESTIGATION_DATA_KEY);
        medicine = mPage.getData().getInt(SkillsPage.MEDICINE_DATA_KEY);
        nature = mPage.getData().getInt(SkillsPage.NATURE_DATA_KEY);
        perception = mPage.getData().getInt(SkillsPage.PERCEPTION_DATA_KEY);
        performance = mPage.getData().getInt(SkillsPage.PERFORMANCE_DATA_KEY);
        persuasion = mPage.getData().getInt(SkillsPage.PERSUASION_DATA_KEY);
        religion = mPage.getData().getInt(SkillsPage.RELIGION_DATA_KEY);
        sleightofhand = mPage.getData().getInt(SkillsPage.SLEIGHTOFHAND_DATA_KEY);
        stealth = mPage.getData().getInt(SkillsPage.STEALTH_DATA_KEY);
        survival = mPage.getData().getInt(SkillsPage.SURVIVAL_DATA_KEY);

        String skills = buildSkillString(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival);
        tvSkills = ((TextView) rootView.findViewById(R.id.tvSkills));
        if(!skills.isEmpty()){
            tvSkills.setText(skills);
        }
        tvSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSkillDialogFragment.newInstance(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival);
                newFragment.setTargetFragment(fragment, MSG_FINISH_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_FINISH_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDTRAIT_DIALOG");
            }
        });

        labelSkills = ((TextView) rootView.findViewById(R.id.labelSkills));
        labelSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSkillDialogFragment.newInstance(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival);
                newFragment.setTargetFragment(fragment, MSG_FINISH_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_FINISH_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDTRAIT_DIALOG");
            }
        });


        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (tvSkills != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    public void onDialogResult(int requestCode, int acrobatics, int animalHandling, int arcana, int athletics, int deception, int history, int insight, int intimidation, int investigation, int medicine, int nature, int perception, int performance, int persuasion, int religion, int sleightofHand, int stealth, int survival) {
        tvSkills.setText(buildSkillString(acrobatics, animalHandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofHand, stealth, survival));
        this.acrobatics = acrobatics;
        this.animalhandling = animalHandling;
        this.arcana = arcana;
        this.athletics = athletics;
        this.deception = deception;
        this.history = history;
        this.insight = insight;
        this.intimidation = intimidation;
        this.investigation = investigation;
        this.medicine = medicine;
        this.nature = nature;
        this.perception = perception;
        this.performance = performance;
        this.persuasion = persuasion;
        this.religion = religion;
        this.sleightofhand = sleightofHand;
        this.stealth = stealth;
        this.survival = survival;

        mPage.getData().putInt(SkillsPage.ACROBATICS_DATA_KEY,
                acrobatics);
        mPage.getData().putInt(SkillsPage.ANIMALHANDLING_DATA_KEY,
                animalHandling);
        mPage.getData().putInt(SkillsPage.ARCANA_DATA_KEY,
                arcana);
        mPage.getData().putInt(SkillsPage.ATHLETICS_DATA_KEY,
                athletics);
        mPage.getData().putInt(SkillsPage.DECEPTION_DATA_KEY,
                deception);
        mPage.getData().putInt(SkillsPage.HISTORY_DATA_KEY,
                history);
        mPage.getData().putInt(SkillsPage.INSIGHT_DATA_KEY,
                insight);
        mPage.getData().putInt(SkillsPage.INTIMIDATION_DATA_KEY,
                intimidation);
        mPage.getData().putInt(SkillsPage.INVESTIGATION_DATA_KEY,
                investigation);
        mPage.getData().putInt(SkillsPage.MEDICINE_DATA_KEY,
                medicine);
        mPage.getData().putInt(SkillsPage.NATURE_DATA_KEY,
                nature);
        mPage.getData().putInt(SkillsPage.PERCEPTION_DATA_KEY,
                perception);
        mPage.getData().putInt(SkillsPage.PERFORMANCE_DATA_KEY,
                performance);
        mPage.getData().putInt(SkillsPage.PERSUASION_DATA_KEY,
                persuasion);
        mPage.getData().putInt(SkillsPage.RELIGION_DATA_KEY,
                religion);
        mPage.getData().putInt(SkillsPage.SLEIGHTOFHAND_DATA_KEY,
                sleightofHand);
        mPage.getData().putInt(SkillsPage.STEALTH_DATA_KEY,
                stealth);
        mPage.getData().putInt(SkillsPage.SURVIVAL_DATA_KEY,
                survival);
    }

    private String buildSkillString(int acrobatics, int animalHandling, int arcana, int athletics, int deception, int history, int insight, int intimidation, int investigation, int medicine, int nature, int perception, int performance, int persuasion, int religion, int sleightofHand, int stealth, int survival) {
        String skills ="";
        if(acrobatics != 0){
            skills = "Acrobatics ";
            if(acrobatics>0){
                skills = skills + "+" + acrobatics;
            }else if(acrobatics<0){
                skills = skills + acrobatics;
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
                skills = skills + animalHandling;
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
                skills = skills + arcana;
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
                skills = skills + athletics;
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
                skills = skills + deception;
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
                skills = skills + history;
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
                skills = skills + insight;
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
                skills = skills + intimidation;
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
                skills = skills + investigation;
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
                skills = skills + medicine;
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
                skills = skills + nature;
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
                skills = skills + perception;
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
                skills = skills + performance;
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
                skills = skills + persuasion;
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
                skills = skills + religion;
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
                skills = skills + sleightofHand;
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
                skills = skills + stealth;
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
                skills = skills + survival;
            }
        }
        return skills;
    }
}
