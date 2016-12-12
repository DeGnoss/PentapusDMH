package com.pentapus.pentapusdmh.Fragments.Spells;


import android.content.AsyncQueryHandler;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.R;

import java.util.Observable;
import java.util.Observer;


public class MySpellTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback {


    private static final String MODE = "modeUpdate";
    private static final String SPELL_ID = "spellId";
    private String sourceType;
    private SearchView searchView;
    private String searchViewQuery;

    private RecyclerView mySpellRecyclerView;
    private ActionMode mActionMode;
    private MySpellAdapter mySpellAdapter;
    private int mode;

    public MySpellTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static MySpellTableFragment newInstance(String sourceType, int mode) {
        MySpellTableFragment fragment = new MySpellTableFragment();
        Bundle args = new Bundle();
        args.putString("sourcetype", sourceType);
        args.putInt("mode", mode);
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
        }
        if(savedInstanceState != null){
            searchViewQuery = savedInstanceState.getString("sv2");
        }
        mySpellAdapter = new MySpellAdapter(getContext(), this, mode);
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


        //setUpItemTouchHelper();
        //setUpAnimationDecoratorHelper();

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
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            String pasteString = String.valueOf(itemPaste.getText());
            if (pasteUri == null) {
                pasteUri = Uri.parse(pasteString);
            }
            if (pasteUri != null) {
                if (DbContentProvider.SPELL.equals(getContext().getContentResolver().getType(pasteUri))) {
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
        String selection;
        String[] selectionArgs;

        if(args != null){
//            selectionArgs = new String[]{"%" + args.getString("filter") + "%"};
            selectionArgs = new String[]{"%" + sourceType + "%", "%" + args.getString("filter") + "%"};
            String selection1 = DataBaseHandler.KEY_SOURCE;

            String selection2 = DataBaseHandler.KEY_NAME;
            selection = selection1 + " LIKE ? AND " + selection2 + " LIKE ?";
        }else{

            selectionArgs = new String[]{"%" + sourceType + "%"};
            String selection1 = DataBaseHandler.KEY_SOURCE;
            selection = selection1 + " LIKE ?";
        }
        return new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_SPELL, DataBaseHandler.PROJECTION_SPELL, selection, selectionArgs, null);
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
        mySpellAdapter.statusClicked(position);
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onItemLongCLick(final int position) {
        SpellViewPagerDialogFragment.setSelectedType(0);
        SpellViewPagerDialogFragment.setHighlightedPos(position);
        int oldPos = SpellViewPagerDialogFragment.getSelectedPos();
        SpellViewPagerDialogFragment.setSelectedPos(position);
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mySpellRecyclerView.getAdapter().notifyItemChanged(position);
                String title = "Selected: " + String.valueOf(position);
                ((SpellViewPagerDialogFragment)getActivity().getSupportFragmentManager().findFragmentByTag("F_SPELL_PAGER")).setFabVisibility(false);
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
                        //TODO: ADD DIALOG TO MAKE SURE THE DELETE IS INTENTIONAL
                        Cursor cursor = mySpellAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SPELL + "/" + npcId);
                        getContext().getContentResolver().delete(uri, null, null);
                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard.hasPrimaryClip()) {
                            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
                            Uri pasteUri = itemPaste.getUri();
                            if (pasteUri == null) {
                                pasteUri = Uri.parse(String.valueOf(itemPaste.getText()));
                            }
                            if (pasteUri.equals(uri)) {
                                Uri newUri = Uri.parse("");
                                ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", newUri);
                                clipboard.setPrimaryClip(clip);
                                getActivity().invalidateOptionsMenu();
                            }
                        }
                        mode.finish();
                        return true;
                    case R.id.edit:
                        cursor = mySpellAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int spellId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(MODE, true);
                        bundle.putInt(SPELL_ID, spellId);
                        editSpell(bundle);
                        mode.finish();
                        return true;
                    case R.id.copy:
                        cursor = mySpellAdapter.getCursor();
                        cursor.moveToPosition(position);
                        spellId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        uri = Uri.parse(DbContentProvider.CONTENT_URI_SPELL + "/" + spellId);
                        pasteSpell(uri);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                SpellViewPagerDialogFragment.setHighlightedPos(-1);
                SpellViewPagerDialogFragment.setSelectedPos(-1);
                mySpellRecyclerView.getAdapter().notifyItemChanged(position);
                ((SpellViewPagerDialogFragment)getActivity().getSupportFragmentManager().findFragmentByTag("F_SPELL_PAGER")).setFabVisibility(true);
                mActionMode = null;
            }
        });
    }

    private void editSpell(Bundle bundle) {
        Fragment fragment;
        fragment = new MySpellEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawer_layout, fragment, "FE_MYSPELL")
                .addToBackStack("FE_MYSPELL")
                .commit();
    }


    private void pasteSpell(Uri pasteUri) {
        final AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContext().getContentResolver()) {

            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                if (cursor == null) {
                    // Some providers return null if an error occurs whereas others throw an exception
                } else if (cursor.getCount() < 1) {
                    // No matches found
                } else {
                    while (cursor.moveToNext()) {
                        ContentValues values = new ContentValues();
                        values.put(DataBaseHandler.KEY_NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
                        values.put(DataBaseHandler.KEY_INFO, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
                        values.put(DataBaseHandler.KEY_LEVEL, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LEVEL)));
                        values.put(DataBaseHandler.KEY_VOCAL, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_VOCAL)));
                        values.put(DataBaseHandler.KEY_SOMATIC, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SOMATIC)));
                        values.put(DataBaseHandler.KEY_MATERIAL, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MATERIAL)));
                        values.put(DataBaseHandler.KEY_TIME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TIME)));
                        values.put(DataBaseHandler.KEY_DURATION, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DURATION)));
                        values.put(DataBaseHandler.KEY_SCHOOL, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SCHOOL)));
                        values.put(DataBaseHandler.KEY_RANGE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_RANGE)));
                        values.put(DataBaseHandler.KEY_INFO, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
                        startInsert(1, null, DbContentProvider.CONTENT_URI_SPELL, values);
                    }
                    cursor.close();
                }
            }
        };

        queryHandler.startQuery(
                1, null,
                pasteUri,
                DataBaseHandler.PROJECTION_SPELL,
                null,
                null,
                null
        );
    }


    public void filterData(String filterArgs){
        Bundle bundle = new Bundle();
        bundle.putString("filter", filterArgs);
        if(isAdded()){
            getLoaderManager().restartLoader(0, bundle, this);
        }
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



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(searchView != null){
            outState.putString("sv2", searchView.getQuery().toString());
        }
    }
}
