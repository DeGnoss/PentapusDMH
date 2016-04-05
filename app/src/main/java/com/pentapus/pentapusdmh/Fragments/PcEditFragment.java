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


public class PcEditFragment extends Fragment {

    Button addchar_btn;
    EditText name_tf, info_tf, init_tf;
    private String mode, id;
    private int campaignId;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PcEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SessionEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PcEditFragment newInstance(String param1, String param2) {
        PcEditFragment fragment = new PcEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_pc_edit, container, false);
        name_tf = (EditText) charEditView.findViewById(R.id.etName);
        info_tf = (EditText) charEditView.findViewById(R.id.etInfo);
        init_tf = (EditText) charEditView.findViewById(R.id.etInit);

        if (this.getArguments() != null) {
            mode = getArguments().getString("mode");
            campaignId = Integer.parseInt(getArguments().getString("campaignId"));
            //check wheter entry gets updated or added
            if (mode.trim().equalsIgnoreCase("update")) {
                id = getArguments().getString("pcId");
                loadCharacterInfo(name_tf, info_tf, init_tf, id);
            }
        }
        addchar_btn = (Button) charEditView.findViewById(R.id.bDone);
        addchar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButton(mode);
            }
        });
        // Inflate the layout for this fragment
        return charEditView;

    }

    private void loadCharacterInfo(EditText name, EditText info, EditText init, String id) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO,
                DataBaseHandler.KEY_INITIATIVEBONUS};
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            String myInitiative = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS));
            String myInfo = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
            name.setText(myName, TextView.BufferType.EDITABLE);
            info.setText(myInfo, TextView.BufferType.EDITABLE);
            init.setText(myInitiative, TextView.BufferType.EDITABLE);
        }
    }

    public void doneButton(String mode) {
        // get values from the input text fields
        String myName = name_tf.getText().toString();
        String myInitiative = init_tf.getText().toString();
        String myInfo = info_tf.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, myName);
        values.put(DataBaseHandler.KEY_INFO, myInfo);
        values.put(DataBaseHandler.KEY_INITIATIVEBONUS, myInitiative);
        values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);

        // insert a record
        if (mode.trim().equalsIgnoreCase("add")) {
            getContext();
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_PC, values);
        }
        // update a record
        else {
            id = getArguments().getString("pcId");
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + id);
            getContext().getContentResolver().update(uri, values, null, null);
        }
        Bundle bundle = new Bundle();
        bundle.putString("type", "pc");
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
