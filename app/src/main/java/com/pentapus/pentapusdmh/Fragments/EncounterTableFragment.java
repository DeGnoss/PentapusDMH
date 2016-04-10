package com.pentapus.pentapusdmh.Fragments;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;


public class EncounterTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SESSION_NAME = "sessionName";
    private static final String SESSION_ID = "sessionId";

    private static final String ENCOUNTER_NAME = "encounterName";
    private static final String ENCOUNTER_ID = "encounterId";

    private static final String MODE = "modeUpdate";


    private int sessionId;
    private String sessionName;

    private SimpleCursorAdapter dataAdapterEncounters;
    private FloatingActionButton fab;
    final CharSequence[] items = {"Edit", "Delete", "Copy"};

    public EncounterTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sessionName Parameter 1.
     * @param sessionId   Parameter 2.
     * @return A new instance of fragment SessionTableFragment.
     */
    public static EncounterTableFragment newInstance(String sessionName, int sessionId) {
        EncounterTableFragment fragment = new EncounterTableFragment();
        Bundle args = new Bundle();
        args.putString(SESSION_NAME, sessionName);
        args.putInt(SESSION_ID, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (this.getArguments() != null) {
            sessionId = getArguments().getInt(SESSION_ID);
            sessionName = getArguments().getString(SESSION_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_encounter_table, container, false);
        // insert a record

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(sessionName + " Encounters");
        displayListView(tableView);
        fab = (FloatingActionButton) tableView.findViewById(R.id.fabEncounter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putBoolean(MODE, false);
                bundle.putInt(SESSION_ID, sessionId);
                addEncounter(bundle);
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

        dataAdapterEncounters = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.encounter_info,
                null,
                columns,
                to,
                0);

        final ListView listView = (ListView) view.findViewById(R.id.listViewEncounters);
        listView.setAdapter(dataAdapterEncounters);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                int encounterId =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                String encounterName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));

                Bundle bundle = new Bundle();
                bundle.putInt(ENCOUNTER_ID, encounterId);
                bundle.putString(ENCOUNTER_NAME, encounterName);
                loadNPC(bundle, encounterId, encounterName);
            }
        });

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
                                    int encounterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(MODE, true);
                                    bundle.putInt(ENCOUNTER_ID, encounterId);
                                    bundle.putInt(SESSION_ID, sessionId);
                                    editEncounter(bundle);
                                    dialog.dismiss();
                                } else if (item == 1) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + id);
                                    getContext().getContentResolver().delete(uri, null, null);
                                    dialog.dismiss();
                                }else if (item == 2) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + id);
                                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboard.setPrimaryClip(ClipData.newUri(getContext().getContentResolver(), "ENCOUNTER", uri));
                                    getActivity().invalidateOptionsMenu();
                                    dialog.dismiss();
                                }else {
                                    dialog.dismiss();
                                }
                            }
                        }).show();
                return true;
            }
        });
    }

    private void addEncounter(Bundle bundle) {
        Fragment fragment;
        fragment = new EncounterEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_ENCOUNTER")
                .addToBackStack("FE_ENCOUNTER")
                .commit();
    }

    private void editEncounter(Bundle bundle) {
        Fragment fragment;
        fragment = new EncounterEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_ENCOUNTER")
                .addToBackStack("FE_ENCOUNTER")
                .commit();
    }

    private void loadNPC(Bundle bundle, int encounterId, String encounterName) {
        SharedPrefsHelper.saveEncounter(getContext(), encounterId, encounterName);
        Fragment fragment;
        fragment = new EncounterFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "F_ENCOUNTER")
                .addToBackStack("F_ENCOUNTER")
                .commit();
    }

    public int getSessionId() {
        return sessionId;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.campaign_settings).setVisible(true);

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        if(clipboard.hasPrimaryClip()){
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            String pasteString = String.valueOf(itemPaste.getText());
            if(pasteUri == null){
                pasteUri = Uri.parse(pasteString);
            }
            if(pasteUri != null){
                if(DbContentProvider.ENCOUNTER.equals(getContext().getContentResolver().getType(pasteUri))){
                    menu.findItem(R.id.menu_paste).setVisible(true);
                }
            }
        }
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
                DataBaseHandler.KEY_INFO
        };
        String[] selectionArgs = new String[]{String.valueOf(sessionId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_ENCOUNTER, projection, selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapterEncounters.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapterEncounters.swapCursor(null);
    }


}
