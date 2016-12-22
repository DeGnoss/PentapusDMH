package com.pentapus.pentapusdmh.Fragments.Spells;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.Utils;


public class MySpellEditFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String SPELL_ID = "spellId";
    private static final String ENCOUNTER_ID = "encounterId";

    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_CHOOSE_IMG = 2;


    private Uri myFile;

    private boolean modeUpdate;
    private int spellId;

    Button addchar_btn;
    ImageButton bChooseImage;
    EditText name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar;

    public MySpellEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modeUpdate  Parameter 1.
     * @param spellId       Parameter 2.
     * @return A new instance of fragment EncounterEditFragment.
     */
    public static MySpellEditFragment newInstance(boolean modeUpdate, int spellId) {
        MySpellEditFragment fragment = new MySpellEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(MODE, modeUpdate);
        args.putInt(SPELL_ID, spellId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            modeUpdate = getArguments().getBoolean(MODE);
            //check whether entry gets updated or added
            if (modeUpdate) {
                spellId = getArguments().getInt(SPELL_ID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_mymonster_edit, container, false);
        charEditView.setBackgroundColor(Color.WHITE);
        name_tf = (EditText) charEditView.findViewById(R.id.etName);
        info_tf = (EditText) charEditView.findViewById(R.id.etInfo);
        init_tf = (EditText) charEditView.findViewById(R.id.etInit);
        maxHp_tf = (EditText) charEditView.findViewById(R.id.etHpMax);
        ac_tf = (EditText) charEditView.findViewById(R.id.etAc);
        etStrength = (EditText) charEditView.findViewById(R.id.etStrength);
        etDex = (EditText) charEditView.findViewById(R.id.etDex);
        etConst = (EditText) charEditView.findViewById(R.id.etConst);
        etInt = (EditText) charEditView.findViewById(R.id.etInt);
        etWis = (EditText) charEditView.findViewById(R.id.etWis);
        etChar = (EditText) charEditView.findViewById(R.id.etChar);
        bChooseImage = (ImageButton) charEditView.findViewById(R.id.bChooseImage);


        if (modeUpdate) {
            loadSpellInfo(name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar, spellId);
        }
        addchar_btn = (Button) charEditView.findViewById(R.id.bDone);
        addchar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButton(modeUpdate);
            }
        });

        bChooseImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               // showViewPager();
            }
        });

        // Inflate the layout for this fragment
        return charEditView;
    }


    private void loadSpellInfo(EditText name, EditText info, EditText init, EditText maxHp, EditText ac, EditText strength, EditText dex, EditText constit, EditText intelligence, EditText wis, EditText charisma, int id) {

        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SPELL + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_SPELL, null, null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            String myInfo = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
            String myInitiative = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LEVEL)));
            String myMaxHp = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_VOCAL)));
            String myAc = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SOMATIC)));
            String myStrength = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MATERIAL)));
            String myDexterity = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TIME));
            String myConstitution = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DURATION));
            String myIntelligence = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SCHOOL)));
            String myWisdom = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_RANGE));
            //cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO))
           // cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_PHB))

            name.setText(myName, TextView.BufferType.EDITABLE);
            info.setText(myInfo, TextView.BufferType.EDITABLE);
            init.setText(myInitiative, TextView.BufferType.EDITABLE);
            maxHp.setText(myMaxHp, TextView.BufferType.EDITABLE);
            ac.setText(myAc, TextView.BufferType.EDITABLE);
            strength.setText(myStrength, TextView.BufferType.EDITABLE);
            dex.setText(myDexterity, TextView.BufferType.EDITABLE);
            constit.setText(myConstitution, TextView.BufferType.EDITABLE);
            intelligence.setText(myIntelligence, TextView.BufferType.EDITABLE);
            wis.setText(myWisdom, TextView.BufferType.EDITABLE);
            bChooseImage.setImageURI(myFile);
        }
        assert cursor != null;
        cursor.close();
    }

    public void doneButton(boolean modeUpdate) {
        // get values from the input text fields
        String myName = name_tf.getText().toString();
        String myInfo = info_tf.getText().toString();
        String myInitiative = init_tf.getText().toString();
        String myMaxHp = maxHp_tf.getText().toString();
        String myAc = ac_tf.getText().toString();
        String myStrength = etStrength.getText().toString();
        String myDexterity = etDex.getText().toString();
        String myConstitution = etConst.getText().toString();
        String myIntelligence = etInt.getText().toString();
        String myWisdom = etWis.getText().toString();
        String myCharisma = etChar.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, myName);
        values.put(DataBaseHandler.KEY_INFO, myInfo);
        values.put(DataBaseHandler.KEY_LEVEL, myInitiative);
        values.put(DataBaseHandler.KEY_VOCAL, myMaxHp);
        values.put(DataBaseHandler.KEY_SOMATIC, myAc);
        values.put(DataBaseHandler.KEY_MATERIAL, myStrength);
        values.put(DataBaseHandler.KEY_TIME, myDexterity);
        values.put(DataBaseHandler.KEY_DURATION, myConstitution);
        values.put(DataBaseHandler.KEY_SCHOOL, myIntelligence);
        values.put(DataBaseHandler.KEY_RANGE, myWisdom);
        values.put(DataBaseHandler.KEY_INFO, "info");
        values.put(DataBaseHandler.KEY_SOURCE, "custom");

        // insert a record
        if (!modeUpdate) {
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_SPELL, values);
        }
        // update a record
        else {
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SPELL + "/" + spellId);
            getContext().getContentResolver().update(uri, values, null, null);
        }

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume(){
        super.onResume();
        //((MainActivity)getActivity()).setFabVisibility(false);
        ((MainActivity)getActivity()).disableNavigationDrawer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_CHOOSE_IMG && resultCode == Activity.RESULT_OK){
            if(data != null) {
                String value = data.getStringExtra("imageUri");
                if(value != null) {
                    Uri uri = Uri.parse(value);
                    myFile = uri;
                    bChooseImage.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            bChooseImage.setImageURI(myFile);
                        }
                    });
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
