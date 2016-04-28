package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageFragmentPagerAdapter;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageGridAdapter;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.MonsterEditFragment;
import com.pentapus.pentapusdmh.R;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Koni on 4/4/16.
 */
public class MonsterViewPagerDialogFragment extends Fragment {

    private static final String ARG_PAGE = "ARG_PAGE";
    private static final String ENCOUNTER_ID = "encounterId";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MonsterViewPagerAdapter pagerAdapter;
    private FloatingActionButton fabImageVP;
    private int id;
    private int encounterId;
    private static final String MODE = "modeUpdate";
    private Button bDone;

    private static int selectedType = -1;
    private static int selectedPos = -1;
    private static int highlightedPos = -1;
    private static Uri monsterUri = null;


    public MonsterViewPagerDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static MonsterViewPagerDialogFragment newInstance(int encounterId) {
        MonsterViewPagerDialogFragment fragment = new MonsterViewPagerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ENCOUNTER_ID, encounterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            encounterId = getArguments().getInt("encounterId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.monster_viewpager_tab_layout, parent, false);
        view.setBackgroundColor(Color.WHITE);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        fabImageVP = (FloatingActionButton) view.findViewById(R.id.fabImageVP);

        bDone = (Button) view.findViewById(R.id.bDone);
        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasteMonster(monsterUri);
                getFragmentManager().popBackStack();
            }
        });

        fabImageVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(MODE, false);
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
                        fabImageVP.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fabImageVP.setVisibility(View.GONE);
                        break;
                    default:
                        fabImageVP.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }


    private void pasteMonster(Uri pasteUri) {
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


    private void addMonster(Bundle bundle) {
        Fragment fragment;
        fragment = new MyMonsterEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment, "FE_MYMONSTER")
                .addToBackStack("FE_MYMONSTER")
                .commit();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pagerAdapter = new MonsterViewPagerAdapter(getChildFragmentManager(), getContext(), id);
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
        MonsterViewPagerDialogFragment.selectedType = selectedType;
    }

    public static int getSelectedPos() {
        return selectedPos;
    }

    public static void setSelectedPos(int selectedPos) {
        MonsterViewPagerDialogFragment.selectedPos = selectedPos;
    }

    public static int getHighlightedPos() {
        return highlightedPos;
    }

    public static void setHighlightedPos(int highlightedPos) {
        MonsterViewPagerDialogFragment.highlightedPos = highlightedPos;
    }

    public static Uri getMonsterUri() {
        return monsterUri;
    }

    public static void setMonsterUri(Uri monsterUri) {
        MonsterViewPagerDialogFragment.monsterUri = monsterUri;
    }
}
//Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + monsterId);

