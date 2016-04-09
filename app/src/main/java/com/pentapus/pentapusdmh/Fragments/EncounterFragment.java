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
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;

public class EncounterFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ENCOUNTER_ID = "encounterId";
    private static final String ENCOUNTER_NAME = "encounterName";

    private static final String MODE = "modeUpdate";
    private static final String NPC_ID = "npcId";


    private int encounterId;
    private String encounterName;

    private SimpleCursorAdapter dataAdapterNPC, dataAdapterPC;
    private static int campaignId;
    private MergeAdapter mergeAdapter;
    private FloatingActionButton fab;
    final CharSequence[] items = {"Edit", "Delete", "Copy"};

    public EncounterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param encounterName Parameter 1.
     * @param encounterId Parameter 2.
     * @return A new instance of fragment SessionTableFragment.
     */
    public static EncounterFragment newInstance(String encounterName, int encounterId) {
        EncounterFragment fragment = new EncounterFragment();
        Bundle args = new Bundle();
        args.putString(ENCOUNTER_NAME, encounterName);
        args.putInt(ENCOUNTER_ID, encounterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        if (getArguments() != null) {
            encounterId = getArguments().getInt("encounterId");
            encounterName = getArguments().getString("encounterName");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_encounter, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(encounterName + " Preparation");
        displayListView(tableView);
        fab = (FloatingActionButton) tableView.findViewById(R.id.fabEncounter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putBoolean(MODE, false);
                bundle.putInt(ENCOUNTER_ID, encounterId);
                addNPC(bundle);
            }
        });
        // Inflate the layout for this fragment
        return tableView;

    }

    private void displayListView(View view) {

        mergeAdapter = new MergeAdapter();

        String[] columns = new String[]{
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO
        };

        int[] to = new int[]{
                R.id.name,
                R.id.info,
        };

        dataAdapterNPC = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.encounter_info,
                null,
                columns,
                to,
                0);

        dataAdapterPC = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.encounter_info,
                null,
                columns,
                to,
                0);

        mergeAdapter.addAdapter(dataAdapterNPC);
        mergeAdapter.addAdapter(dataAdapterPC);


        final ListView listView = (ListView) view.findViewById(R.id.listViewEncounter);
        listView.setAdapter(mergeAdapter);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(1, null, this);



        //TODO add cases to keep from crashing
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
                                    listView.getAdapter();
                                    Log.d("Adapter clicked:", listView.getAdapter().getItem(position).toString());
                                    int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(MODE, true);
                                    bundle.putInt(NPC_ID, npcId);
                                    bundle.putInt(ENCOUNTER_ID, encounterId);
                                    editNPC(bundle);
                                    dialog.dismiss();
                                } else if (item == 1) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + id);
                                    getContext().getContentResolver().delete(uri, null, null);
                                    dialog.dismiss();
                                }else if (item == 2) {
                                        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + id);
                                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                        clipboard.setPrimaryClip(ClipData.newUri(getContext().getContentResolver(), "NPC", uri));
                                        dialog.dismiss();
                                    } else

                                    {
                                        dialog.dismiss();
                                    }
                                }
            }

            ).

            show();

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

    public int getEncounterId() {
        return encounterId;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.play_mode).setVisible(true);
        super.onPrepareOptionsMenu(menu);
        ClipboardManager clipboard = (ClipboardManager)
                getContext().getSystemService(Context.CLIPBOARD_SERVICE);


        MenuItem mPasteItem = menu.findItem(R.id.menu_paste);

        // If the clipboard contains an item, enables the Paste option on the menu.
        if (clipboard.hasPrimaryClip()) {
            mPasteItem.setVisible(true);
        } else {
            // If the clipboard is empty, disables the menu's Paste option.
            mPasteItem.setVisible(false);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO
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
