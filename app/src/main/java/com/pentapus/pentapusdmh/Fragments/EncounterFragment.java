package com.pentapus.pentapusdmh.Fragments;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.pentapus.pentapusdmh.CursorRecyclerViewAdapter;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.DividerItemDecoration;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.RecyclerItemClickListener;
import com.pentapus.pentapusdmh.SimpleItemCard;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class EncounterFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.AdapterInterface {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ENCOUNTER_ID = "encounterId";
    private static final String ENCOUNTER_NAME = "encounterName";

    private static final String MODE = "modeUpdate";
    private static final String NPC_ID = "npcId";

    private ActionMode mActionMode;

    private int encounterId;
    private String encounterName;


    private CursorRecyclerViewAdapter dataAdapterNPC, dataAdapterPC;
    private static int campaignId;
    RecyclerViewMergeAdapter<CursorRecyclerViewAdapter> mergeAdapter;
    private FloatingActionButton fab;
    final CharSequence[] items = {"Edit", "Delete", "Copy"};
    private RecyclerView mRecyclerView;

    public EncounterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param encounterName Parameter 1.
     * @param encounterId   Parameter 2.
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
        dataAdapterNPC = new CursorRecyclerViewAdapter(getContext(), null, this);
        dataAdapterPC = new CursorRecyclerViewAdapter(getContext(), null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_encounter, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(encounterName + " Preparation");
        //displayListView(tableView);
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

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(1, null, this);
        mRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));

        mergeAdapter = new RecyclerViewMergeAdapter<>();
        mergeAdapter.addAdapter(0, dataAdapterNPC);
        mergeAdapter.addAdapter(1, dataAdapterPC);

        mRecyclerView.setAdapter(mergeAdapter);

        // Inflate the layout for this fragment
        return tableView;

    }


    public RecyclerViewMergeAdapter<CursorRecyclerViewAdapter> getMergeAdapter() {
        return mergeAdapter;
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
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.play_mode).setVisible(true);

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        if(clipboard.hasPrimaryClip()){
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            String pasteString = String.valueOf(itemPaste.getText());
            if(pasteUri == null){
                pasteUri = Uri.parse(pasteString);
            }
            if(pasteUri != null){
                if(DbContentProvider.NPC.equals(getContext().getContentResolver().getType(pasteUri))){
                    menu.findItem(R.id.menu_paste).setVisible(true);
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                Log.d("Contextmenu ", "Edit");
                return true;
            case R.id.copy:
                Log.d("Contextmenu ", "Copy");
                return true;
            case R.id.delete:
                Log.d("Contextmenu ", "Delete");
                return true;
            default:
                return super.onContextItemSelected(item);
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
                DataBaseHandler.KEY_INFO,
                DataBaseHandler.KEY_TYPE,
                DataBaseHandler.KEY_ICON
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
                dataAdapterNPC.swapCursor(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(final int position, final int positionType, boolean isLongClick){
        if(isLongClick){
            if(positionType == 1) {


                mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.setTitle("Selected");
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
                                if (positionType == 1) {
                                    Cursor cursor = dataAdapterNPC.getCursor();
                                    cursor.moveToPosition(position);
                                    int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId);
                                    getContext().getContentResolver().delete(uri, null, null);
                                }
                                //deleteClicked();
                                mode.finish();
                                return true;
                            case R.id.edit:
                                if (positionType == 1) {
                                    Cursor cursor = dataAdapterNPC.getCursor();
                                    cursor.moveToPosition(position);
                                    int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(MODE, true);
                                    bundle.putInt(NPC_ID, npcId);
                                    bundle.putInt(ENCOUNTER_ID, encounterId);
                                    editNPC(bundle);
                                }
                                mode.finish();
                                return true;
                            case R.id.copy:
                                if (positionType == 1) {
                                    Cursor cursor = dataAdapterNPC.getCursor();
                                    cursor.moveToPosition(position);
                                    int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId);
                                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", uri);
                                    clipboard.setPrimaryClip(clip);
                                    getActivity().invalidateOptionsMenu();
                                }
                                mode.finish();
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        CursorRecyclerViewAdapter.selectedPos = -1;
                        mergeAdapter.notifyItemChanged(position);
                    }
                });
            }else{
                Toast.makeText(getContext(), "Function not yet implemented.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Log.d("EncounterFragment ", "Click");
        }
    }

}
