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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pentapus.pentapusdmh.DataBaseHandler;
import com.pentapus.pentapusdmh.DbContentProvider;
import com.pentapus.pentapusdmh.R;

public class EncounterFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private String encounterId;
    private FloatingActionButton fab;
    final CharSequence[] items = {"Edit", "Delete"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SimpleCursorAdapter dataAdapterEncounter;

    public EncounterFragment() {
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
    public static EncounterFragment newInstance(String param1, String param2) {
        EncounterFragment fragment = new EncounterFragment();
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
        final View tableView = inflater.inflate(R.layout.fragment_encounter, container, false);
        // insert a record
        if (this.getArguments() != null) {
            encounterId = getArguments().getString("encounterId");
        }
        displayListView(tableView);
        fab = (FloatingActionButton) tableView.findViewById(R.id.fabEncounter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("mode", "add");
                bundle.putString("encounterId", encounterId);
                addNPC(bundle);
            }
        });
        // Inflate the layout for this fragment
        return tableView;

    }

    private void displayListView(View view) {

        String[] columns = new String[]{
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO
        };

        int[] to = new int[]{
                R.id.name,
                R.id.info,
        };

        dataAdapterEncounter = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.encounter_info,
                null,
                columns,
                to,
                0);

        final ListView listView = (ListView) view.findViewById(R.id.listViewEncounter);
        listView.setAdapter(dataAdapterEncounter);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
                new AlertDialog.Builder(getContext()).setTitle(title)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                                    String npcId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Bundle bundle = new Bundle();
                                    bundle.putString("mode", "update");
                                    bundle.putString("npcId", npcId);
                                    bundle.putString("encounterId", encounterId);
                                    editNPC(bundle);
                                    dialog.dismiss();
                                } else if (item == 1) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + id);
                                    getContext().getContentResolver().delete(uri, null, null);
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        }).show();
                return true;
            }
        });
    }

    private void addNPC(Bundle bundle) {
        Fragment fragment;
        fragment = new NPCEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_NPC")
                .addToBackStack("FE_NPC")
                .commit();
    }

    private void editNPC(Bundle bundle) {
        Fragment fragment;
        fragment = new NPCEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_NPC")
                .addToBackStack("FE_NPC")
                .commit();
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
                DataBaseHandler.KEY_INFO
        };
        String[] selectionArgs = new String[]{encounterId};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_NPC, projection, selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapterEncounter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapterEncounter.swapCursor(null);
    }
}
