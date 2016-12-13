package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.WizardFragment;
import com.wizardpager.wizard.model.AbstractWizardModel;
import com.wizardpager.wizard.ui.StepPagerStrip;

/**
 * Created by Koni on 11.11.2016.
 */

public class MonsterEditWizardFragment extends WizardFragment implements BasicInfoFragment.OnSizeChangedListener, BasicInfoFragment.OnNameChangedListener, AbilitiesFragment.OnWisdomChangedListener{
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
    private String name;
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
        String name = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.NAME_DATA_KEY);
        String type = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.TYPE_DATA_KEY);
        String alignment = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.ALIGNMENT_DATA_KEY);
        String imageUri = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.IMAGEURI_DATA_KEY);
        String size = mWizardModel.findByKey("Basic Info").getData().getString(BasicInfoPage.SIZE_DATA_KEY);

        String str = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.STR_DATA_KEY);
        String dex = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.DEX_DATA_KEY);
        String con = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.CON_DATA_KEY);
        String intel = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.INT_DATA_KEY);
        String wis = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.WIS_DATA_KEY);
        String cha = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.CHA_DATA_KEY);
        String hp = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.HP_DATA_KEY);
        String hpdice = String.valueOf(mWizardModel.findByKey("Abilities & Hit Points").getData().getInt(AbilitiesPage.HITDICE_DATA_KEY));
        String ac1 = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.AC1_DATA_KEY);
        String ac1Type = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.AC1TYPE_DATA_KEY);
        String ac2 = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.AC2_DATA_KEY);
        String ac2Type = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.AC2TYPE_DATA_KEY);
        String speed = mWizardModel.findByKey("Abilities & Hit Points").getData().getString(AbilitiesPage.SPEED_DATA_KEY);

        String acrobatics = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.ACROBATICS_DATA_KEY);
        String animalHandling = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.ANIMALHANDLING_DATA_KEY);
        String arcana = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.ARCANA_DATA_KEY);
        String athletics = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.ATHLETICS_DATA_KEY);
        String deception = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.DECEPTION_DATA_KEY);
        String history = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.HISTORY_DATA_KEY);
        String insight = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.INSIGHT_DATA_KEY);
        String intimidation = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.INTIMIDATION_DATA_KEY);
        String investigation = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.INVESTIGATION_DATA_KEY);
        String medicine = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.MEDICINE_DATA_KEY);
        String nature = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.NATURE_DATA_KEY);
        String perception = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.PERCEPTION_DATA_KEY);
        String performance = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.PERFORMANCE_DATA_KEY);
        String persuasion = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.PERSUASION_DATA_KEY);
        String religion = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.RELIGION_DATA_KEY);
        String sleightOfHand = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.SLEIGHTOFHAND_DATA_KEY);
        String stealth = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.STEALTH_DATA_KEY);
        String survival = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.SURVIVAL_DATA_KEY);
        String stStr = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.STSTR_DATA_KEY);
        String stDex = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.STDEX_DATA_KEY);
        String stCon = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.STCON_DATA_KEY);
        String stInt = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.STINT_DATA_KEY);
        String stWis = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.STWIS_DATA_KEY);
        String stCha = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.STCHA_DATA_KEY);


        String dmgVul = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.DMGVUL_DATA_KEY);
        String dmgRes = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.DMGRES_DATA_KEY);
        String dmgIm = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.DMGIM_DATA_KEY);
        String conIm = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.CONIM_DATA_KEY);
        String senses = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.SENSES_DATA_KEY);
        if (senses == null || !senses.contains("passive Perception")){
            if(wis!=null && !wis.isEmpty()){
                if(senses != null){
                    senses = senses + ", passive Perception " + (10 + AbilityModifierCalculator.calculateMod(Integer.valueOf(wis)));
                }else{
                    senses = "passive Perception " + (10 + AbilityModifierCalculator.calculateMod(Integer.valueOf(wis)));
                }
            }
        }
        String languages = mWizardModel.findByKey("Additional Info").getData().getString(SkillsPage.LANGUAGES_DATA_KEY);


        //String dmgVul = buildString(mWizardModel.findByKey("Damage Vulnerabilities").getData().getStringArrayList(DmgVulChoicePage.SIMPLE_DATA_KEY));
        //String dmgIm = buildString(mWizardModel.findByKey("Damage Immunities").getData().getStringArrayList(DmgVulChoicePage.SIMPLE_DATA_KEY));
       // String conIm = buildString(mWizardModel.findByKey("Condition Immunities").getData().getStringArrayList(DmgVulChoicePage.SIMPLE_DATA_KEY));


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

        String multiAttack = mWizardModel.findByKey("Actions").getData().getString(ActionPage.MULTIATTACK_DATA_KEY);

        String a1name = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1NAME_DATA_KEY);
        String a1desc = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1DESC_DATA_KEY);
        String a1mod = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1MOD_DATA_KEY);
        String a1roll1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1ROLL1_DATA_KEY);
        String a1roll2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1ROLL2_DATA_KEY);
        String a1type1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1TYPE1_DATA_KEY);
        String a1type2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1TYPE2_DATA_KEY);
        String a1auto = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1AUTO_DATA_KEY);
        String a1add = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A1ADD_DATA_KEY);

        String a2name = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A2NAME_DATA_KEY);
        String a2desc = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A2DESC_DATA_KEY);
        String a2mod = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A2MOD_DATA_KEY);
        String a2roll1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A2ROLL1_DATA_KEY);
        String a2roll2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A2ROLL2_DATA_KEY);
        String a2type1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A2TYPE1_DATA_KEY);
        String a2type2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A2TYPE2_DATA_KEY);
        String a2auto = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A2AUTO_DATA_KEY);
        String a2add = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A2ADD_DATA_KEY);

        String a3name = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A3NAME_DATA_KEY);
        String a3desc = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A3DESC_DATA_KEY);
        String a3mod = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A3MOD_DATA_KEY);
        String a3roll1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A3ROLL1_DATA_KEY);
        String a3roll2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A3ROLL2_DATA_KEY);
        String a3type1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A3TYPE1_DATA_KEY);
        String a3type2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A3TYPE2_DATA_KEY);
        String a3auto = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A3AUTO_DATA_KEY);
        String a3add = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A3ADD_DATA_KEY);

        String a4name = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A4NAME_DATA_KEY);
        String a4desc = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A4DESC_DATA_KEY);
        String a4mod = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A4MOD_DATA_KEY);
        String a4roll1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A4ROLL1_DATA_KEY);
        String a4roll2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A4ROLL2_DATA_KEY);
        String a4type1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A4TYPE1_DATA_KEY);
        String a4type2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A4TYPE2_DATA_KEY);
        String a4auto = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A4AUTO_DATA_KEY);
        String a4add = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A4ADD_DATA_KEY);

        /*
        String a5name = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A5NAME_DATA_KEY);
        String a5desc = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A5DESC_DATA_KEY);
        String a5mod = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A5MOD_DATA_KEY);
        String a5roll1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A5ROLL1_DATA_KEY);
        String a5roll2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A5ROLL2_DATA_KEY);
        String a5type1 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A5TYPE1_DATA_KEY);
        String a5type2 = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A5TYPE2_DATA_KEY);
        String a5auto = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A5AUTO_DATA_KEY);
        String a5add = mWizardModel.findByKey("Actions").getData().getString(ActionPage.A5ADD_DATA_KEY);
*/

        String r1name = mWizardModel.findByKey("Reactions").getData().getString(ReactionPage.R1NAME_DATA_KEY);
        String r1desc = mWizardModel.findByKey("Reactions").getData().getString(ReactionPage.R1DESC_DATA_KEY);


        String lmultiAttack = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LMULTIATTACK_DATA_KEY);

        String la1name = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA1NAME_DATA_KEY);
        String la1desc = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA1DESC_DATA_KEY);
        String la1mod = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA1MOD_DATA_KEY);
        String la1roll1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA1ROLL1_DATA_KEY);
        String la1roll2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA1ROLL2_DATA_KEY);
        String la1type1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA1TYPE1_DATA_KEY);
        String la1type2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA1TYPE2_DATA_KEY);
        String la1auto = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA1AUTO_DATA_KEY);
        String la1add = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA1ADD_DATA_KEY);

        String la2name = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA2NAME_DATA_KEY);
        String la2desc = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA2DESC_DATA_KEY);
        String la2mod = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA2MOD_DATA_KEY);
        String la2roll1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA2ROLL1_DATA_KEY);
        String la2roll2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA2ROLL2_DATA_KEY);
        String la2type1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA2TYPE1_DATA_KEY);
        String la2type2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA2TYPE2_DATA_KEY);
        String la2auto = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA2AUTO_DATA_KEY);
        String la2add = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA2ADD_DATA_KEY);

        String la3name = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA3NAME_DATA_KEY);
        String la3desc = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA3DESC_DATA_KEY);
        String la3mod = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA3MOD_DATA_KEY);
        String la3roll1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA3ROLL1_DATA_KEY);
        String la3roll2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA3ROLL2_DATA_KEY);
        String la3type1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA3TYPE1_DATA_KEY);
        String la3type2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA3TYPE2_DATA_KEY);
        String la3auto = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA3AUTO_DATA_KEY);
        String la3add = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA3ADD_DATA_KEY);

        String la4name = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA4NAME_DATA_KEY);
        String la4desc = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA4DESC_DATA_KEY);
        String la4mod = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA4MOD_DATA_KEY);
        String la4roll1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA4ROLL1_DATA_KEY);
        String la4roll2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA4ROLL2_DATA_KEY);
        String la4type1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA4TYPE1_DATA_KEY);
        String la4type2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA4TYPE2_DATA_KEY);
        String la4auto = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA4AUTO_DATA_KEY);
        String la4add = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA4ADD_DATA_KEY);

        String la5name = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA5NAME_DATA_KEY);
        String la5desc = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA5DESC_DATA_KEY);
        String la5mod = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA5MOD_DATA_KEY);
        String la5roll1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA5ROLL1_DATA_KEY);
        String la5roll2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA5ROLL2_DATA_KEY);
        String la5type1 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA5TYPE1_DATA_KEY);
        String la5type2 = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA5TYPE2_DATA_KEY);
        String la5auto = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA5AUTO_DATA_KEY);
        String la5add = mWizardModel.findByKey("Legendary Actions").getData().getString(LegendaryActionPage.LA5ADD_DATA_KEY);

        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, name);
        values.put(DataBaseHandler.KEY_TYPE, type);
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
        values.put(DataBaseHandler.KEY_AC, ac1);
        values.put(DataBaseHandler.KEY_ACTYPE, ac1Type);
        values.put(DataBaseHandler.KEY_AC2, ac2);
        values.put(DataBaseHandler.KEY_AC2TYPE, ac2Type);
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
        values.put(DataBaseHandler.KEY_SENSES, senses);
        values.put(DataBaseHandler.KEY_LANGUAGES, languages);

        values.put(DataBaseHandler.KEY_MULTIATTACK, multiAttack);

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

        values.put(DataBaseHandler.KEY_ATK2NAME, a2name);
        values.put(DataBaseHandler.KEY_ATK2DESC, a2desc);
        values.put(DataBaseHandler.KEY_ATK2MOD, a2mod);
        values.put(DataBaseHandler.KEY_ATK2DMG1ROLL, a2roll1);
        values.put(DataBaseHandler.KEY_ATK2DMG2ROLL, a2roll2);
        values.put(DataBaseHandler.KEY_ATK2DMG1TYPE, a2type1);
        values.put(DataBaseHandler.KEY_ATK2DMG2TYPE, a2type2);
        values.put(DataBaseHandler.KEY_ATK2AUTOROLL, a2auto);
        values.put(DataBaseHandler.KEY_ATK2ADDITIONAL, a2add);

        values.put(DataBaseHandler.KEY_ATK3NAME, a3name);
        values.put(DataBaseHandler.KEY_ATK3DESC, a3desc);
        values.put(DataBaseHandler.KEY_ATK3MOD, a3mod);
        values.put(DataBaseHandler.KEY_ATK3DMG1ROLL, a3roll1);
        values.put(DataBaseHandler.KEY_ATK3DMG2ROLL, a3roll2);
        values.put(DataBaseHandler.KEY_ATK3DMG1TYPE, a3type1);
        values.put(DataBaseHandler.KEY_ATK3DMG2TYPE, a3type2);
        values.put(DataBaseHandler.KEY_ATK3AUTOROLL, a3auto);
        values.put(DataBaseHandler.KEY_ATK3ADDITIONAL, a3add);

        values.put(DataBaseHandler.KEY_ATK4NAME, a4name);
        values.put(DataBaseHandler.KEY_ATK4DESC, a4desc);
        values.put(DataBaseHandler.KEY_ATK4MOD, a4mod);
        values.put(DataBaseHandler.KEY_ATK4DMG1ROLL, a4roll1);
        values.put(DataBaseHandler.KEY_ATK4DMG2ROLL, a4roll2);
        values.put(DataBaseHandler.KEY_ATK4DMG1TYPE, a4type1);
        values.put(DataBaseHandler.KEY_ATK4DMG2TYPE, a4type2);
        values.put(DataBaseHandler.KEY_ATK4AUTOROLL, a4auto);
        values.put(DataBaseHandler.KEY_ATK4ADDITIONAL, a4add);

        values.put(DataBaseHandler.KEY_REACTION1NAME, r1name);
        values.put(DataBaseHandler.KEY_REACTION1DESC, r1desc);

        /*TODO
        Add Action 5
        Add Legendary Actions
         */

        /*
        values.put(DataBaseHandler.KEY_ATK5NAME, a5name);
        values.put(DataBaseHandler.KEY_ATK5DESC, a5desc);
        values.put(DataBaseHandler.KEY_ATK5MOD, a5mod);
        values.put(DataBaseHandler.KEY_ATK5DMG1ROLL, a5roll1);
        values.put(DataBaseHandler.KEY_ATK5DMG2ROLL, a5roll2);
        values.put(DataBaseHandler.KEY_ATK5DMG1TYPE, a5type1);
        values.put(DataBaseHandler.KEY_ATK5DMG2TYPE, a5type2);
        values.put(DataBaseHandler.KEY_ATK5AUTOROLL, a5auto);
        values.put(DataBaseHandler.KEY_ATK5ADDITIONAL, a5add);
*/

