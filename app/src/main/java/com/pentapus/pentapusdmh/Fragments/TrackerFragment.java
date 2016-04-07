package com.pentapus.pentapusdmh.Fragments;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.commonsware.cwac.merge.MergeAdapter;
import com.pentapus.pentapusdmh.CustomRecyclerLayoutManager;
import com.pentapus.pentapusdmh.ViewpagerClasses.HpOverviewFragment;
import com.pentapus.pentapusdmh.RecyclerItemClickListener;
import com.pentapus.pentapusdmh.TrackerAdapter;
import com.pentapus.pentapusdmh.TrackerInfoCard;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.ViewpagerClasses.ViewPagerDialogFragment;

import java.util.ArrayList;

public class TrackerFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final String ID = "id";
    private static final String STATUSES = "statuses";
    private static final String HP = "hp";


    private TrackerAdapter chars;
    private static int campaignId, encounterId;


    public TrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SessionTableFragment.
     */
    public static TrackerFragment newInstance() {
        return new TrackerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Integer> initiative = new ArrayList<Integer>();
        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        encounterId = SharedPrefsHelper.loadEncounterId(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_tracker, container, false);
        final RecyclerView mRecyclerView = (RecyclerView) tableView.findViewById(R.id.listViewEncounter);
        CustomRecyclerLayoutManager llm = new CustomRecyclerLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        // insert a record
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(SharedPrefsHelper.loadEncounterName(getContext()));

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(1, null, this);
        chars = new TrackerAdapter(getContext());
        mRecyclerView.setAdapter(chars);
        //Log.d("Name:", names.get(0));

        Button next = (Button) tableView.findViewById(R.id.bNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chars.moveToBottom();
                chars.notifyDataSetChanged();
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //TrackerInfoCard current = chars.getList().get(position);
                        onClick(position);
                    }
                })
        );
        // Inflate the layout for this fragment
        return tableView;
    }


    public void onClick(int id) {
        TrackerInfoCard mSaveTrackerInfoCard = chars.getItem(id);
        //showDialog(id);
        showViewPager(id);
        chars.setSelected(id);
        //Log.d("Current Hp: ", String.valueOf(characterList.get(id).maxHp));
        //characterList.get(id).maxHp = String.valueOf(10);
    }


    public void showViewPager(int id) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ViewPagerDialogFragment newFragment = new ViewPagerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        //bundle.putBoolean("blinded", chars.getStatus(id));
        bundle.putBooleanArray(STATUSES, chars.getStatuses(id));
        bundle.putInt(HP, chars.getHp(id));
        newFragment.setArguments(bundle);
        newFragment.setTargetFragment(this, 0);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment, "F_DIALOG_PAGER")
                .addToBackStack(null).commit();
    }

    /*
    public void onDialogButtonClick(int id, int hpChange) {
        chars.setHp(id, hpChange);
    }

    public TrackerInfoCard getmSaveTrackerInfoCard() {
        return mSaveTrackerInfoCard;
    }
    */

    public TrackerAdapter getChars() {
        return chars;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.play_mode).setVisible(false);
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
        if (id == 0) {
            String[] projection = {
                    DataBaseHandler.KEY_ROWID,
                    DataBaseHandler.KEY_NAME,
                    DataBaseHandler.KEY_INITIATIVEBONUS,
                    DataBaseHandler.KEY_AC,
                    DataBaseHandler.KEY_MAXHP
            };
            String[] selectionArgs = new String[]{String.valueOf(encounterId)};
            String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
            return new CursorLoader(this.getContext(),
                    DbContentProvider.CONTENT_URI_NPC, projection, selection, selectionArgs, null);
        } else {
            String[] projection = {
                    DataBaseHandler.KEY_ROWID,
                    DataBaseHandler.KEY_NAME,
                    DataBaseHandler.KEY_INITIATIVEBONUS,
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
                    int initiative = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS));
                    int initiativeMod = initiative;
                    int ac = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_AC));
                    int maxHp = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_MAXHP));
                    initiative = initiative + DiceHelper.d20();
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = names;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    ci.ac = String.valueOf(ac);
                    ci.maxHp = String.valueOf(maxHp);
                    ci.type = "npc";
                    ci.dead = false;
                    chars.addListItem(ci);
                }
                break;
            //case PC
            case 1:
                while (data.moveToNext()) {
                    String names = data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME));
                    int initiative = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS));
                    int initiativeMod = initiative;
                    initiative = initiative + DiceHelper.d20();
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = names;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    ci.type = "pc";
                    ci.dead = false;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}