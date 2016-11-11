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
import com.wizardpager.wizard.ui.StepPagerStrip;

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
        saveData(name, type, alignment);
    }

    private void saveData(String name, String type, String alignment){
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, name);
        values.put(DataBaseHandler.KEY_MONSTERTYPE, type);
        values.put(DataBaseHandler.KEY_ALIGNMENT, alignment);
       /* values.put(DataBaseHandler.KEY_INITIATIVEBONUS, myInitiative);
        values.put(DataBaseHandler.KEY_MAXHP, myMaxHp);
        values.put(DataBaseHandler.KEY_AC, myAc);
        values.put(DataBaseHandler.KEY_STRENGTH, myStrength);
        values.put(DataBaseHandler.KEY_DEXTERITY, myDexterity);
        values.put(DataBaseHandler.KEY_CONSTITUTION, myConstitution);
        values.put(DataBaseHandler.KEY_INTELLIGENCE, myIntelligence);
        values.put(DataBaseHandler.KEY_WISDOM, myWisdom);
        values.put(DataBaseHandler.KEY_CHARISMA, myCharisma);*/
        if(myFile == null){
            myFile = Uri.parse("android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight");
        }
        values.put(DataBaseHandler.KEY_ICON, String.valueOf(myFile));
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
            createBundleBasicInfo(name, type, alignment);
            //

        }
        assert cursor != null;
        cursor.close();

    }

    private void createBundleBasicInfo(String name, String type, String alignment){
        Bundle bdl1 = new Bundle();
        bdl1.putString("name",name);
        bdl1.putString("type",type);
        bdl1.putString("alignment",alignment);
        bundle.putBundle("Basic Info",bdl1);
    }

}