/*
        values.put(DataBaseHandler.KEY_LMULTIATTACK, lmultiAttack);

        values.put(DataBaseHandler.KEY_LATK1NAME, la1name);
        values.put(DataBaseHandler.KEY_LATK1DESC, la1desc);
        values.put(DataBaseHandler.KEY_LATK1MOD, la1mod);
        values.put(DataBaseHandler.KEY_LATK1DMG1ROLL, la1roll1);
        values.put(DataBaseHandler.KEY_LATK1DMG2ROLL, la1roll2);
        values.put(DataBaseHandler.KEY_LATK1DMG1TYPE, la1type1);
        values.put(DataBaseHandler.KEY_LATK1DMG2TYPE, la1type2);
        values.put(DataBaseHandler.KEY_LATK1AUTOROLL, la1auto);
        values.put(DataBaseHandler.KEY_LATK1ADDITIONAL, la1add);

        values.put(DataBaseHandler.KEY_LATK2NAME, la2name);
        values.put(DataBaseHandler.KEY_LATK2DESC, la2desc);
        values.put(DataBaseHandler.KEY_LATK2MOD, la2mod);
        values.put(DataBaseHandler.KEY_LATK2DMG1ROLL, la2roll1);
        values.put(DataBaseHandler.KEY_LATK2DMG2ROLL, la2roll2);
        values.put(DataBaseHandler.KEY_LATK2DMG1TYPE, la2type1);
        values.put(DataBaseHandler.KEY_LATK2DMG2TYPE, la2type2);
        values.put(DataBaseHandler.KEY_LATK2AUTOROLL, la2auto);
        values.put(DataBaseHandler.KEY_LATK2ADDITIONAL, la2add);

        values.put(DataBaseHandler.KEY_LATK3NAME, la3name);
        values.put(DataBaseHandler.KEY_LATK3DESC, la3desc);
        values.put(DataBaseHandler.KEY_LATK3MOD, la3mod);
        values.put(DataBaseHandler.KEY_LATK3DMG1ROLL, la3roll1);
        values.put(DataBaseHandler.KEY_LATK3DMG2ROLL, la3roll2);
        values.put(DataBaseHandler.KEY_LATK3DMG1TYPE, la3type1);
        values.put(DataBaseHandler.KEY_LATK3DMG2TYPE, la3type2);
        values.put(DataBaseHandler.KEY_LATK3AUTOROLL, la3auto);
        values.put(DataBaseHandler.KEY_LATK3ADDITIONAL, la3add);

        values.put(DataBaseHandler.KEY_LATK4NAME, la4name);
        values.put(DataBaseHandler.KEY_LATK4DESC, la4desc);
        values.put(DataBaseHandler.KEY_LATK4MOD, la4mod);
        values.put(DataBaseHandler.KEY_LATK4DMG1ROLL, la4roll1);
        values.put(DataBaseHandler.KEY_LATK4DMG2ROLL, la4roll2);
        values.put(DataBaseHandler.KEY_LATK4DMG1TYPE, la4type1);
        values.put(DataBaseHandler.KEY_LATK4DMG2TYPE, la4type2);
        values.put(DataBaseHandler.KEY_LATK4AUTOROLL, la4auto);
        values.put(DataBaseHandler.KEY_LATK4ADDITIONAL, la4add);

        values.put(DataBaseHandler.KEY_LATK5NAME, la5name);
        values.put(DataBaseHandler.KEY_LATK5DESC, la5desc);
        values.put(DataBaseHandler.KEY_LATK5MOD, la5mod);
        values.put(DataBaseHandler.KEY_LATK5DMG1ROLL, la5roll1);
        values.put(DataBaseHandler.KEY_LATK5DMG2ROLL, la5roll2);
        values.put(DataBaseHandler.KEY_LATK5DMG1TYPE, la5type1);
        values.put(DataBaseHandler.KEY_LATK5DMG2TYPE, la5type2);
        values.put(DataBaseHandler.KEY_LATK5AUTOROLL, la5auto);
        values.put(DataBaseHandler.KEY_LATK5ADDITIONAL, la5add);
*/


        if (imageUri == null) {
            imageUri = "android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight";
        }
        values.put(DataBaseHandler.KEY_ICON, imageUri);
        values.put(DataBaseHandler.KEY_SOURCE, "USER");
        values.put(DataBaseHandler.KEY_IDENTIFIER, DataBaseHandler.TYPE_MONSTER);

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
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE));
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

            String ac1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
            String ac1Type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ACTYPE));
            String ac2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC2));
            String ac2Type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC2TYPE));

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
            String senses = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SENSES));
            String languages = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LANGUAGES));


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

            String multiAttack = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MULTIATTACK));

            String a1name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1NAME));
            String a1desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DESC));
            String a1mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1MOD));
            String a1roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG1ROLL));
            String a1roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG2ROLL));
            String a1type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG1TYPE));
            String a1type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG2TYPE));
            String a1auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1AUTOROLL));
            String a1add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1ADDITIONAL));

            String a2name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2NAME));
            String a2desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DESC));
            String a2mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2MOD));
            String a2roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG1ROLL));
            String a2roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG2ROLL));
            String a2type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG1TYPE));
            String a2type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG2TYPE));
            String a2auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2AUTOROLL));
            String a2add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2ADDITIONAL));

            String a3name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3NAME));
            String a3desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DESC));
            String a3mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3MOD));
            String a3roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG1ROLL));
            String a3roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG2ROLL));
            String a3type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG1TYPE));
            String a3type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG2TYPE));
            String a3auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3AUTOROLL));
            String a3add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3ADDITIONAL));

            String a4name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4NAME));
            String a4desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DESC));
            String a4mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4MOD));
            String a4roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG1ROLL));
            String a4roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG2ROLL));
            String a4type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG1TYPE));
            String a4type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG2TYPE));
            String a4auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4AUTOROLL));
            String a4add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4ADDITIONAL));

            String r1name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION1NAME));
            String r1desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION1DESC));

            /*
            String a5name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5NAME));
            String a5desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5DESC));
            String a5mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5MOD));
            String a5roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5DMG1ROLL));
            String a5roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5DMG2ROLL));
            String a5type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5DMG1TYPE));
            String a5type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5DMG2TYPE));
            String a5auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5AUTOROLL));
            String a5add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5ADDITIONAL));
*/

