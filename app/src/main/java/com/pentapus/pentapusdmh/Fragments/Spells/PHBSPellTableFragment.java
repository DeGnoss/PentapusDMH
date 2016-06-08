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
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.FilterManager;
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterAdapter;
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterEditFragment;
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class PHBSpellTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback, Observer {



    private static final String SPELL_ID = "spellId";
    private static final String SPELL_NAME = "spellName";
    private String sourceType;
    private Bundle filters;


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
    public static PHBSpellTableFragment newInstance(String sourceType) {
        PHBSpellTableFragment fragment = new PHBSpellTableFragment();
        Bundle args = new Bundle();
        args.putString("sourcetype", sourceType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (this.getArguments() != null) {
            sourceType = this.getArguments().getString("sourcetype");
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
        filters = new Bundle();
        if(SharedPrefsHelper.loadPHBFilter(getContext()))
            filters.putBoolean("phb", SharedPrefsHelper.loadPHBFilter(getContext()));
        if(SharedPrefsHelper.loadEEFilter(getContext()))
            filters.putBoolean("ee", SharedPrefsHelper.loadEEFilter(getContext()));
        if(SharedPrefsHelper.loadSCAGFilter(getContext()))
            filters.putBoolean("scag", SharedPrefsHelper.loadSCAGFilter(getContext()));
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, filters, this);
        } else {
            getLoaderManager().restartLoader(0, filters, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "";
        String[] selectionArgs;
        List<String> selectionList = new ArrayList<>();
        int size = 0;
        if (args != null) {

            if(args.getBoolean("phb")){
                selectionList.add("%PHB%");
                size++;
            }
            if(args.getBoolean("ee")){
                selectionList.add("%EE%");
                size++;
            }

            if(args.getBoolean("scag")){
                selectionList.add("%SCAG%");
                size++;
            }


            if(args.getString("filter") != null){
                selectionList.add(args.getString("filter"));
            }

            selectionArgs = new String[ selectionList.size() ];
            selectionList.toArray( selectionArgs );

            for (int i = 0; i<size ;i++) {
                if (!selection.isEmpty()) {
                    selection = selection + " OR ";
                    selection = selection + DataBaseHandler.KEY_SOURCE + " LIKE ?";
                } else {
                    selection = "(" + DataBaseHandler.KEY_SOURCE + " LIKE ?";
                }
            }
            if(args.getString("filter") != null){
                if(!selection.isEmpty()){
                    selection = selection + ")" + " AND " + DataBaseHandler.KEY_NAME + " LIKE ?";
                }else{
                    selection = DataBaseHandler.KEY_NAME + " LIKE ?";
                }
            }else{
                selection = selection + ")";
            }
        } else {
            //selectionArgs = new String[]{"%" + "PHB" + "%", "%" + "EE" + "%", "%" + "PHB" + "%"};
            selectionArgs = null;
            selection = null;
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
        Cursor cursor = mySpellAdapter.getCursor();
        cursor.moveToPosition(position);
        int spellId =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        String spellName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));

        Bundle bundle = new Bundle();
        bundle.putInt(SPELL_ID, spellId);
        bundle.putString(SPELL_NAME, spellName);
        loadSpell(bundle);
    }

    public void loadSpell(Bundle bundle){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Fragment fragment;
        fragment = new DetailSpellFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerFrame, fragment, "FD_SPELL")
                .addToBackStack("FD_SPELL")
                .commit();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        super.onPrepareOptionsMenu(menu);
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
        String filterFormatted = "%" + filterArgs + "%";
        filters.putString("filter", filterFormatted);
        if(isAdded()){
            getLoaderManager().restartLoader(0, filters, this);
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
