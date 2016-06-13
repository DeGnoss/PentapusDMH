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
        viewPager.setOffscreenPageLimit(1);
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