/*
            String lmultiAttack = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LMULTIATTACK));

            String la1name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1NAME));
            String la1desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1DESC));
            String la1mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1MOD));
            String la1roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1DMG1ROLL));
            String la1roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1DMG2ROLL));
            String la1type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1DMG1TYPE));
            String la1type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1DMG2TYPE));
            String la1auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1AUTOROLL));
            String la1add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1ADDITIONAL));

            String la2name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2NAME));
            String la2desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2DESC));
            String la2mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2MOD));
            String la2roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2DMG1ROLL));
            String la2roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2DMG2ROLL));
            String la2type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2DMG1TYPE));
            String la2type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2DMG2TYPE));
            String la2auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2AUTOROLL));
            String la2add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2ADDITIONAL));

            String la3name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3NAME));
            String la3desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3DESC));
            String la3mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3MOD));
            String la3roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3DMG1ROLL));
            String la3roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3DMG2ROLL));
            String la3type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3DMG1TYPE));
            String la3type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3DMG2TYPE));
            String la3auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3AUTOROLL));
            String la3add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3ADDITIONAL));

            String la4name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4NAME));
            String la4desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4DESC));
            String la4mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4MOD));
            String la4roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4DMG1ROLL));
            String la4roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4DMG2ROLL));
            String la4type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4DMG1TYPE));
            String la4type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4DMG2TYPE));
            String la4auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4AUTOROLL));
            String la4add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4ADDITIONAL));

            String la5name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5NAME));
            String la5desc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5DESC));
            String la5mod = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5MOD));
            String la5roll1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5DMG1ROLL));
            String la5roll2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5DMG2ROLL));
            String la5type1 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5DMG1TYPE));
            String la5type2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5DMG2TYPE));
            String la5auto = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5AUTOROLL));
            String la5add = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5ADDITIONAL));
*/

            createBundleBasicInfo(name, type, alignment, imageUri, size);
            createBundleAbilities(str, dex, con, intel, wis, cha, hp, hpdice, ac1, ac1Type, ac2, ac2Type, speed);
            createBundleSkills(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival, dmgVul, dmgRes, dmgIm, conIm, stStr, stDex, stCon, stInt, stWis, stCha, senses, languages);
            createBundleTraits(t1Name, t1Desc, t2Name, t2Desc, t3Name, t3Desc, t4Name, t4Desc, t5Name, t5Desc);
           // createBundleActions(a1name, a1desc, a1mod, a1roll1, a1roll2, a1type1, a1type2, a1auto, a1add, a2name, a2desc, a2mod, a2roll1, a2roll2, a2type1, a2type2, a2auto, a2add, a3name, a3desc, a3mod, a3roll1, a3roll2, a3type1, a3type2, a3auto, a3add, a4name, a4desc, a4mod, a4roll1, a4roll2, a4type1, a4type2, a4auto, a4add, a5name, a5desc, a5mod, a5roll1, a5roll2, a5type1, a5type2, a5auto, a5add);
            createBundleActions(multiAttack, a1name, a1desc, a1mod, a1roll1, a1roll2, a1type1, a1type2, a1auto, a1add, a2name, a2desc, a2mod, a2roll1, a2roll2, a2type1, a2type2, a2auto, a2add, a3name, a3desc, a3mod, a3roll1, a3roll2, a3type1, a3type2, a3auto, a3add, a4name, a4desc, a4mod, a4roll1, a4roll2, a4type1, a4type2, a4auto, a4add);
            //createBundleLegendaryActions(lmultiAttack, la1name, la1desc, la1mod, la1roll1, la1roll2, la1type1, la1type2, la1auto, la1add, la2name, la2desc, la2mod, la2roll1, la2roll2, la2type1, la2type2, la2auto, la2add, la3name, la3desc, la3mod, la3roll1, la3roll2, la3type1, la3type2, la3auto, la3add, la4name, la4desc, la4mod, la4roll1, la4roll2, la4type1, la4type2, la4auto, la4add);
            createBundleReactions(r1name, r1desc);
        }
        assert cursor != null;
        cursor.close();

    }

    private void createBundleBasicInfo(String name, String type, String alignment, String imageUri, String size) {
        Bundle bdl1 = new Bundle();
        bdl1.putString(BasicInfoPage.NAME_DATA_KEY, name);
        bdl1.putString(BasicInfoPage.TYPE_DATA_KEY, type);
        bdl1.putString(BasicInfoPage.ALIGNMENT_DATA_KEY, alignment);
        bdl1.putString(BasicInfoPage.IMAGEURI_DATA_KEY, imageUri);
        bdl1.putString(BasicInfoPage.SIZE_DATA_KEY, size);
        bundle.putBundle("Basic Info", bdl1);
    }


    private void createBundleAbilities(String str, String dex, String con, String intel, String wis, String cha, String hp, String hpdice, String ac1, String ac1Type, String ac2, String ac2Type, String speed) {
        Bundle bdl1 = new Bundle();
        bdl1.putString(AbilitiesPage.STR_DATA_KEY, str);
        bdl1.putString(AbilitiesPage.DEX_DATA_KEY, dex);
        bdl1.putString(AbilitiesPage.CON_DATA_KEY, con);
        bdl1.putString(AbilitiesPage.INT_DATA_KEY, intel);
        bdl1.putString(AbilitiesPage.WIS_DATA_KEY, wis);
        bdl1.putString(AbilitiesPage.CHA_DATA_KEY, cha);
        bdl1.putString(AbilitiesPage.HP_DATA_KEY, hp);
        bdl1.putString(AbilitiesPage.HITDICE_DATA_KEY, hpdice);
        bdl1.putString(AbilitiesPage.AC1_DATA_KEY, ac1);
        bdl1.putString(AbilitiesPage.AC1TYPE_DATA_KEY, ac1Type);
        bdl1.putString(AbilitiesPage.AC2_DATA_KEY, ac2);
        bdl1.putString(AbilitiesPage.AC2TYPE_DATA_KEY, ac2Type);
        bdl1.putString(AbilitiesPage.SPEED_DATA_KEY, speed);
        bundle.putBundle("Abilities & Hit Points", bdl1);
    }

    private void createBundleSkills(String acrobatics, String animalhandling, String arcana, String athletics, String deception, String history, String insight, String intimidation, String investigation, String medicine, String nature, String perception, String performance, String persuasion, String religion, String sleightofhand, String stealth, String survival, String dmgVul, String dmgRes, String dmgIm, String conIm, String stStr, String stDex, String stCon, String stInt, String stWis, String stCha, String senses, String languages) {
        Bundle bdl1 = new Bundle();
        bdl1.putString(SkillsPage.ACROBATICS_DATA_KEY, acrobatics);
        bdl1.putString(SkillsPage.ANIMALHANDLING_DATA_KEY, animalhandling);
        bdl1.putString(SkillsPage.ARCANA_DATA_KEY, arcana);
        bdl1.putString(SkillsPage.ATHLETICS_DATA_KEY, athletics);
        bdl1.putString(SkillsPage.DECEPTION_DATA_KEY, deception);
        bdl1.putString(SkillsPage.HISTORY_DATA_KEY, history);
        bdl1.putString(SkillsPage.INSIGHT_DATA_KEY, insight);
        bdl1.putString(SkillsPage.INTIMIDATION_DATA_KEY, intimidation);
        bdl1.putString(SkillsPage.INVESTIGATION_DATA_KEY, investigation);
        bdl1.putString(SkillsPage.MEDICINE_DATA_KEY, medicine);
        bdl1.putString(SkillsPage.NATURE_DATA_KEY, nature);
        bdl1.putString(SkillsPage.PERCEPTION_DATA_KEY, perception);
        bdl1.putString(SkillsPage.PERFORMANCE_DATA_KEY, performance);
        bdl1.putString(SkillsPage.PERSUASION_DATA_KEY, persuasion);
        bdl1.putString(SkillsPage.RELIGION_DATA_KEY, religion);
        bdl1.putString(SkillsPage.SLEIGHTOFHAND_DATA_KEY, sleightofhand);
        bdl1.putString(SkillsPage.STEALTH_DATA_KEY, stealth);
        bdl1.putString(SkillsPage.SURVIVAL_DATA_KEY, survival);
        bdl1.putString(SkillsPage.STSTR_DATA_KEY, stStr);
        bdl1.putString(SkillsPage.STDEX_DATA_KEY, stDex);
        bdl1.putString(SkillsPage.STCON_DATA_KEY, stCon);
        bdl1.putString(SkillsPage.STINT_DATA_KEY, stInt);
        bdl1.putString(SkillsPage.STWIS_DATA_KEY, stWis);
        bdl1.putString(SkillsPage.STCHA_DATA_KEY, stCha);
        bdl1.putString(SkillsPage.DMGVUL_DATA_KEY, dmgVul);
        bdl1.putString(SkillsPage.DMGRES_DATA_KEY, dmgRes);
        bdl1.putString(SkillsPage.DMGIM_DATA_KEY, dmgIm);
        bdl1.putString(SkillsPage.CONIM_DATA_KEY, conIm);
        bdl1.putString(SkillsPage.SENSES_DATA_KEY, senses);
        bdl1.putString(SkillsPage.LANGUAGES_DATA_KEY, languages);
        bundle.putBundle("Additional Info", bdl1);
    }


    private void createBundleTraits(String t1Name, String t1Desc, String t2Name, String t2Desc, String t3Name, String t3Desc, String t4Name, String t4Desc, String t5Name, String t5Desc) {
        Bundle bdl1 = new Bundle();
        bdl1.putString(TraitsPage.T1NAME_DATA_KEY, t1Name);
        bdl1.putString(TraitsPage.T1DESC_DATA_KEY, t1Desc);
        bdl1.putString(TraitsPage.T2NAME_DATA_KEY, t2Name);
        bdl1.putString(TraitsPage.T2DESC_DATA_KEY, t2Desc);
        bdl1.putString(TraitsPage.T3NAME_DATA_KEY, t3Name);
        bdl1.putString(TraitsPage.T3DESC_DATA_KEY, t3Desc);
        bdl1.putString(TraitsPage.T4NAME_DATA_KEY, t4Name);
        bdl1.putString(TraitsPage.T4DESC_DATA_KEY, t4Desc);
        bdl1.putString(TraitsPage.T5NAME_DATA_KEY, t5Name);
        bdl1.putString(TraitsPage.T5DESC_DATA_KEY, t5Desc);
        bundle.putBundle("Traits", bdl1);
    }
