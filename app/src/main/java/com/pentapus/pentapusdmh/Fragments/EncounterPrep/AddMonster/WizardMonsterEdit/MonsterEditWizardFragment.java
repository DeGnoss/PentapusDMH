package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.MyMonsterEditFragment;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.WizardFragment;
import com.wizardpager.wizard.model.AbstractWizardModel;
import com.wizardpager.wizard.model.Page;
import com.wizardpager.wizard.model.SingleFixedChoicePage;
import com.wizardpager.wizard.ui.StepPagerStrip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Koni on 11.11.2016.
 */

public class MonsterEditWizardFragment extends WizardFragment {
    private AbstractWizardModel mWizardModel = new MonsterEditWizardModel(getActivity());
    private static final String MODE = "modeUpdate";
    private static final String MONSTER_ID = "monsterId";
    private static final String ENCOUNTER_ID = "encounterId";
    private static final String NAV_MODE = "navMode";
    private Uri myFile;
    private boolean modeUpdate;
    private int monsterId;
    private int encounterId;
    private boolean navMode;
    private String mName, mType, mAlignment;
    private Bundle bundle;


    public static MonsterEditWizardFragment newInstance(boolean modeUpdate, int monsterId, int encounterId) {
        MonsterEditWizardFragment fragment = new MonsterEditWizardFragment();
        Bundle args = new Bundle();
        args.putBoolean(MODE, modeUpdate);
        args.putInt(MONSTER_ID, monsterId);
        args.putInt(ENCOUNTER_ID, encounterId);
        fragment.setArguments(args);
        return fragment;
    }


