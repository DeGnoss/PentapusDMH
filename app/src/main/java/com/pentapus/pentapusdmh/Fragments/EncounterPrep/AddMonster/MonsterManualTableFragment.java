package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterAdapter;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.MonsterEditFragment;
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.R;


public class MonsterManualTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback {


    private static final String MODE = "modeUpdate";


    private int sessionId;
    private String sessionName;

    private RecyclerView myMonsterRecyclerView;
    private ActionMode mActionMode;
    private boolean isNavMode;

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
        myMonsterAdapter = new MonsterManualAdapter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_monster_table, container, false);
        // insert a record

        myMonsterRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myMonsterRecyclerView.setLayoutManager(linearLayoutManager);
        myMonsterRecyclerView.setHasFixedSize(true);
        myMonsterRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        myMonsterRecyclerView.setAdapter(myMonsterAdapter);
        if (isNavMode) {
            myMonsterAdapter.statusClicked(-1);
        }

        // Inflate the layout for this fragment
        return tableView;
    }

    @Override
    public void onResume() {
        super.onResume();
        myMonsterAdapter.statusClicked(-1);
        ((MonsterViewPagerDialogFragment)getParentFragment()).setFabIcon(true);
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            getLoaderManager().restartLoader(0, null, this);
        }
    }


    public int getSessionId() {
        return sessionId;
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] selectionArgs = new String[]{String.valueOf(1)};
        String selection = DataBaseHandler.KEY_MM + " = ?";
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_MONSTER, DataBaseHandler.PROJECTION_MONSTER_TEMPLATE, selection, selectionArgs, null);
        return cursorLoader;
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
        if (!isNavMode) {
            myMonsterAdapter.statusClicked(position);
        } else {
            myMonsterAdapter.statusClicked(-1);
        }
    }

    @Override
    public void onItemLongCLick(final int position) {
    }

    public RecyclerView getMyMonsterRecyclerView() {
        return myMonsterRecyclerView;
    }

    public void dismissActionMode(){
        if(mActionMode!= null){
            mActionMode.finish();
        }
    }

    @Override
    public void onMenuRefresh() {
        getActivity().invalidateOptionsMenu();
    }
}
