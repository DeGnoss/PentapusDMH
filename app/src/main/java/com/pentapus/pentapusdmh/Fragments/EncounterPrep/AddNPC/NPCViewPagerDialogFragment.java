package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.Utils;

/**
 * Created by Koni on 4/4/16.
 */
public class NPCViewPagerDialogFragment extends Fragment {



    private static final String ENCOUNTER_ID = "encounterId";
    private static final String NAV_MODE = "navMode";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NPCViewPagerAdapter pagerAdapter;
    private FloatingActionButton fabNPCVP;
    private int id;
    private boolean navMode;
    private int encounterId;
    private static final String MODE = "modeUpdate";
    private Button bDone;

    private static int selectedType = -1;
    private static int selectedPos = -1;
    private static int highlightedPos = -1;
    private static Uri npcUri = null;


    public NPCViewPagerDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static NPCViewPagerDialogFragment newInstance(int encounterId, boolean navMode) {
        NPCViewPagerDialogFragment fragment = new NPCViewPagerDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(NAV_MODE, navMode);
        args.putInt(ENCOUNTER_ID, encounterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            encounterId = getArguments().getInt(ENCOUNTER_ID);
            navMode = getArguments().getBoolean(NAV_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.npc_viewpager_tab_layout, parent, false);
        ((MainActivity)getActivity()).setFabVisibility(false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, Utils.getStatusBarHeight(getActivity()), 0, 0);
        view.setBackgroundColor(Color.WHITE);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        fabNPCVP = (FloatingActionButton) view.findViewById(R.id.fabImageVP);

        bDone = (Button) view.findViewById(R.id.bDone);
        if(navMode){
            bDone.setVisibility(View.GONE);
        }else {
            bDone.setVisibility(View.VISIBLE);
            bDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pasteNPC(npcUri);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        fabNPCVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(MODE, false);
                addNPC(bundle);
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
                        fabNPCVP.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fabNPCVP.setVisibility(View.GONE);
                        break;
                    default:
                        fabNPCVP.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
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


    private void addNPC(Bundle bundle) {
        Fragment fragment;
        fragment = new MyNPCEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment, "FE_MYNPC")
                .addToBackStack("FE_MYNPC")
                .commit();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pagerAdapter = new NPCViewPagerAdapter(getChildFragmentManager(), getContext(), id);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static int getSelectedType() {
        return selectedType;
    }

    public static void setSelectedType(int selectedType) {
        NPCViewPagerDialogFragment.selectedType = selectedType;
    }

    public static int getSelectedPos() {
        return selectedPos;
    }

    public static void setSelectedPos(int selectedPos) {
        NPCViewPagerDialogFragment.selectedPos = selectedPos;
    }

    public static int getHighlightedPos() {
        return highlightedPos;
    }

    public static void setHighlightedPos(int highlightedPos) {
        NPCViewPagerDialogFragment.highlightedPos = highlightedPos;
    }

    public static Uri getNPCUri() {
        return npcUri;
    }

    public static void setNPCUri(Uri npcUri) {
        NPCViewPagerDialogFragment.npcUri = npcUri;
    }

    public void setFabVisibility(boolean visibility){
        if(visibility){
            fabNPCVP.setVisibility(View.VISIBLE);
        }else{
            fabNPCVP.setVisibility(View.GONE);
        }
    }
}