    //Set layout of Pager
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            modeUpdate = getArguments().getBoolean(MODE);
            encounterId = getArguments().getInt(ENCOUNTER_ID);
            navMode = getArguments().getBoolean(NAV_MODE);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.wizard, container, false);

        ViewPager mPager = (ViewPager) myFragmentView.findViewById(R.id.pager);
        StepPagerStrip mStepPagerStrip = (StepPagerStrip) myFragmentView.findViewById(R.id.strip);
        Button mNextButton = (Button) myFragmentView.findViewById(R.id.next_button);
        Button mPrevButton = (Button) myFragmentView.findViewById(R.id.prev_button);
        setControls(mPager, mStepPagerStrip, mNextButton, mPrevButton);
        //check whether entry gets updated or added
        if (modeUpdate) {
            bundle = new Bundle();
            monsterId = getArguments().getInt(MONSTER_ID);
            //... and load if necessary
            loadMonsterInfo(monsterId);
            mWizardModel.load(bundle);
        }

        return myFragmentView;
    }


    //Create Wizard
    @Override
    public AbstractWizardModel onCreateModel() {
        return mWizardModel;
    }

    //Method that runs after wizard is finished
    @Override
    public void onSubmit() {
        /*String type = mWizardModel.findByKey("Order type").getData().getString(Page.SIMPLE_DATA_KEY);

        if(type.equals("Sandwich")){
            String bread = mWizardModel.findByKey("Sandwich:Bread").getData().getString(Page.SIMPLE_DATA_KEY);
            String meats = "None";
            String veggies = "None";
            String cheeses = "None";
            String toasted = mWizardModel.findByKey("Sandwich:Toasted?").getData().getString(Page.SIMPLE_DATA_KEY);

            if(mWizardModel.findByKey("Sandwich:Meats").getData().get(Page.SIMPLE_DATA_KEY)!=null){
                meats = mWizardModel.findByKey("Sandwich:Meats").getData().get(Page.SIMPLE_DATA_KEY).toString();
            }
            if(mWizardModel.findByKey("Sandwich:Veggies").getData().get(Page.SIMPLE_DATA_KEY)!=null){
                veggies = mWizardModel.findByKey("Sandwich:Veggies").getData().get(Page.SIMPLE_DATA_KEY).toString();
            }
            if(mWizardModel.findByKey("Sandwich:Cheeses").getData().get(Page.SIMPLE_DATA_KEY).toString()!=null){
                cheeses = mWizardModel.findByKey("Sandwich:Cheeses").getData().get(Page.SIMPLE_DATA_KEY).toString();
            }

            Toast.makeText(getActivity(), "Ordering Your "+type+ "\n" + bread + ", " + meats + ", " + veggies + ", " + cheeses + ", " + toasted, Toast.LENGTH_LONG).show();
        }

        else if(type.equals("Salad")){
            String salad = mWizardModel.findByKey("Salad:Salad type").getData().getString(Page.SIMPLE_DATA_KEY);
            String dressing = mWizardModel.findByKey("Salad:Dressing").getData().getString(Page.SIMPLE_DATA_KEY);
            Toast.makeText(getActivity(), "Ordering Your "+type+ "\n" + salad + ", " + dressing, Toast.LENGTH_LONG).show();
        }*/

        String name = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.NAME_DATA_KEY);
        String type = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.TYPE_DATA_KEY);
        String alignment = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.ALIGNMENT_DATA_KEY);
        String speed = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.SPEED_DATA_KEY);
        String imageUri = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.IMAGEURI_DATA_KEY);

        String size = mWizardModel.findByKey("Size").getData().getString(SingleFixedChoicePage.SIMPLE_DATA_KEY);

        String str = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.STR_DATA_KEY);
        String dex = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.DEX_DATA_KEY);
        String con = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.CON_DATA_KEY);
        String intel = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.INT_DATA_KEY);
        String wis = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.WIS_DATA_KEY);
        String cha = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.CHA_DATA_KEY);
        String hp = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.HP_DATA_KEY);
        String hpdice = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.HITDICE_DATA_KEY);
        String ac = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.AC_DATA_KEY);
        String acType = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.ACTYPE_DATA_KEY);
        String stStr = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.STSTR_DATA_KEY);
        String stDex = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.STDEX_DATA_KEY);
        String stCon = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.STCON_DATA_KEY);
        String stInt = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.STINT_DATA_KEY);
        String stWis = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.STWIS_DATA_KEY);
        String stCha = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.STCHA_DATA_KEY);

        String acrobatics = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.ACROBATICS_DATA_KEY);
        String animalHandling = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.ANIMALHANDLING_DATA_KEY);
        String arcana = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.ARCANA_DATA_KEY);
        String athletics = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.ATHLETICS_DATA_KEY);
        String deception = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.DECEPTION_DATA_KEY);
        String history = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.HISTORY_DATA_KEY);
        String insight = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.INSIGHT_DATA_KEY);
        String intimidation = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.INTIMIDATION_DATA_KEY);
        String investigation = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.INVESTIGATION_DATA_KEY);
        String medicine = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.MEDICINE_DATA_KEY);
        String nature = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.NATURE_DATA_KEY);
        String perception = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.PERCEPTION_DATA_KEY);
        String performance = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.PERFORMANCE_DATA_KEY);
        String persuasion = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.PERSUASION_DATA_KEY);
        String religion = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.RELIGION_DATA_KEY);
        String sleightOfHand = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.SLEIGHTOFHAND_DATA_KEY);
        String stealth = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.STEALTH_DATA_KEY);
        String survival = mWizardModel.findByKey("Skills").getData().getString(SkillsPage.SURVIVAL_DATA_KEY);



        String dmgVul = buildString(mWizardModel.findByKey("Damage Vulnerabilities").getData().getStringArrayList(DmgVulChoicePage.SIMPLE_DATA_KEY));
        String dmgRes = buildString(mWizardModel.findByKey("Damage Resistances").getData().getStringArrayList(DmgVulChoicePage.SIMPLE_DATA_KEY));
        String dmgIm = buildString(mWizardModel.findByKey("Damage Immunities").getData().getStringArrayList(DmgVulChoicePage.SIMPLE_DATA_KEY));
        String conIm = buildString(mWizardModel.findByKey("Condition Immunities").getData().getStringArrayList(DmgVulChoicePage.SIMPLE_DATA_KEY));


        String t1Name = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T1NAME_DATA_KEY);
        String t1Desc = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T1DESC_DATA_KEY);
        String t2Name = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T2NAME_DATA_KEY);
        String t2Desc = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T2DESC_DATA_KEY);
        String t3Name = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T3NAME_DATA_KEY);
        String t3Desc = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T3DESC_DATA_KEY);
        String t4Name = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T4NAME_DATA_KEY);
        String t4Desc = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T4DESC_DATA_KEY);
        String t5Name = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T5NAME_DATA_KEY);
        String t5Desc = mWizardModel.findByKey("Traits").getData().getString(TraitsPage.T5DESC_DATA_KEY);

        String a1name = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1NAME_DATA_KEY);
        String a1desc = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1DESC_DATA_KEY);
        String a1mod = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1MOD_DATA_KEY);
        String a1roll1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1ROLL1_DATA_KEY);
        String a1roll2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1ROLL2_DATA_KEY);
        String a1type1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1TYPE1_DATA_KEY);
        String a1type2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1TYPE2_DATA_KEY);
        String a1auto = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1AUTO_DATA_KEY);
        String a1add = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1ADD_DATA_KEY);






        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, name);
        values.put(DataBaseHandler.KEY_MONSTERTYPE, type);
        values.put(DataBaseHandler.KEY_ALIGNMENT, alignment);
        values.put(DataBaseHandler.KEY_SPEED, speed);
        values.put(DataBaseHandler.KEY_SIZE, size);
        values.put(DataBaseHandler.KEY_STRENGTH, str);
        values.put(DataBaseHandler.KEY_DEXTERITY, dex);
        values.put(DataBaseHandler.KEY_CONSTITUTION, con);
        values.put(DataBaseHandler.KEY_INTELLIGENCE, intel);
        values.put(DataBaseHandler.KEY_WISDOM, wis);
        values.put(DataBaseHandler.KEY_CHARISMA, cha);
        values.put(DataBaseHandler.KEY_MAXHP, hp);
        values.put(DataBaseHandler.KEY_HPROLL, hpdice);
        values.put(DataBaseHandler.KEY_AC, ac);
        values.put(DataBaseHandler.KEY_ACTYPE, acType);
        values.put(DataBaseHandler.KEY_STSTR, stStr);
        values.put(DataBaseHandler.KEY_STDEX, stDex);
        values.put(DataBaseHandler.KEY_STCON, stCon);
        values.put(DataBaseHandler.KEY_STINT, stInt);
        values.put(DataBaseHandler.KEY_STWIS, stWis);
        values.put(DataBaseHandler.KEY_STCHA, stCha);
        values.put(DataBaseHandler.KEY_ACROBATICS, acrobatics);
        values.put(DataBaseHandler.KEY_ANIMALHANDLING, animalHandling);
        values.put(DataBaseHandler.KEY_ARCANA, arcana);
        values.put(DataBaseHandler.KEY_ATHLETICS, athletics);
        values.put(DataBaseHandler.KEY_DECEPTION, deception);
        values.put(DataBaseHandler.KEY_HISTORY, history);
        values.put(DataBaseHandler.KEY_INSIGHT, insight);
        values.put(DataBaseHandler.KEY_INTIMIDATION, intimidation);
        values.put(DataBaseHandler.KEY_INVESTIGATION, investigation);
        values.put(DataBaseHandler.KEY_MEDICINE, medicine);
        values.put(DataBaseHandler.KEY_NATURE, nature);
        values.put(DataBaseHandler.KEY_PERCEPTION, perception);
        values.put(DataBaseHandler.KEY_PERFORMANCE, performance);
        values.put(DataBaseHandler.KEY_PERSUASION, persuasion);
        values.put(DataBaseHandler.KEY_RELIGION, religion);
        values.put(DataBaseHandler.KEY_SLEIGHTOFHAND, sleightOfHand);
        values.put(DataBaseHandler.KEY_STEALTH, stealth);
        values.put(DataBaseHandler.KEY_SURVIVAL, survival);
        values.put(DataBaseHandler.KEY_DMGVUL, dmgVul);
        values.put(DataBaseHandler.KEY_DMGRES, dmgRes);
        values.put(DataBaseHandler.KEY_DMGIM, dmgIm);
        values.put(DataBaseHandler.KEY_CONIM, conIm);
        values.put(DataBaseHandler.KEY_ABILITY1NAME, t1Name);
        values.put(DataBaseHandler.KEY_ABILITY1DESC, t1Desc);
        values.put(DataBaseHandler.KEY_ABILITY2NAME, t2Name);
        values.put(DataBaseHandler.KEY_ABILITY2DESC, t2Desc);
        values.put(DataBaseHandler.KEY_ABILITY3NAME, t3Name);
        values.put(DataBaseHandler.KEY_ABILITY3DESC, t3Desc);
        values.put(DataBaseHandler.KEY_ABILITY4NAME, t4Name);
        values.put(DataBaseHandler.KEY_ABILITY4DESC, t4Desc);
        values.put(DataBaseHandler.KEY_ABILITY5NAME, t5Name);
        values.put(DataBaseHandler.KEY_ABILITY5DESC, t5Desc);

        values.put(DataBaseHandler.KEY_ATK1NAME, a1name);
        values.put(DataBaseHandler.KEY_ATK1DESC, a1desc);
        values.put(DataBaseHandler.KEY_ATK1MOD, a1mod);
        values.put(DataBaseHandler.KEY_ATK1DMG1ROLL, a1roll1);
        values.put(DataBaseHandler.KEY_ATK1DMG2ROLL, a1roll2);
        values.put(DataBaseHandler.KEY_ATK1DMG1TYPE, a1type1);
        values.put(DataBaseHandler.KEY_ATK1DMG2TYPE, a1type2);
        values.put(DataBaseHandler.KEY_ATK1AUTOROLL, a1auto);
        values.put(DataBaseHandler.KEY_ATK1ADDITIONAL, a1add);



        if(imageUri == null){
            imageUri = "android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight";
        }
        values.put(DataBaseHandler.KEY_ICON, imageUri);
        values.put(DataBaseHandler.KEY_SOURCE, "USER");
        values.put(DataBaseHandler.KEY_TYPE, DataBaseHandler.TYPE_MONSTER);

        // insert a record
        if (!modeUpdate) {
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_MONSTER, values);
        }
        // update a record
        else {
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + monsterId);
            getContext().getContentResolver().update(uri, values, null, null);
        }

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        getActivity().getSupportFragmentManager().popBackStack();

    }



    private void loadMonsterInfo(int id) {

        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_MONSTER_TEMPLATE, null, null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            //Basic Info
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MONSTERTYPE));
            String alignment = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ALIGNMENT));
            String speed = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SPEED));
            String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON));
            String size = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SIZE));

            String str = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH));
            String dex = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY));
            String con = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION));
            String intel = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE));
            String wis = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM));
            String cha = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA));
            String hp = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
            String hpdice = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_HPROLL));

            String ac = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
            String acType = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ACTYPE));

            String stStr = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STSTR));
            String stDex = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STDEX));
            String stCon = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STCON));
            String stInt = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STINT));
            String stWis = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STWIS));
            String stCha = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STCHA));

            String acrobatics = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ACROBATICS));
            String animalhandling = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ANIMALHANDLING));
            String arcana = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ARCANA));
            String athletics = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATHLETICS));
            String deception = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DECEPTION));
            String history = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_HISTORY));
            String insight = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INSIGHT));
            String intimidation = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTIMIDATION));
            String investigation = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INVESTIGATION));
            String medicine = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MEDICINE));
            String nature = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NATURE));
            String perception = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_PERCEPTION));
            String performance = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_PERFORMANCE));
            String persuasion = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_PERSUASION));
            String religion = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_RELIGION));
            String sleightofhand = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SLEIGHTOFHAND));
            String stealth = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STEALTH));
            String survival = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SURVIVAL));

            String dmgVul = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGVUL));
            String dmgRes = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGRES));
            String dmgIm = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGIM));
            String conIm = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONIM));

            String t1Name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1NAME));
            String t1Desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1DESC));
            String t2Name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY2NAME));
            String t2Desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY2DESC));
            String t3Name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY3NAME));
            String t3Desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY3DESC));
            String t4Name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY4NAME));
            String t4Desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY4DESC));
            String t5Name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY5NAME));
            String t5Desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY5DESC));

            String a1name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1NAME));
            String a1desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DESC));
            String a1mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1MOD));
            String a1roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG1ROLL));
            String a1roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG2ROLL));
            String a1type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG1TYPE));
            String a1type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG2TYPE));
            String a1auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1AUTOROLL));
            String a1add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1ADDITIONAL));


            createBundleBasicInfo(name, type, alignment, speed, imageUri);
            createBundleSize(size);
            createBundleAbilities(str, dex, con, intel, wis, cha, hp, hpdice, ac, acType, stStr, stDex, stCon, stInt, stWis, stCha);
            createBundleSkills(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival);
            createBundleDmgVul(dmgVul);
            createBundleDmgRes(dmgRes);
            createBundleDmgIm(dmgIm);
            createBundleConIm(conIm);
            createBundleTraits(t1Name, t1Desc, t2Name, t2Desc, t3Name, t3Desc, t4Name, t4Desc, t5Name, t5Desc);
            createBundle(a1name, a1desc, a1mod, a1roll1, a1roll2, a1type1, a1type2, a1auto, a1add);
        }
        assert cursor != null;
        cursor.close();

    }

    private void createBundleBasicInfo(String name, String type, String alignment, String speed, String imageUri){
        Bundle bdl1 = new Bundle();
        bdl1.putString("name",name);
        bdl1.putString("type",type);
        bdl1.putString("alignment",alignment);
        bdl1.putString("speed",speed);
        bdl1.putString("imageuri",imageUri);
        bundle.putBundle("Basic Info",bdl1);
    }



    private void createBundleSize(String size){
        Bundle bdl1 = new Bundle();
        bdl1.putString("Size",size);
        bundle.putBundle("Size",bdl1);
    }

    private void createBundleAbilities(String str, String dex, String con, String intel, String wis, String cha, String hp, String hpdice, String ac, String acType, String stStr, String stDex, String stCon, String stInt, String stWis, String stCha){
        Bundle bdl1 = new Bundle();
        bdl1.putString("str",str);
        bdl1.putString("dex",dex);
        bdl1.putString("con",con);
        bdl1.putString("int",intel);
        bdl1.putString("wis",wis);
        bdl1.putString("cha",cha);
        bdl1.putString("hp",hp);
        bdl1.putString("hitdice",hpdice);
        bdl1.putString("ac",ac);
        bdl1.putString("actype",acType);
        bdl1.putString("ststr", stStr);
        bdl1.putString("stdex",stDex);
        bdl1.putString("stcon",stCon);
        bdl1.putString("stint",stInt);
        bdl1.putString("stwis",stWis);
        bdl1.putString("stcha",stCha);
        bundle.putBundle("Abilities & Hit Points",bdl1);
    }

    private void createBundleSkills(String acrobatics, String animalhandling, String arcana, String athletics, String deception, String history, String insight, String intimidation, String investigation, String medicine, String nature, String perception, String performance, String persuasion, String religion, String sleightofhand, String stealth, String survival){
        Bundle bdl1 = new Bundle();
        bdl1.putString("acrobatics",acrobatics);
        bdl1.putString("animalhandling",animalhandling);
        bdl1.putString("arcana",arcana);
        bdl1.putString("athletics",athletics);
        bdl1.putString("deception",deception);
        bdl1.putString("history",history);
        bdl1.putString("insight",insight);
        bdl1.putString("intimidation",intimidation);
        bdl1.putString("investigation",investigation);
        bdl1.putString("medicine",medicine);
        bdl1.putString("nature",nature);
        bdl1.putString("perception",perception);
        bdl1.putString("performance",performance);
        bdl1.putString("persuasion",persuasion);
        bdl1.putString("religion",religion);
        bdl1.putString("sleightofhand",sleightofhand);
        bdl1.putString("stealth",stealth);
        bdl1.putString("survival",survival);
        bundle.putBundle("Skills",bdl1);
    }

    private void createBundleDmgVul(String dmgVul){
        Bundle bdl1 = new Bundle();
        if(dmgVul != null) {
            bdl1.putStringArrayList("Damage Vulnerabilities", reconstructArrayList(dmgVul));
        }
        bundle.putBundle("Damage Vulnerabilities",bdl1);
    }

    private void createBundleDmgRes(String dmgRes){
        Bundle bdl1 = new Bundle();
        if(dmgRes != null){
            bdl1.putStringArrayList("Damage Resistances",reconstructArrayList(dmgRes));
        }
        bundle.putBundle("Damage Resistances",bdl1);
    }

    private void createBundleDmgIm(String dmgIm){
        Bundle bdl1 = new Bundle();
        if(dmgIm != null){
            bdl1.putStringArrayList("Damage Immunities",reconstructArrayList(dmgIm));
        }
        bundle.putBundle("Damage Immunities",bdl1);
    }

    private void createBundleConIm(String conIm){
        Bundle bdl1 = new Bundle();
        if(conIm != null){
            bdl1.putStringArrayList("Condition Immunities",reconstructArrayList(conIm));
        }
        bundle.putBundle("Condition Immunities",bdl1);
    }

    private void createBundleTraits(String t1Name, String t1Desc, String t2Name, String t2Desc, String t3Name, String t3Desc, String t4Name, String t4Desc, String t5Name, String t5Desc){
        Bundle bdl1 = new Bundle();
        bdl1.putString("t1name",t1Name);
        bdl1.putString("t1desc",t1Desc);
        bdl1.putString("t2name",t2Name);
        bdl1.putString("t2desc",t2Desc);
        bdl1.putString("t3name",t3Name);
        bdl1.putString("t3desc",t3Desc);
        bdl1.putString("t4name",t4Name);
        bdl1.putString("t4desc",t4Desc);
        bdl1.putString("t5name",t5Name);
        bdl1.putString("t5desc",t5Desc);
        bundle.putBundle("Traits",bdl1);
    }

    private void createBundle(String a1name, String a1desc, String a1mod, String a1roll1, String a1roll2, String a1type1, String a1type2, String a1auto, String a1add){
        Bundle bdl1 = new Bundle();
        bdl1.putString("a1name",a1name);
        bdl1.putString("a1desc",a1desc);
        bdl1.putString("a1mod",a1mod);
        bdl1.putString("a1roll1",a1roll1);
        bdl1.putString("a1roll2",a1roll2);
        bdl1.putString("a1type1",a1type1);
        bdl1.putString("a1type2",a1type2);
        bdl1.putString("a1auto",a1auto);
        bdl1.putString("a1add",a1add);
        bundle.putBundle("Actions",bdl1);
    }


    private String buildString(ArrayList<String> stringArray){
        StringBuilder builder = new StringBuilder();
        if(stringArray == null){
            return null;
        }
        for(String s : stringArray) {
            if (builder.length() > 0) {
                builder.append("; ");
            }
            builder.append(s);
        }
        return builder.toString();
    }

    private ArrayList<String> reconstructArrayList(String string){
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(string.split("; ")));
        return myList;
    }


}