//    private void createBundleActions(String a1name, String a1desc, String a1mod, String a1roll1, String a1roll2, String a1type1, String a1type2, String a1auto, String a1add, String a2name, String a2desc, String a2mod, String a2roll1, String a2roll2, String a2type1, String a2type2, String a2auto, String a2add, String a3name, String a3desc, String a3mod, String a3roll1, String a3roll2, String a3type1, String a3type2, String a3auto, String a3add, String a4name, String a4desc, String a4mod, String a4roll1, String a4roll2, String a4type1, String a4type2, String a4auto, String a4add, String a5name, String a5desc, String a5mod, String a5roll1, String a5roll2, String a5type1, String a5type2, String a5auto, String a5add) {

    private void createBundleActions(String multiattack, String a1name, String a1desc, String a1mod, String a1roll1, String a1roll2, String a1type1, String a1type2, String a1auto, String a1add, String a2name, String a2desc, String a2mod, String a2roll1, String a2roll2, String a2type1, String a2type2, String a2auto, String a2add, String a3name, String a3desc, String a3mod, String a3roll1, String a3roll2, String a3type1, String a3type2, String a3auto, String a3add, String a4name, String a4desc, String a4mod, String a4roll1, String a4roll2, String a4type1, String a4type2, String a4auto, String a4add) {
        Bundle bdl1 = new Bundle();

        bdl1.putString(ActionPage.MULTIATTACK_DATA_KEY, multiattack);

        bdl1.putString(ActionPage.A1NAME_DATA_KEY, a1name);
        bdl1.putString(ActionPage.A1DESC_DATA_KEY, a1desc);
        bdl1.putString(ActionPage.A1MOD_DATA_KEY, a1mod);
        bdl1.putString(ActionPage.A1ROLL1_DATA_KEY, a1roll1);
        bdl1.putString(ActionPage.A1ROLL2_DATA_KEY, a1roll2);
        bdl1.putString(ActionPage.A1TYPE1_DATA_KEY, a1type1);
        bdl1.putString(ActionPage.A1TYPE2_DATA_KEY, a1type2);
        bdl1.putString(ActionPage.A1AUTO_DATA_KEY, a1auto);
        bdl1.putString(ActionPage.A1ADD_DATA_KEY, a1add);

        bdl1.putString(ActionPage.A2NAME_DATA_KEY, a2name);
        bdl1.putString(ActionPage.A2DESC_DATA_KEY, a2desc);
        bdl1.putString(ActionPage.A2MOD_DATA_KEY, a2mod);
        bdl1.putString(ActionPage.A2ROLL1_DATA_KEY, a2roll1);
        bdl1.putString(ActionPage.A2ROLL2_DATA_KEY, a2roll2);
        bdl1.putString(ActionPage.A2TYPE1_DATA_KEY, a2type1);
        bdl1.putString(ActionPage.A2TYPE2_DATA_KEY, a2type2);
        bdl1.putString(ActionPage.A2AUTO_DATA_KEY, a2auto);
        bdl1.putString(ActionPage.A2ADD_DATA_KEY, a2add);

        bdl1.putString(ActionPage.A3NAME_DATA_KEY, a3name);
        bdl1.putString(ActionPage.A3DESC_DATA_KEY, a3desc);
        bdl1.putString(ActionPage.A3MOD_DATA_KEY, a3mod);
        bdl1.putString(ActionPage.A3ROLL1_DATA_KEY, a3roll1);
        bdl1.putString(ActionPage.A3ROLL2_DATA_KEY, a3roll2);
        bdl1.putString(ActionPage.A3TYPE1_DATA_KEY, a3type1);
        bdl1.putString(ActionPage.A3TYPE2_DATA_KEY, a3type2);
        bdl1.putString(ActionPage.A3AUTO_DATA_KEY, a3auto);
        bdl1.putString(ActionPage.A3ADD_DATA_KEY, a3add);

        bdl1.putString(ActionPage.A4NAME_DATA_KEY, a4name);
        bdl1.putString(ActionPage.A4DESC_DATA_KEY, a4desc);
        bdl1.putString(ActionPage.A4MOD_DATA_KEY, a4mod);
        bdl1.putString(ActionPage.A4ROLL1_DATA_KEY, a4roll1);
        bdl1.putString(ActionPage.A4ROLL2_DATA_KEY, a4roll2);
        bdl1.putString(ActionPage.A4TYPE1_DATA_KEY, a4type1);
        bdl1.putString(ActionPage.A4TYPE2_DATA_KEY, a4type2);
        bdl1.putString(ActionPage.A4AUTO_DATA_KEY, a4auto);
        bdl1.putString(ActionPage.A4ADD_DATA_KEY, a4add);

        /*
        bdl1.putString("a5name", a5name);
        bdl1.putString("a5desc", a5desc);
        bdl1.putString("a5mod", a5mod);
        bdl1.putString("a5roll1", a5roll1);
        bdl1.putString("a5roll2", a5roll2);
        bdl1.putString("a5type1", a5type1);
        bdl1.putString("a5type2", a5type2);
        bdl1.putString("a5auto", a5auto);
        bdl1.putString("a5add", a5add);
        */

        bundle.putBundle("Actions", bdl1);
    }

