package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC;


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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.R;


public class MyNPCTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback {


    private static final String MODE = "modeUpdate";
    private static final String NPC_ID = "npcId";

    private RecyclerView myNPCRecyclerView;
    private ActionMode mActionMode;
    private MyNPCAdapter myNPCAdapter;
    private static int campaignId;
    private boolean isNavMode;

    public MyNPCTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static MyNPCTableFragment newInstance(boolean isNavMode) {
        MyNPCTableFragment fragment = new MyNPCTableFragment();
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
        myNPCAdapter = new MyNPCAdapter(getContext(), this, isNavMode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tableView = inflater.inflate(R.layout.fragment_monster_table, container, false);

        myNPCRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myNPCRecyclerView.setLayoutManager(linearLayoutManager);
        myNPCRecyclerView.setHasFixedSize(true);
        myNPCRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        myNPCRecyclerView.setAdapter(myNPCAdapter);
        return tableView;
    }

    @Override
    public void onResume() {
        super.onResume();
        myNPCAdapter.setSelectedPos(-1);
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
        String[] selectionArgs = new String[]{String.valueOf(campaignId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        return new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_NPC, DataBaseHandler.PROJECTION_NPC_TEMPLATE, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        myNPCAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myNPCAdapter.swapCursor(null);
    }


    @Override
    public void onItemClick(int position) {
        dismissActionMode();
        Cursor cursor = myNPCAdapter.getCursor();
        cursor.moveToPosition(position);
        int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        Bundle bundle = new Bundle();
        bundle.putInt(NPC_ID, npcId);
        displayNPC(bundle);
    }

    @Override
    public void onItemLongCLick(final int position) {

        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                ((NPCViewPagerDialogFragment)getActivity().getSupportFragmentManager().findFragmentByTag("F_NPC_PAGER")).setFabVisibility(false);
                String title = "Selected: " + String.valueOf(position);
                mode.setTitle(title);
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                myNPCAdapter.setSelectedPos(position);
                myNPCAdapter.notifyItemChanged(position);
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
                        Cursor cursor = myNPCAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId);
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
                        cursor = myNPCAdapter.getCursor();
                        cursor.moveToPosition(position);
                        npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(MODE, true);
                        bundle.putInt(NPC_ID, npcId);
                        editNPC(bundle);
                        mode.finish();
                        return true;
                    case R.id.copy:
                        cursor = myNPCAdapter.getCursor();
                        cursor.moveToPosition(position);
                        npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId);
                        pasteNPC(uri);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                myNPCAdapter.setSelectedPos(-1);
                myNPCAdapter.notifyItemChanged(position);
                ((NPCViewPagerDialogFragment)getActivity().getSupportFragmentManager().findFragmentByTag("F_NPC_PAGER")).setFabVisibility(true);
                mActionMode = null;
            }
        });
    }

    private void editNPC(Bundle bundle) {
        Fragment fragment;
        fragment = new MyNPCEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerFrame, fragment, "FE_MYNPC")
                .addToBackStack("FE_MYNPC")
                .commit();
    }


    private void pasteNPC(Uri pasteUri) {
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
                        values.put(DataBaseHandler.KEY_INITIATIVEBONUS, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS)));
                        values.put(DataBaseHandler.KEY_MAXHP, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP)));
                        values.put(DataBaseHandler.KEY_AC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)));
                        values.put(DataBaseHandler.KEY_STRENGTH, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH)));
                        values.put(DataBaseHandler.KEY_DEXTERITY, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY)));
                        values.put(DataBaseHandler.KEY_CONSTITUTION, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION)));
                        values.put(DataBaseHandler.KEY_INTELLIGENCE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE)));
                        values.put(DataBaseHandler.KEY_WISDOM, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM)));
                        values.put(DataBaseHandler.KEY_CHARISMA, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA)));
                        values.put(DataBaseHandler.KEY_ICON, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
                        values.put(DataBaseHandler.KEY_TYPE, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)));
                        values.put(DataBaseHandler.KEY_BELONGSTO, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_BELONGSTO)));
                        startInsert(1, null, DbContentProvider.CONTENT_URI_NPC, values);
                    }
                    cursor.close();
                }
            }
        };
        queryHandler.startQuery(
                1, null,
                pasteUri,
                DataBaseHandler.PROJECTION_NPC_TEMPLATE,
                null,
                null,
                null
        );
    }

    public void dismissActionMode(){
        if(mActionMode!= null){
            mActionMode.finish();
        }
    }

    public RecyclerView getMyNPCRecyclerView() {
        return myNPCRecyclerView;
    }

    @Override
    public void onMenuRefresh() {
        getActivity().invalidateOptionsMenu();
    }

    private void displayNPC(Bundle bundle) {
        Fragment fragment;
        fragment = new DetailNPCFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerFrame, fragment, "F_DETAIL_NPC")
                .addToBackStack("F_DETAIL_NPC")
                .commit();
    }
}
