package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC;


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
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.R;


public class NPCTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback {


    private static final String MODE = "modeUpdate";
    private static final String NPC_ID = "npcId";

    private RecyclerView myNPCRecyclerView;
    private ActionMode mActionMode;
    private NPCAdapter npcAdapter;
    private static int campaignId;
    private boolean isNavMode;

    public NPCTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static NPCTableFragment newInstance(boolean isNavMode) {
        NPCTableFragment fragment = new NPCTableFragment();
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
        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        npcAdapter = new NPCAdapter(getContext(), this, isNavMode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_monster_table, container, false);

        myNPCRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myNPCRecyclerView.setLayoutManager(linearLayoutManager);
        myNPCRecyclerView.setHasFixedSize(true);
        myNPCRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        myNPCRecyclerView.setAdapter(npcAdapter);
        return tableView;
    }

    @Override
    public void onResume() {
        super.onResume();
        npcAdapter.setSelectedPos(-1);
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
                if (DbContentProvider.NPC.equals(getContext().getContentResolver().getType(pasteUri))) {
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
        return new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_NPC, DataBaseHandler.PROJECTION_NPC_TEMPLATE, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        npcAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        npcAdapter.swapCursor(null);
    }


    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onItemLongCLick(int position) {

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


    public RecyclerView getMyNPCRecyclerView() {
        return myNPCRecyclerView;
    }
}
