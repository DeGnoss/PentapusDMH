package com.pentapus.pentapusdmh.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.R;

public class NPCEditFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String NPC_ID = "npcId";
    private static final String ENCOUNTER_ID = "encounterId";

    private boolean modeUpdate;
    private int npcId;
    private int encounterId;

    Button addchar_btn;
    EditText name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar;

    public NPCEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modeUpdate  Parameter 1.
     * @param npcId       Parameter 2.
     * @param encounterId Parameter 3.
     * @return A new instance of fragment EncounterEditFragment.
     */
    public static NPCEditFragment newInstance(boolean modeUpdate, int npcId, int encounterId) {
        NPCEditFragment fragment = new NPCEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(MODE, modeUpdate);
        args.putInt(NPC_ID, npcId);
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
            //check wheter entry gets updated or added
            if (modeUpdate) {
                npcId = getArguments().getInt(NPC_ID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_npc_edit, container, false);
        name_tf = (EditText) charEditView.findViewById(R.id.etName);
        info_tf = (EditText) charEditView.findViewById(R.id.etInfo);
        init_tf = (EditText) charEditView.findViewById(R.id.etInit);
        maxHp_tf = (EditText) charEditView.findViewById(R.id.etHpMax);
        ac_tf = (EditText) charEditView.findViewById(R.id.etAc);
        etStrength = (EditText) charEditView.findViewById(R.id.etStrength);
        etDex = (EditText) charEditView.findViewById(R.id.etDex);
        etConst = (EditText) charEditView.findViewById(R.id.etConst);
        etInt  = (EditText) charEditView.findViewById(R.id.etInt);
        etWis = (EditText) charEditView.findViewById(R.id.etWis);
        etChar = (EditText) charEditView.findViewById(R.id.etChar);


        if (modeUpdate) {
            loadNPCInfo(name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar, npcId);
        }
        addchar_btn = (Button) charEditView.findViewById(R.id.bDone);
        addchar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButton(modeUpdate);
            }
        });
        // Inflate the layout for this fragment
        return charEditView;
    }

    private void loadNPCInfo(EditText name, EditText info, EditText init, EditText maxHp, EditText ac, EditText strength, EditText dex, EditText constit, EditText intelligence, EditText wis, EditText charisma, int id) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO,
                DataBaseHandler.KEY_INITIATIVEBONUS,
                DataBaseHandler.KEY_MAXHP,
                DataBaseHandler.KEY_AC,
                DataBaseHandler.KEY_STRENGTH,
                DataBaseHandler.KEY_DEXTERITY,
                DataBaseHandler.KEY_CONSTITUTION,
                DataBaseHandler.KEY_INTELLIGENCE,
                DataBaseHandler.KEY_WISDOM,
                DataBaseHandler.KEY_CHARISMA};
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
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

        }
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
        values.put(DataBaseHandler.KEY_BELONGSTO, encounterId);

        // insert a record
        if (!modeUpdate) {
            getContext();
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_NPC, values);
        }
        // update a record
        else {
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId);
            getContext().getContentResolver().update(uri, values, null, null);
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
}
