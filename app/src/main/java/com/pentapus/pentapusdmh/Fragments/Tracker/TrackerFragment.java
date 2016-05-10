package com.pentapus.pentapusdmh.Fragments.Tracker;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.pentapus.pentapusdmh.HelperClasses.RecyclerItemClickListener;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.ViewpagerClasses.ViewPagerDialogFragment;


public class TrackerFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final String ID = "id";
    private static final String STATUSES = "statuses";
    private static final String HP = "hp";
    private static final String ABILITIES = "abilities";
    private RecyclerView mRecyclerView;
    private boolean pendingIntroAnimation;


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
        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        encounterId = SharedPrefsHelper.loadEncounterId(getContext());
        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }
        ((MainActivity)getActivity()).disableNavigationDrawer();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_tracker, container, false);
        ((MainActivity)getActivity()).setFabVisibility(false);
        mRecyclerView = (RecyclerView) tableView.findViewById(R.id.listViewEncounter);
        CustomRecyclerLayoutManager llm = new CustomRecyclerLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(SharedPrefsHelper.loadEncounterName(getContext()));

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(1, null, this);
       // getLoaderManager().initLoader(2, null, this);
        chars = new TrackerAdapter(getContext());
        mRecyclerView.setAdapter(chars);

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
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        // Inflate the layout for this fragment
        return tableView;
    }


    public void onClick(int id) {
        TrackerInfoCard mSaveTrackerInfoCard = chars.getItem(id);
        showViewPager(id);
        chars.setSelected(id);
    }


    public void showViewPager(int id) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ViewPagerDialogFragment newFragment = new ViewPagerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        bundle.putIntArray(ABILITIES, chars.getAbilities(id));
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
        switch (id) {
            case 0: //NPC
                String[] selectionArgs = new String[]{String.valueOf(encounterId)};
                String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
                return new CursorLoader(this.getContext(),
                        DbContentProvider.CONTENT_URI_ENCOUNTERPREP, DataBaseHandler.PROJECTION_ENCOUNTERPREP, selection, selectionArgs, null);
            case 1:
                selectionArgs = new String[]{String.valueOf(campaignId)};
                selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
                return new CursorLoader(this.getContext(),
                        DbContentProvider.CONTENT_URI_PC, DataBaseHandler.PROJECTION_PC, selection, selectionArgs, null);
           /* case 2:
                selectionArgs = new String[]{String.valueOf(campaignId)};
                selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
                return new CursorLoader(this.getContext(),
                        DbContentProvider.CONTENT_URI_PC, DataBaseHandler.PROJECTION_PC, selection, selectionArgs, null); */
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            //case monster
            case 0:
                while (data.moveToNext()) {
                    String names = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
                    int initiative = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS));
                    int initiativeMod = initiative;
                    int ac = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
                    int maxHp = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
                    int strength = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH));
                    int dexterity = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY));
                    int constitution = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION));
                    int intelligence = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE));
                    int wisdom = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM));
                    int charisma = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA));
                    int type = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE));
                    Uri iconUri = Uri.parse(data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));

                    initiative = initiative + DiceHelper.d20();
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = names;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    ci.ac = ac;
                    ci.maxHp = maxHp;
                    ci.hp = maxHp;
                    ci.type = type;
                    ci.dead = false;
                    ci.strength = strength;
                    ci.dexterity = dexterity;
                    ci.constitution = constitution;
                    ci.intelligence = intelligence;
                    ci.wisdom = wisdom;
                    ci.charisma = charisma;
                    ci.iconUri = iconUri;
                    chars.addListItem(ci);
                }
                break;
            case 1:
                //case PC
                while (data.moveToNext()) {
                    String names = data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME));
                    int initiative = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS));
                    int initiativeMod = initiative;
                    int ac = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_AC));
                    int hp = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_HP));
                    int type = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE));
                    int maxHp = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_MAXHP));
                    int disabled = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_DISABLED));
                    Uri iconUri = Uri.parse(data.getString(data.getColumnIndex(DataBaseHandler.KEY_ICON)));
                    initiative = initiative + DiceHelper.d20();
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = names;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    ci.ac = ac;
                    ci.maxHp = maxHp;
                    ci.hp = maxHp;
                    ci.type = type;
                    ci.dead = false;
                    ci.iconUri = iconUri;
                    if(disabled == 0){
                        chars.addListItem(ci);
                    }
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


    private void startIntroAnimation() {
        mRecyclerView.setAlpha(0f);
        mRecyclerView.animate()
                .setDuration(500)
                .alpha(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

}