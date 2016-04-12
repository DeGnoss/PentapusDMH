package com.pentapus.pentapusdmh.Fragments;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pentapus.pentapusdmh.CustomRecyclerLayoutManager;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.DividerItemDecoration;
import com.pentapus.pentapusdmh.EncounterPreparationAdapter;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.RecyclerItemClickListener;
import com.pentapus.pentapusdmh.SimpleItemCard;
import com.pentapus.pentapusdmh.TrackerAdapter;
import com.pentapus.pentapusdmh.TrackerInfoCard;
import com.pentapus.pentapusdmh.ViewpagerClasses.ViewPagerDialogFragment;

public class EncounterPreparationFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final String ENCOUNTER_ID = "encounterId";
    private static final String ENCOUNTER_NAME = "encounterName";

    private static final String MODE = "modeUpdate";
    private static final String NPC_ID = "npcId";

    private int encounterId;
    private String encounterName;
    private static int campaignId;

    private RecyclerView mRecyclerView;
    private EncounterPreparationAdapter chars;
    private FloatingActionButton fab;

    public EncounterPreparationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SessionTableFragment.
     */
    public static EncounterPreparationFragment newInstance(String encounterName, int encounterId) {
        EncounterPreparationFragment fragment = new EncounterPreparationFragment();
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_encounter, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(encounterName + " Preparation");

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(1, null, this);
        chars = new EncounterPreparationAdapter(getContext());
        mRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));


        mRecyclerView.setAdapter(chars);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //TrackerInfoCard current = chars.getList().get(position);
                        onClick(position);
                    }
                })
        );


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
        // Inflate the layout for this fragment
        return tableView;
    }


    public void onClick(int id) {
        Log.d("EncounterPreparation: ", String.valueOf(id));
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


    public EncounterPreparationAdapter getChars() {
        return chars;
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
        if (id == 0) {
            String[] projection = {
                    DataBaseHandler.KEY_ROWID,
                    DataBaseHandler.KEY_NAME,
                    DataBaseHandler.KEY_INFO,
                    DataBaseHandler.KEY_ICON
            };
            String[] selectionArgs = new String[]{String.valueOf(encounterId)};
            String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
            return new CursorLoader(this.getContext(),
                    DbContentProvider.CONTENT_URI_NPC, projection, selection, selectionArgs, null);
        } else {
            String[] projection = {
                    DataBaseHandler.KEY_ROWID,
                    DataBaseHandler.KEY_NAME,
                    DataBaseHandler.KEY_INFO,
                    DataBaseHandler.KEY_ICON
            };
            String[] selectionArgs = new String[]{String.valueOf(campaignId)};
            String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
            return new CursorLoader(this.getContext(),
                    DbContentProvider.CONTENT_URI_PC, projection, selection, selectionArgs, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            //case NPC
            case 0:
                while (data.moveToNext()) {
                    String names = data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME));
                    String info = data.getString(data.getColumnIndex(DataBaseHandler.KEY_INFO));
                    Uri iconUri = Uri.parse(data.getString(data.getColumnIndex(DataBaseHandler.KEY_ICON)));
                    SimpleItemCard ci = new SimpleItemCard();
                    ci.name = names;
                    ci.info = info;
                    ci.iconUri = iconUri;
                    chars.addListItem(ci);
                }
                break;
            //case PC
            case 1:
                while (data.moveToNext()) {
                    String names = data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME));
                    String info = data.getString(data.getColumnIndex(DataBaseHandler.KEY_INFO));
                    Uri iconUri = Uri.parse(data.getString(data.getColumnIndex(DataBaseHandler.KEY_ICON)));
                    SimpleItemCard ci = new SimpleItemCard();
                    ci.name = names;
                    ci.info = info;
                    ci.iconUri = iconUri;
                    chars.addListItem(ci);
                }
                break;
            default:
                break;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chars.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case 0:
                //dataAdapterNPC.swapCursor(null);
                break;
            case 1:
                //dataAdapterPC.swapCursor(null);
                break;
            default:
                break;
        }
    }

}