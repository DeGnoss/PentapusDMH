package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koni on 4/4/16.
 */
public class MonsterViewPagerDialogFragment extends Fragment {

    private static final String ENCOUNTER_ID = "encounterId";
    private static final String NAV_MODE = "navMode";
    private static final String ENCOUNTER_NAME = "encounterName";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MonsterViewPagerAdapter pagerAdapter;
    private FloatingActionButton fabMonsterVP;
    private int id;
    private int encounterId;
    private boolean navMode;
    private static final String MODE = "modeUpdate";

    private String encounterName;
    private static List<Uri> monstersToBeAdded;


    public MonsterViewPagerDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static MonsterViewPagerDialogFragment newInstance(int encounterId, boolean navMode) {
        MonsterViewPagerDialogFragment fragment = new MonsterViewPagerDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(NAV_MODE, navMode);
        args.putInt(ENCOUNTER_ID, encounterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            encounterId = getArguments().getInt(ENCOUNTER_ID);
            navMode = getArguments().getBoolean(NAV_MODE);
            encounterName = getArguments().getString(ENCOUNTER_NAME);
        }
        monstersToBeAdded = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.monster_viewpager_tab_layout, parent, false);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        fabMonsterVP = (FloatingActionButton) view.findViewById(R.id.fabImageVP);

        fabMonsterVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(MODE, false);
                bundle.putBoolean(NAV_MODE, navMode);
                addMonster(bundle);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ((MyMonsterTableFragment) pagerAdapter.getRegisteredFragment(0)).getMyMonsterRecyclerView().getAdapter().notifyDataSetChanged();
                        ((MonsterManualTableFragment) pagerAdapter.getRegisteredFragment(1)).dismissActionMode();
                        fabMonsterVP.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        ((MonsterManualTableFragment) pagerAdapter.getRegisteredFragment(1)).getMyMonsterRecyclerView().getAdapter().notifyDataSetChanged();
                        ((MyMonsterTableFragment) pagerAdapter.getRegisteredFragment(0)).dismissActionMode();
                        fabMonsterVP.setVisibility(View.GONE);
                        break;
                    default:
                        fabMonsterVP.setVisibility(View.GONE);
                        break;
                }
                pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return view;
    }

    //only when not in navmode
    public void pasteMonster() {
        int i = 0;
        Uri pasteUri;
        for (Uri element : monstersToBeAdded) {
            pasteUri = monstersToBeAdded.get(i);
            i++;
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
                            values.put(DataBaseHandler.KEY_MONSTERTYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MONSTERTYPE)));
                            values.put(DataBaseHandler.KEY_ACTYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ACTYPE)));
                            values.put(DataBaseHandler.KEY_XP, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_XP)));
                            values.put(DataBaseHandler.KEY_SIZE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SIZE)));
                            values.put(DataBaseHandler.KEY_SPEED, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SPEED)));
                            values.put(DataBaseHandler.KEY_SOURCE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SIZE)));
                            values.put(DataBaseHandler.KEY_SOURCEPAGE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SOURCEPAGE)));
                            values.put(DataBaseHandler.KEY_MULTIATTACK, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MULTIATTACK)));
                            values.put(DataBaseHandler.KEY_ATK1NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1NAME)));
                            values.put(DataBaseHandler.KEY_ATK1DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DESC)));
                            values.put(DataBaseHandler.KEY_ATK1MOD, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1MOD)));
                            values.put(DataBaseHandler.KEY_ATK1DMG1ROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG1ROLL)));
                            values.put(DataBaseHandler.KEY_ATK1DMG1TYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG1TYPE)));
                            values.put(DataBaseHandler.KEY_ATK1DMG2ROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG2ROLL)));
                            values.put(DataBaseHandler.KEY_ATK1DMG2TYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DMG2TYPE)));
                            values.put(DataBaseHandler.KEY_ATK1AUTOROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1AUTOROLL)));
                            values.put(DataBaseHandler.KEY_ATK1ADDITIONAL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1ADDITIONAL)));
                            values.put(DataBaseHandler.KEY_ATK2NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2NAME)));
                            values.put(DataBaseHandler.KEY_ATK2DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DESC)));
                            values.put(DataBaseHandler.KEY_ATK2MOD, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2MOD)));
                            values.put(DataBaseHandler.KEY_ATK2DMG1ROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG1ROLL)));
                            values.put(DataBaseHandler.KEY_ATK2DMG1TYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG1TYPE)));
                            values.put(DataBaseHandler.KEY_ATK2DMG2ROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG2ROLL)));
                            values.put(DataBaseHandler.KEY_ATK2DMG2TYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DMG2TYPE)));
                            values.put(DataBaseHandler.KEY_ATK2AUTOROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2AUTOROLL)));
                            values.put(DataBaseHandler.KEY_ATK2ADDITIONAL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2ADDITIONAL)));
                            values.put(DataBaseHandler.KEY_ATK3NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3NAME)));
                            values.put(DataBaseHandler.KEY_ATK3DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DESC)));
                            values.put(DataBaseHandler.KEY_ATK3MOD, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3MOD)));
                            values.put(DataBaseHandler.KEY_ATK3DMG1ROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG1ROLL)));
                            values.put(DataBaseHandler.KEY_ATK3DMG1TYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG1TYPE)));
                            values.put(DataBaseHandler.KEY_ATK3DMG2ROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG2ROLL)));
                            values.put(DataBaseHandler.KEY_ATK3DMG2TYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DMG2TYPE)));
                            values.put(DataBaseHandler.KEY_ATK3AUTOROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3AUTOROLL)));
                            values.put(DataBaseHandler.KEY_ATK3ADDITIONAL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3ADDITIONAL)));
                            values.put(DataBaseHandler.KEY_ATK4NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4NAME)));
                            values.put(DataBaseHandler.KEY_ATK4DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DESC)));
                            values.put(DataBaseHandler.KEY_ATK4MOD, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4MOD)));
                            values.put(DataBaseHandler.KEY_ATK4DMG1ROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG1ROLL)));
                            values.put(DataBaseHandler.KEY_ATK4DMG1TYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG1TYPE)));
                            values.put(DataBaseHandler.KEY_ATK4DMG2ROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG2ROLL)));
                            values.put(DataBaseHandler.KEY_ATK4DMG2TYPE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DMG2TYPE)));
                            values.put(DataBaseHandler.KEY_ATK4AUTOROLL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4AUTOROLL)));
                            values.put(DataBaseHandler.KEY_ATK4ADDITIONAL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4ADDITIONAL)));
                            values.put(DataBaseHandler.KEY_REACTION1NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION1NAME)));
                            values.put(DataBaseHandler.KEY_REACTION1DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION1DESC)));
                            values.put(DataBaseHandler.KEY_ABILITY1NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1NAME)));
                            values.put(DataBaseHandler.KEY_ABILITY1DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1DESC)));
                            values.put(DataBaseHandler.KEY_ABILITY2NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY2NAME)));
                            values.put(DataBaseHandler.KEY_ABILITY2DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY2DESC)));
                            values.put(DataBaseHandler.KEY_ABILITY3NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY3NAME)));
                            values.put(DataBaseHandler.KEY_ABILITY3DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY3DESC)));
                            values.put(DataBaseHandler.KEY_ABILITY4NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY4NAME)));
                            values.put(DataBaseHandler.KEY_ABILITY4DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY4DESC)));
                            values.put(DataBaseHandler.KEY_ABILITY5NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY5NAME)));
                            values.put(DataBaseHandler.KEY_ABILITY5DESC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY5DESC)));
                            values.put(DataBaseHandler.KEY_ACROBATICS, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ACROBATICS)));
                            values.put(DataBaseHandler.KEY_ANIMALHANDLING, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ANIMALHANDLING)));
                            values.put(DataBaseHandler.KEY_ARCANA, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ARCANA)));
                            values.put(DataBaseHandler.KEY_ATHLETICS, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATHLETICS)));
                            values.put(DataBaseHandler.KEY_DECEPTION, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DECEPTION)));
                            values.put(DataBaseHandler.KEY_HISTORY, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_HISTORY)));
                            values.put(DataBaseHandler.KEY_INSIGHT, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INSIGHT)));
                            values.put(DataBaseHandler.KEY_INTIMIDATION, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTIMIDATION)));
                            values.put(DataBaseHandler.KEY_INVESTIGATION, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INVESTIGATION)));
                            values.put(DataBaseHandler.KEY_MEDICINE, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MEDICINE)));
                            values.put(DataBaseHandler.KEY_NATURE, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NATURE)));
                            values.put(DataBaseHandler.KEY_PERCEPTION, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_PERCEPTION)));
                            values.put(DataBaseHandler.KEY_PERFORMANCE, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_PERFORMANCE)));
                            values.put(DataBaseHandler.KEY_PERSUASION, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_PERSUASION)));
                            values.put(DataBaseHandler.KEY_RELIGION, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_RELIGION)));
                            values.put(DataBaseHandler.KEY_SLEIGHTOFHAND, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SLEIGHTOFHAND)));
                            values.put(DataBaseHandler.KEY_STEALTH, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STEALTH)));
                            values.put(DataBaseHandler.KEY_SURVIVAL, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SURVIVAL)));
                            values.put(DataBaseHandler.KEY_STSTR, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STSTR)));
                            values.put(DataBaseHandler.KEY_STDEX, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STDEX)));
                            values.put(DataBaseHandler.KEY_STCON, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STCON)));
                            values.put(DataBaseHandler.KEY_STINT, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STINT)));
                            values.put(DataBaseHandler.KEY_STWIS, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STWIS)));
                            values.put(DataBaseHandler.KEY_STCHA, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STCHA)));
                            values.put(DataBaseHandler.KEY_SENSES, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SENSES)));
                            values.put(DataBaseHandler.KEY_ALIGNMENT, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ALIGNMENT)));
                            values.put(DataBaseHandler.KEY_LANGUAGES, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LANGUAGES)));
                            values.put(DataBaseHandler.KEY_DMGRES, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGRES)));
                            values.put(DataBaseHandler.KEY_DMGIM, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGIM)));
                            values.put(DataBaseHandler.KEY_DMGVUL, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGVUL)));
                            values.put(DataBaseHandler.KEY_CONIM, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONIM)));
                            values.put(DataBaseHandler.KEY_BELONGSTO, encounterId);
                            startInsert(1, null, DbContentProvider.CONTENT_URI_ENCOUNTERPREP, values);
                        }
                        cursor.close();
                    }
                }
            };

            queryHandler.startQuery(
                    1, null,
                    pasteUri,
                    DataBaseHandler.PROJECTION_ENCOUNTERPREP,
                    null,
                    null,
                    null
            );
        }
        monstersToBeAdded.clear();
    }


    private void addMonster(Bundle bundle) {
        Fragment fragment;
        fragment = new MyMonsterEditFragment();
        fragment.setArguments(bundle);
        if (navMode) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ContainerFrame, fragment, "FE_MYMONSTER")
                    .addToBackStack("NAV_F")
                    .commit();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ContainerFrame, fragment, "FE_MYMONSTER")
                    .addToBackStack("FE_MYMONSTER")
                    .commit();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pagerAdapter = new MonsterViewPagerAdapter(getChildFragmentManager(), getContext(), navMode, id);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        monstersToBeAdded.clear();
        ((MainActivity) getActivity()).setFabVisibility(false);
        if (navMode) {
            ((MainActivity) getActivity()).enableNavigationDrawer();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Fiends");
        } else {
            ((MainActivity) getActivity()).disableNavigationDrawer();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(encounterName + " Preparation");
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(monstersToBeAdded.size()>0){
            menu.findItem(R.id.add_selected).setVisible(true);
        }else{
            menu.findItem(R.id.add_selected).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setFabVisibility(boolean visibility) {
        if (visibility) {
            fabMonsterVP.setVisibility(View.VISIBLE);
        } else {
            fabMonsterVP.setVisibility(View.GONE);
        }
    }

    public void setFabIcon(boolean isAdd) {
        if (isAdd) {
            fabMonsterVP.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add));
        } else {
            fabMonsterVP.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_done));
        }
    }

    public static void addMonsterToList(Uri npcUri) {
        monstersToBeAdded.add(npcUri);
    }

    public static void removeMonsterFromList(Uri npcUri) {
        monstersToBeAdded.remove(npcUri);
    }

    public boolean isNavMode() {
        return navMode;
    }
}

