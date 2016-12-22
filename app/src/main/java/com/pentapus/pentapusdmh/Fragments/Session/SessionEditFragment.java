package com.pentapus.pentapusdmh.Fragments.Session;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;

public class SessionEditFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String SESSION_ID = "sessionId";

    private boolean modeUpdate;
    private int sessionId;
    FloatingActionButton fab;


    Button addchar_btn;
    EditText name_tf, info_tf;
    private int campaignId;

    public SessionEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modeUpdate Parameter 1.
     * @param sessionId  Parameter 2.
     * @return A new instance of fragment SessionEditFragment.
     */
    public static SessionEditFragment newInstance(boolean modeUpdate, int sessionId) {
        SessionEditFragment fragment = new SessionEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(MODE, modeUpdate);
        args.putInt(SESSION_ID, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        if (this.getArguments() != null) {
            modeUpdate = getArguments().getBoolean(MODE);
            //check wheter entry gets updated or added
            if (modeUpdate) {
                sessionId = getArguments().getInt(SESSION_ID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_session_edit, container, false);
        name_tf = (EditText) charEditView.findViewById(R.id.etName);
        info_tf = (EditText) charEditView.findViewById(R.id.etInfo);
        fab = (FloatingActionButton) charEditView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClick();
            }
        });

        if (modeUpdate) {
            loadSessionInfo(name_tf, info_tf, sessionId);
        }
       /* addchar_btn = (Button) charEditView.findViewById(R.id.bDone);
        addchar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButton(modeUpdate);
            }
        }); */
        // Inflate the layout for this fragment
        return charEditView;

    }

    public void onFabClick(){
        // get values from the input text fields
        String myName = name_tf.getText().toString();
        String myInitiative = info_tf.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, myName);
        values.put(DataBaseHandler.KEY_INFO, myInitiative);
        values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);

        // insert a record
        if (!modeUpdate) {
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_SESSION, values);
        }
        // update a record
        else {
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SESSION + "/" + sessionId);
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
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }


    private void loadSessionInfo(EditText name, EditText info, int id) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO};
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SESSION + "/" + id);
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
        values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);

        // insert a record
        if (!mode) {
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_SESSION, values);
        }
        // update a record
        else {
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SESSION + "/" + sessionId);
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
        //((MainActivity)getActivity()).setFabIcon(false);
    }
}
