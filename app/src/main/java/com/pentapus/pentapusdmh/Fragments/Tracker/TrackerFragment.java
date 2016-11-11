package com.pentapus.pentapusdmh.Fragments.Tracker;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pentapus.pentapusdmh.Fragments.Preferences.SettingsFragment;
import com.pentapus.pentapusdmh.HelperClasses.RecyclerItemClickListener;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.HelperClasses.UriDeserializer;
import com.pentapus.pentapusdmh.HelperClasses.UriSerializer;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.ViewpagerClasses.ViewPagerDialogFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class TrackerFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private int MSG_SHOW_DIALOG = 1000, MSG_FINISH_DIALOG = 1001;
    private static final String ID = "id";
    private static final String STATUSES = "statuses";
    private static final String HP = "hp";
    private static final String ABILITIES = "abilities";
    private RecyclerView mRecyclerView;
    private boolean pendingIntroAnimation, firstStartUp;
    private int enteredInitiative;
    private int dialogCounter = 0;
    List<PcListElement> pcList = new ArrayList<>();


    private TrackerAdapter chars;
    private static int campaignId, encounterId;

    public class PcListElement {
        public TrackerInfoCard trackerInfoCard;
        public int listId;

        public PcListElement(TrackerInfoCard trackerInfoCard, int listId) {
            this.trackerInfoCard = trackerInfoCard;
            this.listId = listId;
        }
    }


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

        if(!firstStartUp){
            SharedPrefsHelper.saveTrackerList(getContext(), "");
            getLoaderManager().initLoader(0, null, this);
            getLoaderManager().initLoader(1, null, this);
        }
        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
            chars = new TrackerAdapter((AppCompatActivity) getActivity(), getContext());
        }else{
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriDeserializer())
                    .create();
            Type type = new TypeToken<List<TrackerInfoCard>>() {
            }.getType();
            List<TrackerInfoCard> savedList = gson.fromJson(savedInstanceState.getString("savedList"), type);
            chars = new TrackerAdapter((AppCompatActivity) getActivity(), getContext(), savedList);
        }
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_tracker, container, false);
        mRecyclerView = (RecyclerView) tableView.findViewById(R.id.listViewEncounter);
        CustomRecyclerLayoutManager llm = new CustomRecyclerLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);


        // getLoaderManager().initLoader(2, null, this);
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
            //startIntroAnimation();
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
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.play_mode).setVisible(false);
        menu.findItem(R.id.spell_book).setVisible(true);
        super.onPrepareOptionsMenu(menu);
        getActivity().supportInvalidateOptionsMenu();
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
    public void onPause() {
        super.onPause();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();
        String json = gson.toJson(chars.getCharacterList());
        SharedPrefsHelper.saveTrackerList(getContext(), json);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(SharedPrefsHelper.loadEncounterName(getContext()));
        ((MainActivity)getActivity()).setFabVisibility(false);
        ((MainActivity)getActivity()).disableNavigationDrawer();
        getActivity().invalidateOptionsMenu();
        String trackerList = SharedPrefsHelper.loadTrackerList(getContext());
        if(!trackerList.equals("")){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriDeserializer())
                    .create();
            Type type = new TypeToken<List<TrackerInfoCard>>() {
            }.getType();
            List<TrackerInfoCard> savedList = gson.fromJson(trackerList, type);
            chars.setCharacterList(savedList);
            chars.notifyDataSetChanged();
        }
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
                    int ac2 = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_AC2));
                    int maxHp = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
                    int strength = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH));
                    int dexterity = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY));
                    int constitution = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION));
                    int intelligence = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE));
                    int wisdom = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM));
                    int charisma = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA));
                    int type = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE));

                    String monsterType = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_MONSTERTYPE));
                    String acType = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ACTYPE));
                    String ac2Type = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_AC2TYPE));
                    int xp = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_XP));
                    String size = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_SIZE));
                    String source = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_SOURCE));
                    int sourcepage = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_SOURCEPAGE));
                    String speed = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_SPEED));
                    String multiattack = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_MULTIATTACK));

                    String atk1name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1NAME));
                    String atk1desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DESC));
                    int atk1mod = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1MOD));
                    String atk1dmg1roll = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG1ROLL));
                    String atk1dmg1type = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG1TYPE));
                    String atk1dmg2roll = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG2ROLL));
                    String atk1dmg2type = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG2TYPE));
                    int atk1autoroll = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1AUTOROLL));
                    int atk1additional = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1ADDITIONAL));

                    String atk2name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2NAME));
                    String atk2desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DESC));
                    int atk2mod = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2MOD));
                    String atk2dmg1roll = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG1ROLL));
                    String atk2dmg1type = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG1TYPE));
                    String atk2dmg2roll = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG2ROLL));
                    String atk2dmg2type = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG2TYPE));
                    int atk2autoroll = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2AUTOROLL));
                    int atk2additional = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2ADDITIONAL));

                    String atk3name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3NAME));
                    String atk3desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DESC));
                    int atk3mod = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3MOD));
                    String atk3dmg1roll = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG1ROLL));
                    String atk3dmg1type = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG1TYPE));
                    String atk3dmg2roll = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG2ROLL));
                    String atk3dmg2type = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG2TYPE));
                    int atk3autoroll = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3AUTOROLL));
                    int atk3additional = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3ADDITIONAL));

                    String atk4name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4NAME));
                    String atk4desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DESC));
                    int atk4mod = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4MOD));
                    String atk4dmg1roll = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG1ROLL));
                    String atk4dmg1type = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG1TYPE));
                    String atk4dmg2roll = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG2ROLL));
                    String atk4dmg2type = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG2TYPE));
                    int atk4autoroll = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4AUTOROLL));
                    int atk4additional = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4ADDITIONAL));

                    String reaction1name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION1NAME));
                    String reaction1desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION1DESC));

                    String ability1name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1NAME));
                    String ability1desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1DESC));
                    String ability2name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY2NAME));
                    String ability2desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY2DESC));
                    String ability3name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY3NAME));
                    String ability3desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY3DESC));
                    String ability4name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY4NAME));
                    String ability4desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY4DESC));
                    String ability5name = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY5NAME));
                    String ability5desc = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY5DESC));

                    int acrobatics = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ACROBATICS));
                    int animalhandling = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ANIMALHANDLING));
                    int arcana = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ARCANA));
                    int athletics = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ATHLETICS));
                    int deception = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_DECEPTION));
                    int history = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_HISTORY));
                    int insight = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_INSIGHT));
                    int intimidation = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_INTIMIDATION));
                    int investigation = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_INVESTIGATION));
                    int medicine = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_MEDICINE));
                    int nature = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_NATURE));
                    int perception = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_PERCEPTION));
                    int performance = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_PERFORMANCE));
                    int persuasion = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_PERSUASION));
                    int religion = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_RELIGION));
                    int sleightofhand = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_SLEIGHTOFHAND));
                    int stealth = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_STEALTH));
                    int survival = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_SURVIVAL));

                    int stStr = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_STSTR));
                    int stDex = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_STDEX));
                    int stCon = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_STCON));
                    int stInt = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_STINT));
                    int stWis = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_STWIS));
                    int stCha = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_STCHA));

                    String senses = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_SENSES));
                    String alignment = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ALIGNMENT));
                    String languages = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_LANGUAGES));
                    String dmgRes = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGRES));
                    String dmgIm = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGIM));
                    String dmgVul = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGVUL));
                    String conIm = data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_CONIM));




                    Uri iconUri = Uri.parse(data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
                    initiative = initiative + DiceHelper.d20();
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = names;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    ci.ac = ac;
                    ci.ac2 = ac2;
                    ci.maxHp = maxHp;
                    ci.hp = maxHp;
                    ci.type = type;
                    ci.monsterType = monsterType;
                    ci.dead = false;
                    ci.strength = strength;
                    ci.dexterity = dexterity;
                    ci.constitution = constitution;
                    ci.intelligence = intelligence;
                    ci.wisdom = wisdom;
                    ci.charisma = charisma;
                    ci.iconUri = iconUri;
                    ci.acType = acType;
                    ci.acType2 = ac2Type;
                    ci.xp = xp;
                    ci.size = size;
                    ci.speed = speed;
                    ci.source = source;
                    ci.sourcepage = sourcepage;
                    ci.multiattack = multiattack;

                    ci.atk1name = atk1name;
                    ci.atk1desc = atk1desc;
                    ci.atk1mod = atk1mod;
                    ci.atk1dmg1roll = atk1dmg1roll;
                    ci.atk1dmg1type = atk1dmg1type;
                    ci.atk1dmg2roll = atk1dmg2roll;
                    ci.atk1dmg2type = atk1dmg2type;
                    ci.atk1autoroll = atk1autoroll;
                    ci.atk1additional = atk1additional;

                    ci.atk2name = atk2name;
                    ci.atk2desc = atk2desc;
                    ci.atk2mod = atk2mod;
                    ci.atk2dmg1roll = atk2dmg1roll;
                    ci.atk2dmg1type = atk2dmg1type;
                    ci.atk2dmg2roll = atk2dmg2roll;
                    ci.atk2dmg2type = atk2dmg2type;
                    ci.atk2autoroll = atk2autoroll;
                    ci.atk2additional = atk2additional;

                    ci.atk3name = atk3name;
                    ci.atk3desc = atk3desc;
                    ci.atk3mod = atk3mod;
                    ci.atk3dmg1roll = atk3dmg1roll;
                    ci.atk3dmg1type = atk3dmg1type;
                    ci.atk3dmg2roll = atk3dmg2roll;
                    ci.atk3dmg2type = atk3dmg2type;
                    ci.atk3autoroll = atk3autoroll;
                    ci.atk3additional = atk3additional;

                    ci.atk4name = atk4name;
                    ci.atk4desc = atk4desc;
                    ci.atk4mod = atk4mod;
                    ci.atk4dmg1roll = atk4dmg1roll;
                    ci.atk4dmg1type = atk4dmg1type;
                    ci.atk4dmg2roll = atk4dmg2roll;
                    ci.atk4dmg2type = atk4dmg2type;
                    ci.atk4autoroll = atk4autoroll;
                    ci.atk4additional = atk4additional;

                    ci.reaction1name = reaction1name;
                    ci.reaction1desc = reaction1desc;

                    ci.ability1name = ability1name;
                    ci.ability1desc = ability1desc;
                    ci.ability2name = ability2name;
                    ci.ability2desc = ability2desc;
                    ci.ability3name = ability3name;
                    ci.ability3desc = ability3desc;
                    ci.ability4name = ability4name;
                    ci.ability4desc = ability4desc;
                    ci.ability5name = ability5name;
                    ci.ability5desc = ability5desc;

                    ci.acrobatics = acrobatics;
                    ci.animalhandling = animalhandling;
                    ci.arcana = arcana;
                    ci.athletics = athletics;
                    ci.deception = deception;
                    ci.history = history;
                    ci.insight = insight;
                    ci.intimidation = intimidation;
                    ci.investigation = investigation;
                    ci.medicine = medicine;
                    ci.nature = nature;
                    ci.perception = perception;
                    ci.performance = performance;
                    ci.persuasion = persuasion;
                    ci.religion = religion;
                    ci.sleightofhand = sleightofhand;
                    ci.stealth = stealth;
                    ci.survival = survival;

                    ci.stStr = stStr;
                    ci.stDex = stDex;
                    ci.stCon = stCon;
                    ci.stInt = stInt;
                    ci.stWis = stWis;
                    ci.stCha = stCha;

                    ci.senses = senses;
                    ci.alignment = alignment;
                    ci.languages = languages;
                    ci.dmgRes = dmgRes;
                    ci.dmgIm = dmgIm;
                    ci.dmgVul = dmgVul;
                    ci.conIm = conIm;



                    chars.addListItem(ci);
                }
                break;
            case 1:
                //case PC
                while (data.moveToNext()) {
                    String name = data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME));
                    int initiative = 0;
                    int initiativeMod = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS));
                    int ac = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_AC));
                    int hp = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_HP));
                    int type = data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE));
                    int maxHp = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_MAXHP));
                    int disabled = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_DISABLED));
                    Uri iconUri = Uri.parse(data.getString(data.getColumnIndex(DataBaseHandler.KEY_ICON)));
                    if(SettingsFragment.isAutoRollEnabled(getContext())) {
                        initiative = initiativeMod + DiceHelper.d20();
                    }
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = name;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    ci.ac = ac;
                    ci.maxHp = maxHp;
                    ci.hp = maxHp;
                    ci.type = type;
                    ci.dead = false;
                    ci.iconUri = iconUri;
                    if (disabled == 0) {
                        chars.addListItem(ci);
                    }


                }

                //Fixme: save entered initiative while browsing spells
                if(!SettingsFragment.isAutoRollEnabled(getContext()) && !firstStartUp) {
                    handler.sendEmptyMessage(MSG_SHOW_DIALOG);
                    firstStartUp = true;
                }else{
                    chars.sortCharacterList();
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SHOW_DIALOG) {
                for (int i = 0; i < chars.characterList.size(); i++) {
                    if (chars.characterList.get(i).getType() == DataBaseHandler.TYPE_PC) {
                        pcList.add(new PcListElement(chars.characterList.get(i), i));
                        startInitiativeDialog(chars.characterList.get(i).getName(), Integer.parseInt(chars.characterList.get(i).getInitiativeMod()), i);
                    }
                }
            }
        }
    };

    private void startInitiativeDialog(String name, int initiativeMod, int i) {
        DialogFragment newFragment = EnterInitiativeDialogFragment.newInstance(name, initiativeMod, i);
        newFragment.setTargetFragment(this, MSG_FINISH_DIALOG);
        newFragment.show(getActivity().getSupportFragmentManager(), "F_ENTER_INITIATIVE_DIALOG");
        dialogCounter++;
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

    public void onDialogResult(int requestCode, int listId, int enteredInitiative) {
        Log.d("DialogResult", String.valueOf(enteredInitiative));
        int totalInitiative = Integer.parseInt(chars.characterList.get(listId).getInitiativeMod()) + enteredInitiative;
        chars.characterList.get(listId).setInitiative(String.valueOf(totalInitiative));
        dialogCounter--;
        if (dialogCounter == 0) {
            chars.sortCharacterList();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Save the fragment's instance
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();
        String json = gson.toJson(chars.getCharacterList());
        outState.putString("savedList", json);
        super.onSaveInstanceState(outState);

    }}