/*
    private void createBundleActions(String multiattack, String a1name, String a1desc, String a1mod, String a1roll1, String a1roll2, String a1type1, String a1type2, String a1auto, String a1add, String a2name, String a2desc, String a2mod, String a2roll1, String a2roll2, String a2type1, String a2type2, String a2auto, String a2add, String a3name, String a3desc, String a3mod, String a3roll1, String a3roll2, String a3type1, String a3type2, String a3auto, String a3add, String a4name, String a4desc, String a4mod, String a4roll1, String a4roll2, String a4type1, String a4type2, String a4auto, String a4add) {
        Bundle bdl1 = new Bundle();

        bdl1.putString(LegendaryActionPage.LMULTIATTACK_DATA_KEY, lmultiattack);

        bdl1.putString(LegendaryActionPage.LA1NAME_DATA_KEY, la1name);
        bdl1.putString(LegendaryActionPage.LA1DESC_DATA_KEY, la1desc);
        bdl1.putString(LegendaryActionPage.LA1MOD_DATA_KEY, la1mod);
        bdl1.putString(LegendaryActionPage.LA1ROLL1_DATA_KEY, la1roll1);
        bdl1.putString(LegendaryActionPage.LA1ROLL2_DATA_KEY, la1roll2);
        bdl1.putString(LegendaryActionPage.LA1TYPE1_DATA_KEY, la1type1);
        bdl1.putString(LegendaryActionPage.LA1TYPE2_DATA_KEY, la1type2);
        bdl1.putString(LegendaryActionPage.LA1AUTO_DATA_KEY, la1auto);
        bdl1.putString(LegendaryActionPage.LA1ADD_DATA_KEY, la1add);

        bdl1.putString(LegendaryActionPage.LA2NAME_DATA_KEY, la2name);
        bdl1.putString(LegendaryActionPage.LA2DESC_DATA_KEY, la2desc);
        bdl1.putString(LegendaryActionPage.LA2MOD_DATA_KEY, la2mod);
        bdl1.putString(LegendaryActionPage.LA2ROLL1_DATA_KEY, la2roll1);
        bdl1.putString(LegendaryActionPage.LA2ROLL2_DATA_KEY, la2roll2);
        bdl1.putString(LegendaryActionPage.LA2TYPE1_DATA_KEY, la2type1);
        bdl1.putString(LegendaryActionPage.LA2TYPE2_DATA_KEY, la2type2);
        bdl1.putString(LegendaryActionPage.LA2AUTO_DATA_KEY, la2auto);
        bdl1.putString(LegendaryActionPage.LA2ADD_DATA_KEY, la2add);

        bdl1.putString(LegendaryActionPage.LA3NAME_DATA_KEY, la3name);
        bdl1.putString(LegendaryActionPage.LA3DESC_DATA_KEY, la3desc);
        bdl1.putString(LegendaryActionPage.LA3MOD_DATA_KEY, la3mod);
        bdl1.putString(LegendaryActionPage.LA3ROLL1_DATA_KEY, la3roll1);
        bdl1.putString(LegendaryActionPage.LA3ROLL2_DATA_KEY, la3roll2);
        bdl1.putString(LegendaryActionPage.LA3TYPE1_DATA_KEY, la3type1);
        bdl1.putString(LegendaryActionPage.LA3TYPE2_DATA_KEY, la3type2);
        bdl1.putString(LegendaryActionPage.LA3AUTO_DATA_KEY, la3auto);
        bdl1.putString(LegendaryActionPage.LA3ADD_DATA_KEY, la3add);

        bdl1.putString(LegendaryActionPage.LA4NAME_DATA_KEY, la4name);
        bdl1.putString(LegendaryActionPage.LA4DESC_DATA_KEY, la4desc);
        bdl1.putString(LegendaryActionPage.LA4MOD_DATA_KEY, la4mod);
        bdl1.putString(LegendaryActionPage.LA4ROLL1_DATA_KEY, la4roll1);
        bdl1.putString(LegendaryActionPage.LA4ROLL2_DATA_KEY, la4roll2);
        bdl1.putString(LegendaryActionPage.LA4TYPE1_DATA_KEY, la4type1);
        bdl1.putString(LegendaryActionPage.LA4TYPE2_DATA_KEY, la4type2);
        bdl1.putString(LegendaryActionPage.LA4AUTO_DATA_KEY, la4auto);
        bdl1.putString(LegendaryActionPage.LA4ADD_DATA_KEY, la4add);

        bdl1.putString(LegendaryActionPage.LA5NAME_DATA_KEY, la5name);
        bdl1.putString(LegendaryActionPage.LA5DESC_DATA_KEY, la5desc);
        bdl1.putString(LegendaryActionPage.LA5MOD_DATA_KEY, la5mod);
        bdl1.putString(LegendaryActionPage.LA5ROLL1_DATA_KEY, la5roll1);
        bdl1.putString(LegendaryActionPage.LA5ROLL2_DATA_KEY, la5roll2);
        bdl1.putString(LegendaryActionPage.LA5TYPE1_DATA_KEY, la5type1);
        bdl1.putString(LegendaryActionPage.LA5TYPE2_DATA_KEY, la5type2);
        bdl1.putString(LegendaryActionPage.LA5AUTO_DATA_KEY, la5auto);
        bdl1.putString(LegendaryActionPage.LA5ADD_DATA_KEY, la5add);


        bundle.putBundle("Legendary Actions", bdl1);
    }

    */

    private void createBundleReactions(String r1name, String r1desc) {
        Bundle bdl1 = new Bundle();
        bdl1.putString(ReactionPage.R1NAME_DATA_KEY, r1name);
        bdl1.putString(ReactionPage.R1DESC_DATA_KEY, r1desc);
        bundle.putBundle("Reactions", bdl1);
    }

    @Override
    public void onSizeChanged(String size) {
        if(getmPagerAdapter().getRegisteredFragment(1) != null){
            ((AbilitiesFragment)getmPagerAdapter().getRegisteredFragment(1)).onSizeChanged(size);
        }
    }


    @Override
    public void onWisdomChanged(String wisdom) {
        if(getmPagerAdapter().getRegisteredFragment(2) != null){
            ((SkillsFragment)getmPagerAdapter().getRegisteredFragment(2)).onWisdomChanged(wisdom);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public void onNameChanged(String size) {
        this.name = size;
    }
}
