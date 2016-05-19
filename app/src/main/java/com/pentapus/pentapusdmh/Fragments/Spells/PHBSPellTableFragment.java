package com.pentapus.pentapusdmh.Fragments.Spells;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.FilterManager;
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterAdapter;
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.R;

import java.util.Observable;
import java.util.Observer;


public class PHBSpellTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback, Observer {


    private static final String MODE = "modeUpdate";


    private RecyclerView mySpellRecyclerView;
    private ActionMode mActionMode;

    private PHBSpellAdapter mySpellAdapter;

    public PHBSpellTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static PHBSpellTableFragment newInstance() {
        PHBSpellTableFragment fragment = new PHBSpellTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (this.getArguments() != null) {
        }
        mySpellAdapter = new PHBSpellAdapter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_monster_table, container, false);
        // insert a record

        mySpellRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mySpellRecyclerView.setLayoutManager(linearLayoutManager);
        mySpellRecyclerView.setHasFixedSize(true);
        mySpellRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        mySpellRecyclerView.setAdapter(mySpellAdapter);
        // Inflate the layout for this fragment
        return tableView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, null, this);

        } else {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection;
        String[] selectionArgs;

        if (args != null) {
            selectionArgs = new String[]{"%" + String.valueOf(1) + "%", "%" + args.getString("filter") + "%"};
            String selection1 = DataBaseHandler.KEY_PHB;
            String selection2 = DataBaseHandler.KEY_NAME;
            selection = selection1 + " LIKE ? AND " + selection2 + " LIKE ?";
        } else {
            selectionArgs = new String[]{String.valueOf(1)};
            selection = DataBaseHandler.KEY_PHB + " = ?";

        }
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_SPELL, DataBaseHandler.PROJECTION_SPELL, selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mySpellAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mySpellAdapter.swapCursor(null);
    }


    @Override
    public void onItemClick(int position) {

        //TODO: Select on itemclick
       /* Cursor cursor = myMonsterAdapter.getCursor();
        cursor.moveToPosition(position);
        int encounterId =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        String encounterName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));

        Bundle bundle = new Bundle();
        bundle.putInt(ENCOUNTER_ID, encounterId);
        bundle.putString(ENCOUNTER_NAME, encounterName);
        loadNPC(bundle, encounterId, encounterName); */

    }

    @Override
    public void onItemLongCLick(final int position) {
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mySpellRecyclerView.getAdapter().notifyItemChanged(position);
                String title = "Selected: " + String.valueOf(position);
                mode.setTitle(title);
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        /*
                        //TODO: ADD DIALOG TO MAKE SURE THE DELETE IS INTENTIONAL
                        Cursor cursor = mySpellAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int spellId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SPELL + "/" + spellId);
                        getContext().getContentResolver().delete(uri, null, null);
                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard.hasPrimaryClip()) {
                            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
                            Uri pasteUri = itemPaste.getUri();
                            if (pasteUri == null) {
                                pasteUri = Uri.parse(String.valueOf(itemPaste.getText()));
                            }
                            if(pasteUri.equals(uri)){
                                Uri newUri = Uri.parse("");
                                ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", newUri);
                                clipboard.setPrimaryClip(clip);
                                getActivity().invalidateOptionsMenu();
                            }
                        }
                        mode.finish();
                        return true; */
                    case R.id.edit:
                       /* cursor = myMonsterAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int encounterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(MODE, true);
                        bundle.putInt(ENCOUNTER_ID, encounterId);
                        bundle.putInt(SESSION_ID, sessionId);
                        editEncounter(bundle);
                        mode.finish();
                        return true;*/
                    case R.id.copy:
                       /* cursor = mEncounterAdapter.getCursor();
                        cursor.moveToPosition(position);
                        encounterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + encounterId);
                        clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", uri);
                        clipboard.setPrimaryClip(clip);
                        getActivity().invalidateOptionsMenu();
                        mode.finish();
                        return true; */
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                EncounterAdapter.setSelectedPos(-1);
                mySpellRecyclerView.getAdapter().notifyItemChanged(position);
                mActionMode = null;
            }
        });
    }

    public void filterData(String filterArgs) {
        Bundle bundle = new Bundle();
        bundle.putString("filter", filterArgs);
        if(isAdded()){
            getLoaderManager().restartLoader(0, bundle, this);
        }
    }

    public PHBSpellAdapter getAdapter() {
        return mySpellAdapter;
    }

    @Override
    public void onMenuRefresh() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof FilterManager) {
            String filter = ((FilterManager) observable).getQuery();
            filterData(filter);
        }
    }
}
