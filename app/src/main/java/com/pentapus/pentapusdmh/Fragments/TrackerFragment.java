package com.pentapus.pentapusdmh.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.commonsware.cwac.merge.MergeAdapter;
import com.pentapus.pentapusdmh.TrackerAdapter;
import com.pentapus.pentapusdmh.TrackerInfoCard;
import com.pentapus.pentapusdmh.DataBaseHandler;
import com.pentapus.pentapusdmh.DbContentProvider;
import com.pentapus.pentapusdmh.DiceHelper;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.SharedPrefsHelper;

import java.util.ArrayList;

public class TrackerFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TrackerAdapter chars;
    private static int campaignId, encounterId;
    private MergeAdapter mergeAdapter;
    private ArrayList<String> names;
    private ArrayList<Integer> initiative;



    public TrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SessionTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackerFragment newInstance(String param1, String param2) {
        TrackerFragment fragment = new TrackerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        names = new ArrayList<String>();
        initiative = new ArrayList<Integer>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_tracker, container, false);
        final RecyclerView mRecyclerView = (RecyclerView) tableView.findViewById(R.id.listViewEncounter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        // insert a record
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(SharedPrefsHelper.loadEncounterName(getContext()));

        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        encounterId = SharedPrefsHelper.loadEncounterId(getContext());

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(1, null, this);
        chars = new TrackerAdapter();
        mRecyclerView.setAdapter(chars);
        //Log.d("Name:", names.get(0));

        Button next = (Button) tableView.findViewById(R.id.bNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chars.moveToBottom();
                chars.notifyDataSetChanged();
            }
        });
        // Inflate the layout for this fragment
        return tableView;

    }



    private void createList(ArrayList<String> names, ArrayList<Integer> initiative){
        for(int i=0; i<names.size(); i++){
            initiative.add(i, initiative.get(i));
            TrackerInfoCard ci = new TrackerInfoCard();
            ci.name = names.get(i);
            ci.initiative = String.valueOf(initiative.get(i));
            chars.addListItem(ci);
            chars.notifyItemInserted(i);
        }
    }




    @Override
    public void onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.play_mode).setVisible(false);
        super.onPrepareOptionsMenu(menu);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INITIATIVEBONUS
        };
        if (id == 0) {
            String[] selectionArgs = new String[]{String.valueOf(encounterId)};
            String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
            return new CursorLoader(this.getContext(),
                    DbContentProvider.CONTENT_URI_NPC, projection, selection, selectionArgs, null);
        } else {
            String[] selectionArgs = new String[]{String.valueOf(campaignId)};
            String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
            return new CursorLoader(this.getContext(),
                    DbContentProvider.CONTENT_URI_PC, projection, selection, selectionArgs, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 0:
                while (data.moveToNext()) {
                    String names = data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME));
                    int initiative = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS));
                    int initiativeMod = initiative;
                    initiative = initiative+ DiceHelper.d20();
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = names;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    chars.addListItem(ci);
                    //names.add(data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME)));
                    //initiative.add(data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS)));

                }
                break;
            case 1:
                while (data.moveToNext()) {
                    String names = data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME));
                    int initiative = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS));
                    int initiativeMod = initiative;
                    initiative = initiative + DiceHelper.d20();
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = names;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    chars.addListItem(ci);
                }
                break;
            default:
                break;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chars.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case 0:
                //dataAdapterNPC.swapCursor(null);
                break;
            case 1:
                //dataAdapterPC.swapCursor(null);
                break;
            default:
                break;
        }
    }
}
