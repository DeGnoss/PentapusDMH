package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;


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
import android.support.v4.view.MenuItemCompat;
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

import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.List;


public class MonsterManualTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback {


    private static final String NPC_ID = "npcId";

    private RecyclerView myMonsterRecyclerView;
    private ActionMode mActionMode;
    private boolean isNavMode;
    private Bundle filters;
    private String searchViewQuery;

    private MonsterManualAdapter myMonsterAdapter;

    public MonsterManualTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static MonsterManualTableFragment newInstance(boolean isNavMode) {
        MonsterManualTableFragment fragment = new MonsterManualTableFragment();
        Bundle args = new Bundle();
        args.putBoolean("navMode", isNavMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (this.getArguments() != null) {
            isNavMode = this.getArguments().getBoolean("navMode");
        }
        if (savedInstanceState != null) {
            searchViewQuery = savedInstanceState.getString("sv1");
        }
        myMonsterAdapter = new MonsterManualAdapter(getContext(), this, isNavMode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_monster_table, container, false);

        myMonsterRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myMonsterRecyclerView.setLayoutManager(linearLayoutManager);
        myMonsterRecyclerView.setHasFixedSize(true);
        myMonsterRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        myMonsterRecyclerView.setAdapter(myMonsterAdapter);
        return tableView;
    }

    @Override
    public void onResume() {
        super.onResume();
        filters = new Bundle();
        myMonsterAdapter.setSelectedPos(-1);
        ((MonsterViewPagerDialogFragment) getParentFragment()).setFabIcon(true);
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    public void resetSearch(){
        myMonsterAdapter.setSelectedPos(-1);
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //menu.findItem(R.id.campaign_settings).setVisible(true);
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
                if (DbContentProvider.ENCOUNTER.equals(getContext().getContentResolver().getType(pasteUri))) {
                    menu.findItem(R.id.menu_paste).setVisible(true);
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Name or type");
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
        if (searchViewQuery != null) {
            searchView.setQuery(searchViewQuery, true);
            searchView.setIconified(false);
            searchView.clearFocus();
        }
    }

    public void filterData(String filterArgs) {
        String filterFormatted = "%" + filterArgs + "%";
        filters.putString("filter", filterFormatted);
        if (isAdded()) {
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

            selectionList.add("%MM%");
            size++;

            if (args.getString("filter") != null) {
                selectionList.add(args.getString("filter"));
                selectionList.add(args.getString("filter"));
            }

            selectionArgs = new String[selectionList.size()];
            selectionList.toArray(selectionArgs);

            for (int i = 0; i < size; i++) {
                if (!selection.isEmpty()) {
                    selection = selection + " OR ";
                    selection = selection + DataBaseHandler.KEY_SOURCE + " LIKE ?";
                } else {
                    selection = "(" + DataBaseHandler.KEY_SOURCE + " LIKE ?";
                }
            }
            if (args.getString("filter") != null) {
                if (!selection.isEmpty()) {
                    selection = selection + ")" + " AND (" + DataBaseHandler.KEY_NAME + " LIKE ? OR " + DataBaseHandler.KEY_TYPE + " LIKE ?)";
                } else {
                    selection = DataBaseHandler.KEY_NAME + " LIKE ?";
                }
            } else {
                selection = selection + ")";
            }
        } else {
            selectionArgs = new String[]{"%MM%"};
            selection = "source LIKE ?";
        }
        return new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_MONSTER, DataBaseHandler.PROJECTION_MONSTER_TEMPLATE, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        myMonsterAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myMonsterAdapter.swapCursor(null);
    }


    @Override
    public void onItemClick(int position) {
        dismissActionMode();
        Cursor cursor = myMonsterAdapter.getCursor();
        cursor.moveToPosition(position);
        int monsterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        Bundle bundle = new Bundle();
        bundle.putInt(NPC_ID, monsterId);
        displayMonster(bundle);
    }

    @Override
    public void onItemLongCLick(int position) {
    }

    public RecyclerView getMyMonsterRecyclerView() {
        return myMonsterRecyclerView;
    }

    public void dismissActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    @Override
    public void onMenuRefresh() {
        getActivity().invalidateOptionsMenu();
    }

    private void displayMonster(Bundle bundle) {
        Fragment fragment;
        fragment = new DetailMonsterFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.ContainerFrame, fragment, "F_DETAIL_MONSTER")
                .addToBackStack("F_DETAIL_MONSTER")
                .commit();
    }
}
