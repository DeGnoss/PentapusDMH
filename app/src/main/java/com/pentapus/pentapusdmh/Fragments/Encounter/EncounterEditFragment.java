package com.pentapus.pentapusdmh.Fragments.Encounter;

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

import com.pentapus.pentapusdmh.BaseFragment;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;

public class EncounterEditFragment extends BaseFragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String ENCOUNTER_ID = "encounterId";
    private static final String SESSION_ID = "sessionId";

    private boolean modeUpdate;
    private int encounterId;
    private int sessionId;

    Button addchar_btn;
    EditText name_tf, info_tf;


    public EncounterEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode        Parameter 1.
     * @param encounterId Parameter 2.
     * @param sessionId   Parameter 3.
     * @return A new instance of fragment EncounterEditFragment.
     */
    public static EncounterEditFragment newInstance(boolean mode, int encounterId, int sessionId) {
        EncounterEditFragment fragment = new EncounterEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(MODE, mode);
        args.putInt(ENCOUNTER_ID, encounterId);
        args.putInt(SESSION_ID, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modeUpdate = getArguments().getBoolean(MODE);
            sessionId = getArguments().getInt(SESSION_ID);
            if(modeUpdate){
                encounterId = getArguments().getInt(ENCOUNTER_ID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setFabVisibility(false);
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_encounter_edit, container, false);
        name_tf = (EditText) charEditView.findViewById(R.id.etName);
        info_tf = (EditText) charEditView.findViewById(R.id.etInfo);

        //check wheter entry gets updated or added
        if (modeUpdate) {
            loadEncounterInfo(name_tf, info_tf, encounterId);
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

    private void loadEncounterInfo(EditText name, EditText info, int id) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO};
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            String myInitiative = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
            name.setText(myName, TextView.BufferType.EDITABLE);
            info.setText(myInitiative, TextView.BufferType.EDITABLE);
        }
    }

    public void doneButton(boolean mode) {
        // get values from the input text fields
        String myName = name_tf.getText().toString();
        String myInitiative = info_tf.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, myName);
        values.put(DataBaseHandler.KEY_INFO, myInitiative);
        values.put(DataBaseHandler.KEY_BELONGSTO, sessionId);

        // insert a record
        if (!mode) {
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_ENCOUNTER, values);
        }
        // update a record
        else {
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + encounterId);
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
