package com.pentapus.pentapusdmh.Fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DataBaseHandler;
import com.pentapus.pentapusdmh.DbContentProvider;
import com.pentapus.pentapusdmh.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EncounterEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EncounterEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncounterEditFragment extends Fragment {

    Button addchar_btn;
    EditText name_tf, info_tf;
    private String mode, id;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EncounterEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EncounterEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EncounterEditFragment newInstance(String param1, String param2) {
        EncounterEditFragment fragment = new EncounterEditFragment();
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
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_encounter_edit, container, false);
        name_tf = (EditText) charEditView.findViewById(R.id.etName);
        info_tf = (EditText) charEditView.findViewById(R.id.etInfo);

        if (this.getArguments() != null){
            mode = getArguments().getString("mode");
            //check wheter entry gets updated or added
            if (mode.trim().equalsIgnoreCase("update")){
                loadCharacterInfo(name_tf, info_tf);
            }
        }
        addchar_btn = (Button) charEditView.findViewById(R.id.bDone);
        addchar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charButton(mode);
            }
        });
        // Inflate the layout for this fragment
        return charEditView;

    }

    private void loadCharacterInfo(EditText name, EditText info) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO};
        id = getArguments().getString("rowId");
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

    public void charButton(String mode) {
        // get values from the input text fields
        String myName = name_tf.getText().toString();
        String myInitiative = info_tf.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, myName);
        values.put(DataBaseHandler.KEY_INFO, myInitiative);

        // insert a record
        if(mode.trim().equalsIgnoreCase("add")){
            getContext();
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_ENCOUNTER, values);
        }
        // update a record
        else {
            id = getArguments().getString("rowId");
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + id);
            getContext().getContentResolver().update(uri, values, null, null);
        }
        Bundle bundle = new Bundle();
        bundle.putString("type", "npc");
        mListener.charDone(bundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (OnFragmentInteractionListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void charDone(Bundle bundle);
    }
}
