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
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.R;

public class PcTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CAMPAIGN_ID = "campaignId";
    private static final String CAMPAIGN_NAME = "campaignName";

    private static final String MODE = "modeUpdate";

    private int campaignId;
    private String campaignName;

    private SimpleCursorAdapter dataAdapterPc;
    private FloatingActionButton fab;
    final CharSequence[] items = {"Edit", "Delete"};

    public PcTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param campaignId   Parameter 1.
     * @param campaignName Parameter 2.
     * @return A new instance of fragment SessionTableFragment.
     */
    public static PcTableFragment newInstance(int campaignId, String campaignName) {
        PcTableFragment fragment = new PcTableFragment();
        Bundle args = new Bundle();
        args.putInt(CAMPAIGN_ID, campaignId);
        args.putString(CAMPAIGN_NAME, campaignName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            campaignId = getArguments().getInt(CAMPAIGN_ID);
            campaignName = getArguments().getString(CAMPAIGN_NAME);
        } else {
            campaignId = SharedPrefsHelper.loadCampaignId(getContext());
            campaignName = SharedPrefsHelper.loadCampaignName(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_pc_table, container, false);
        // insert a record
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(campaignName + " Player Characters");
        displayListView(tableView);
        fab = (FloatingActionButton) tableView.findViewById(R.id.fabPc);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putBoolean(MODE, false);
                bundle.putInt(CAMPAIGN_ID, campaignId);
                addPC(bundle);
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

        dataAdapterPc = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.encounter_info,
                null,
                columns,
                to,
                0);

        final ListView listView = (ListView) view.findViewById(R.id.listViewPc);
        listView.setAdapter(dataAdapterPc);
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
                                    int pcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(MODE, true);
                                    bundle.putInt("pcId", pcId);
                                    bundle.putInt(CAMPAIGN_ID, campaignId);
                                    editPC(bundle);
                                    dialog.dismiss();
                                } else if (item == 1) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + id);
                                    getContext().getContentResolver().delete(uri, null, null);
                                    dialog.dismiss();
                                } else if (item == 2) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + id);
                                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboard.setPrimaryClip(ClipData.newUri(getContext().getContentResolver(), "PC", uri));
                                    getActivity().invalidateOptionsMenu();
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

    private void addPC(Bundle bundle) {
        Fragment fragment;
        fragment = new PcEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_PC")
                .addToBackStack("FE_PC")
                .commit();
    }

    private void editPC(Bundle bundle) {
        Fragment fragment;
        fragment = new PcEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_PC")
                .addToBackStack("FE_PC")
                .commit();
    }

    public int getCampaignId() {
        return campaignId;
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
                if(DbContentProvider.PC.equals(getContext().getContentResolver().getType(pasteUri))){
                    menu.findItem(R.id.menu_paste).setVisible(true);
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO
        };
        String[] selectionArgs = new String[]{String.valueOf(campaignId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_PC, projection, selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapterPc.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapterPc.swapCursor(null);
    }
}
