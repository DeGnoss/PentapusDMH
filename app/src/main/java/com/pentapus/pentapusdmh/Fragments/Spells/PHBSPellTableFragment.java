package com.pentapus.pentapusdmh.Fragments.Spells;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterAdapter;
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PHBSpellTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback {



    private static final String SPELL_ID = "spellId";
    private static final String SPELL_NAME = "spellName";
    private String sourceType, level, scclass;
    private Bundle filters;
    private SearchView searchView;
    private String searchViewQuery;
    private int mode;
    private ArrayList<String> spellsKnown = new ArrayList<String>();
    private Bundle bundle = new Bundle();


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
    public static PHBSpellTableFragment newInstance(String sourceType, int mode, Bundle bundle, String level, String scclass) {
        PHBSpellTableFragment fragment = new PHBSpellTableFragment();
        Bundle args = new Bundle();
        args.putString("sourcetype", sourceType);
        args.putInt("mode", mode);
        args.putBundle("bundle", bundle);
        args.putString("level", level);
        args.putString("scclass", scclass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (this.getArguments() != null) {
            sourceType = this.getArguments().getString("sourcetype");
            mode = this.getArguments().getInt("mode");
            level = this.getArguments().getString("level");
            scclass = this.getArguments().getString("scclass");
            bundle = this.getArguments().getBundle("bundle");
        }
        if(savedInstanceState != null){
            searchViewQuery = savedInstanceState.getString("sv1");
        }
        mySpellAdapter = new PHBSpellAdapter(getContext(), this, mode, bundle);
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
        if(level != null && !level.isEmpty()){
            filters.putString("level", level);
        }
        if(scclass != null && !scclass.isEmpty()){
            filters.putString("scclass", scclass);
        }
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, filters, this);
        } else {
            getLoaderManager().restartLoader(0, filters, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "";
        boolean paranthesis = false;
        String[] selectionArgs;
        List<String> selectionList = new ArrayList<>();
        int size = 0;
        if (args != null) {

            selectionList.add("%PHB%");
            size++;

            if(args.getBoolean("ee")){
                selectionList.add("%EE%");
                size++;
            }

            if(args.getBoolean("scag")){
                selectionList.add("%SCAG%");
                size++;
            }

            if(args.getString("level") != null && !args.getString("level").isEmpty()){
                selectionList.add(String.valueOf(((int) Math.ceil(Integer.valueOf(args.getString("level"))/2))));
            }

            if(args.getString("scclass") != null && !args.getString("scclass").isEmpty() && !args.getString("scclass").toLowerCase().equals("all")){
                selectionList.add("1");
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
            if(args.getString("level") != null){
                if(!selection.isEmpty()){
                    selection = selection + ")" + " AND " + DataBaseHandler.KEY_LEVEL + " <= ?";
                    paranthesis = true;
                }else{
                    selection = DataBaseHandler.KEY_LEVEL + " <= ?";
                }
            }
            if(args.getString("scclass") != null && !args.getString("scclass").toLowerCase().equals("all")){
                if(!selection.isEmpty()){
                    if(!paranthesis) {
                        selection = selection + ")" + " AND " + args.getString("scclass").toLowerCase() + " = ?";
                        paranthesis = true;
                    }else{
                        selection = selection + " AND " + args.getString("scclass").toLowerCase() + " = ?";
                    }
                }else{
                    selection = args.getString("scclass").toLowerCase() + " = ?";
                }
            }
            if(args.getString("filter") != null){
                if(!selection.isEmpty()){
                    if(!paranthesis){
                        selection = selection + ")" + " AND " + DataBaseHandler.KEY_NAME + " LIKE ?";
                    }else{
                        selection = selection + " AND " + DataBaseHandler.KEY_NAME + " LIKE ?";
                    }
                }else{
                    selection = DataBaseHandler.KEY_NAME + " LIKE ?";
                }
            }else{
                if(!paranthesis){
                    selection = selection + ")";
                }
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
                .add(R.id.ContainerFrame, fragment, "FD_SPELL")
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Spell names");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return false;
            }
        });
        if(searchViewQuery != null){
            searchView.setQuery(searchViewQuery, true);
            searchView.setIconified(false);
            searchView.clearFocus();
        }
    }

    public ArrayList<String> getSelectedSpells(){
        return getAdapter().getCheckedStatus();
    }

    public HashMap<Integer, Integer> getSpellCounter(){
        return getAdapter().getSpellCounter();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(searchView != null){
            outState.putString("sv1", searchView.getQuery().toString());
        }
    }
}
