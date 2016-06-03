package com.pentapus.pentapusdmh.Fragments.Campaign;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class CampaignEditFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String CAMPAIGN_ID = "campaignId";

    private boolean modeUpdate;
    private int campaignId;

    Button addchar_btn;
    EditText name_tf, info_tf;

    public CampaignEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modeUpdate Parameter 1.
     * @param campaignId Parameter 2.
     * @return A new instance of fragment SessionEditFragment.
     */
    public static CampaignEditFragment newInstance(boolean modeUpdate, int campaignId) {
        CampaignEditFragment fragment = new CampaignEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(MODE, modeUpdate);
        args.putInt(CAMPAIGN_ID, campaignId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modeUpdate = getArguments().getBoolean(MODE);
            if (modeUpdate) {
                campaignId = getArguments().getInt(CAMPAIGN_ID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_campaign_edit, container, false);
        name_tf = (EditText) charEditView.findViewById(R.id.etName);
        info_tf = (EditText) charEditView.findViewById(R.id.etInfo);


        if (modeUpdate) {
            loadCampaignInfo(name_tf, info_tf, campaignId);
        }
        // Inflate the layout for this fragment
        return charEditView;

    }

    public void onFabClick() {
        // get values from the input text fields
        String myName = name_tf.getText().toString();
        String myInitiative = info_tf.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, myName);
        values.put(DataBaseHandler.KEY_INFO, myInitiative);

        // insert a record
        if (!modeUpdate) {
            Uri campaign;
            campaign = getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_CAMPAIGN, values);
            String campaignStr = campaign.getPath();
            campaignId = Integer.parseInt(campaignStr.substring(campaignStr.lastIndexOf('/') + 1));

        }
        // update a record
        else {
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_CAMPAIGN + "/" + campaignId);
            getContext().getContentResolver().update(uri, values, null, null);
        }
        SharedPrefsHelper.saveCampaign(getContext(), campaignId, myName);
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

    private void loadCampaignInfo(EditText name, EditText info, int id) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO};
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_CAMPAIGN + "/" + id);
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
        ((MainActivity)getActivity()).setFabIcon(false);
        ((MainActivity)getActivity()).disableNavigationDrawer();
    }
}
