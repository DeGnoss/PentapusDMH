package com.pentapus.pentapusdmh.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.pentapus.pentapusdmh.DataBaseHandler;
import com.pentapus.pentapusdmh.DbContentProvider;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.SharedPrefsHelper;

public class TrackerFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SimpleCursorAdapter dataAdapterNPC, dataAdapterPC;
    private static int campaignId, encounterId;
    private MergeAdapter mergeAdapter;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_tracker, container, false);
        // insert a record
        campaignId = SharedPrefsHelper.loadCampaign(getContext());
        encounterId = SharedPrefsHelper.loadEncounter(getContext());
        Button next = (Button) tableView.findViewById(R.id.bNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        displayListView(tableView);
        // Inflate the layout for this fragment
        return tableView;

    }

    private void displayListView(View view) {

        mergeAdapter = new MergeAdapter();

        String[] columns = new String[]{
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INITIATIVEBONUS
        };

        int[] to = new int[]{
                R.id.name,
                R.id.initiative,
        };

        dataAdapterNPC = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.char_info,
                null,
                columns,
                to,
                0);

        dataAdapterPC = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.char_info,
                null,
                columns,
                to,
                0);

        Log.d("Testaufruf", "woooooot");

        dataAdapterNPC.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                Log.d("ColumnIndex:", String.valueOf(columnIndex));
                if(columnIndex == 2){
                    int init = cursor.getInt(columnIndex);
                    int rand = (int) Math.round((20*Math.random())+1);
                    Log.d("Random1", "init:" + String.valueOf(init) + "  rand:" + String.valueOf(rand));
                    init = (init + rand);
                    String s = String.valueOf(init);
                    TextView tv = (TextView) view;
                    tv.setText(s);
                    return true;
                }
                return false;
            }
        });
/*
        dataAdapterPC.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex == 2){
                    int init = cursor.getInt(columnIndex);
                    int rand = (int) Math.round((20 * Math.random())+1);
                    Log.d("Random2", "init:" + String.valueOf(init) + "  rand:" + String.valueOf(rand));
                    init = (init + rand);
                    String s = String.valueOf(init);
                    TextView tv = (TextView) view;
                    tv.setText(s);
                    return true;
                }
                return false;
            }
        }); */

        mergeAdapter.addAdapter(dataAdapterNPC);
        mergeAdapter.addAdapter(dataAdapterPC);


        final ListView listView = (ListView) view.findViewById(R.id.listViewEncounter);
        listView.setAdapter(mergeAdapter);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(1, null, this);
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
                dataAdapterNPC.swapCursor(data);
                break;
            case 1:
                dataAdapterPC.swapCursor(data);
                break;
            default:
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case 0:
                dataAdapterNPC.swapCursor(null);
                break;
            case 1:
                dataAdapterPC.swapCursor(null);
                break;
            default:
                break;
        }
    }
}
