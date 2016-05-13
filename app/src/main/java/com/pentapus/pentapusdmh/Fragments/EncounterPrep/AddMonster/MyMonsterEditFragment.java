package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;

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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.Utils;
import com.soundcloud.android.crop.Crop;


public class MyMonsterEditFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String MONSTER_ID = "monsterId";
    private static final String ENCOUNTER_ID = "encounterId";

    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_CHOOSE_IMG = 2;


    private Uri myFile;

    private boolean modeUpdate;
    private int monsterId;
    private int encounterId;
    private int px;

    Button addchar_btn;
    ImageButton bChooseImage;
    EditText name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar;

    public MyMonsterEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modeUpdate  Parameter 1.
     * @param monsterId       Parameter 2.
     * @param encounterId Parameter 3.
     * @return A new instance of fragment EncounterEditFragment.
     */
    public static MyMonsterEditFragment newInstance(boolean modeUpdate, int monsterId, int encounterId) {
        MyMonsterEditFragment fragment = new MyMonsterEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(MODE, modeUpdate);
        args.putInt(MONSTER_ID, monsterId);
        args.putInt(ENCOUNTER_ID, encounterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            modeUpdate = getArguments().getBoolean(MODE);
            encounterId = getArguments().getInt(ENCOUNTER_ID);
            //check whether entry gets updated or added
            if (modeUpdate) {
                monsterId = getArguments().getInt(MONSTER_ID);
            }
        }
        px = (int) Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_mymonster_edit, container, false);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) charEditView.getLayoutParams();
        params.setMargins(0, Utils.getStatusBarHeight(getActivity()), 0, 0);
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
            loadMonsterInfo(name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar, monsterId);
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
                showViewPager();
            }
        });



/*
        bChooseImage.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                Glide.clear(bChooseImage);
                Crop.pickImage(getContext(), getActivity().getSupportFragmentManager().findFragmentByTag("FE_MONSTER"));
                return true;
            }
        });*/



        // Inflate the layout for this fragment
        return charEditView;
    }


    public void showViewPager() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        MonsterViewPagerDialogFragment newFragment = new MonsterViewPagerDialogFragment();

        ImageViewPagerDialogFragment newFragment = new ImageViewPagerDialogFragment();
        newFragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag("FE_MYMONSTER"), RESULT_CHOOSE_IMG);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(R.id.drawer_layout, newFragment, "F_IMAGE_PAGER")
                .addToBackStack("F_IMAGE_PAGER").commit();
    }

    private void loadMonsterInfo(EditText name, EditText info, EditText init, EditText maxHp, EditText ac, EditText strength, EditText dex, EditText constit, EditText intelligence, EditText wis, EditText charisma, int id) {

        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_MONSTER_TEMPLATE, null, null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            String myInfo = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
            String myInitiative = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS));
            String myMaxHp = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
            String myAc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
            String myStrength = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH));
            String myDexterity = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY));
            String myConstitution = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION));
            String myIntelligence = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE));
            String myWisdom = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM));
            String myCharisma = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA));
            myFile = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));

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
            charisma.setText(myCharisma, TextView.BufferType.EDITABLE);
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
        values.put(DataBaseHandler.KEY_INITIATIVEBONUS, myInitiative);
        values.put(DataBaseHandler.KEY_MAXHP, myMaxHp);
        values.put(DataBaseHandler.KEY_AC, myAc);
        values.put(DataBaseHandler.KEY_STRENGTH, myStrength);
        values.put(DataBaseHandler.KEY_DEXTERITY, myDexterity);
        values.put(DataBaseHandler.KEY_CONSTITUTION, myConstitution);
        values.put(DataBaseHandler.KEY_INTELLIGENCE, myIntelligence);
        values.put(DataBaseHandler.KEY_WISDOM, myWisdom);
        values.put(DataBaseHandler.KEY_CHARISMA, myCharisma);
        if(myFile == null){
            myFile = Uri.parse("android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight");
        }
        values.put(DataBaseHandler.KEY_ICON, String.valueOf(myFile));
        values.put(DataBaseHandler.KEY_MM, 0);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_CHOOSE_IMG && resultCode == Activity.RESULT_OK){
            if(data != null) {
                String value = data.getStringExtra("imageUri");
                if(value != null) {
                    Uri uri = Uri.parse(value);
                    myFile = uri;
                    Log.v("MonsterEdit", "Data passed from Child fragment = " + uri);
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
