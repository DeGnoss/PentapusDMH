package com.pentapus.pentapusdmh.Fragments.Spells;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.Utils;


public class DetailSpellFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String SPELL_ID = "spellId";
    private static final String SPELL_NAME = "spellName";

    private int spellId;
    private String spellName;

    TextView name_tf, info_tf, subhead_tf, castingtime_tf, range_tf, components_tf, duration_tf;

    public DetailSpellFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modeUpdate Parameter 1.
     * @param spellId    Parameter 2.
     * @return A new instance of fragment EncounterEditFragment.
     */
    public static DetailSpellFragment newInstance(boolean modeUpdate, int spellId) {
        DetailSpellFragment fragment = new DetailSpellFragment();
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
                spellId = getArguments().getInt(SPELL_ID);
                spellName = getArguments().getString(SPELL_NAME);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(spellName);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(spellName);
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_detail_spell, container, false);
        //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) charEditView.getLayoutParams();
        //params.setMargins(0, Utils.getStatusBarHeight(getActivity()), 0, 0);
        charEditView.setBackgroundColor(Color.WHITE);
        name_tf = (TextView) charEditView.findViewById(R.id.spellName);
        info_tf = (TextView) charEditView.findViewById(R.id.spellInfo);
        subhead_tf = (TextView) charEditView.findViewById(R.id.spellSubName);
        castingtime_tf = (TextView) charEditView.findViewById(R.id.spellCastingTime);
        range_tf = (TextView) charEditView.findViewById(R.id.spellRange);
        components_tf = (TextView) charEditView.findViewById(R.id.spellComponents);
        duration_tf = (TextView) charEditView.findViewById(R.id.spellDuration);



        loadSpellInfo(name_tf, info_tf, subhead_tf, castingtime_tf, range_tf, components_tf, duration_tf, spellId);

        // Inflate the layout for this fragment
        return charEditView;
    }


    private void loadSpellInfo(TextView name, TextView info, TextView subhead, TextView castingtime, TextView range, TextView components, TextView duration, int id) {

        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SPELL + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_SPELL, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            String myInfo = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
            String mySubhead = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SCHOOL));
            String myCastingTime = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TIME));
            String myRange = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_RANGE));
            //String myComponents = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
            String myDuration = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DURATION));


            name.setText(myName);
            info.setText(myInfo);
            subhead.setText(mySubhead);
            castingtime.setText(myCastingTime);
            range.setText(myRange);
            duration.setText(myDuration);
        }
        assert cursor != null;
        cursor.close();
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
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().invalidateOptionsMenu();
        ((MainActivity)getActivity()).setFabVisibility(false);
        ((MainActivity)getActivity()).disableNavigationDrawer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
