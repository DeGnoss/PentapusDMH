package com.pentapus.pentapusdmh.Fragments.Spells;

import android.content.AsyncQueryHandler;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit.CrFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit.TraitsFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit.TraitsPage;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Koni on 4/4/16.
 */
public class SpellViewPagerDialogFragment extends Fragment{

    private static final String NAV_MODE = "navMode";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SpellViewPagerAdapter pagerAdapter;
    private FloatingActionButton fabSpellVP;
    private int id, mode;
    private boolean navMode;
    private static final String MODE = "modeUpdate";
    private Button bDone;
    private String level, scclass;

    private static int selectedType = -1;
    private static int selectedPos = -1;
    private static int highlightedPos = -1;

    private ArrayList<String> selectionList = new ArrayList<>();
    private HashMap<String, String> selectedspells = new HashMap<>();


    public SpellViewPagerDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static SpellViewPagerDialogFragment newInstance(boolean navMode, int mode, ArrayList<String> selectionList, String level, String scClass) {
        SpellViewPagerDialogFragment fragment = new SpellViewPagerDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(NAV_MODE, navMode);
        args.putInt("mode", mode);
        args.putStringArrayList("selectionList", selectionList);
        args.putString("level", level);
        args.putString("scclass", scClass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            navMode = getArguments().getBoolean(NAV_MODE);
            mode = getArguments().getInt("mode");
            if(mode == 1){
                selectionList = getArguments().getStringArrayList("selectionList");
            }else if(mode == 2){
                selectedspells = (HashMap<String, String>)getArguments().getSerializable("selectedspells");
            }
            level = getArguments().getString("level");
            scclass = getArguments().getString("scclass");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spell_viewpager_tab_layout, parent, false);

        //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        //params.setMargins(0, Utils.getStatusBarHeight(getActivity()), 0, 0);
        //view.setBackgroundColor(Color.WHITE);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        fabSpellVP = (FloatingActionButton) view.findViewById(R.id.fabImageVP);
/*
        bDone = (Button) view.findViewById(R.id.bDone);
        if(navMode){
            bDone.setVisibility(View.GONE);
        }else{
            bDone.setVisibility(View.VISIBLE);
            bDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Add onclicklistener methods
                    //pasteMonster(monsterUri);
                    //getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }*/

        fabSpellVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(MODE, false);
                addSpell(bundle);
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
                        fabSpellVP.setVisibility(View.VISIBLE);
                        //((MainActivity)getActivity()).closeSearchView();
                        break;
                    case 1:
                        fabSpellVP.setVisibility(View.GONE);
                        //((MainActivity)getActivity()).closeSearchView();
                        break;
                    default:
                        fabSpellVP.setVisibility(View.GONE);
                        //((MainActivity)getActivity()).closeSearchView();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }

    @Override
    public void onDestroy(){
        ((MainActivity) getActivity()).enableNavigationDrawer();
        if(mode == 1){
            Bundle results = new Bundle();
            results.putStringArrayList("selectedspells", ((PHBSpellTableFragment)pagerAdapter.getRegisteredFragment(1)).getSelectedSpells());
            ((TraitsFragment) getTargetFragment()).onDialogResult(
                    getTargetRequestCode(), -2, results);
        }else if(mode == 2){
            Bundle results = new Bundle();
            results.putSerializable("spellcounter", ((PHBSpellTableFragment)pagerAdapter.getRegisteredFragment(1)).getSpellCounter());
            ((TraitsFragment) getTargetFragment()).onDialogResult(
                    getTargetRequestCode(), -2, results);
        }
        super.onDestroy();
    }

    //only when not in navmode
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
                       /* ContentValues values = new ContentValues();
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
                        values.put(DataBaseHandler.KEY_IDENTIFIER, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_IDENTIFIER)));
                        values.put(DataBaseHandler.KEY_BELONGSTO, encounterId);
                        startInsert(1, null, DbContentProvider.CONTENT_URI_ENCOUNTERPREP, values); */
                    }
                    cursor.close();
                }
            }
        };

        //FIXME adjust projection
        queryHandler.startQuery(
                1, null,
                pasteUri,
                DataBaseHandler.PROJECTION_ENCOUNTERPREP,
                null,
                null,
                null
        );
    }


    private void addSpell(Bundle bundle) {
        /*Fragment fragment;
        fragment = new MySpellEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.drawer_layout, fragment, "FE_MYSPELL")
                .addToBackStack("FE_MYSPELL")
                .commit();*/
        Toast.makeText(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle args = new Bundle();
        if(mode == 1){
            args.putStringArrayList("selectionList", selectionList);
        }else if(mode == 2){
            args.putSerializable("selectedspells", selectedspells);
        }
        pagerAdapter = new SpellViewPagerAdapter(getChildFragmentManager(), getContext(), id, mode, args, level, scclass);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(2);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.play_mode).setVisible(false);
        menu.findItem(R.id.spell_book).setVisible(false);
        getActivity().supportInvalidateOptionsMenu();
        getActivity().invalidateOptionsMenu();

/*
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            String pasteString = String.valueOf(itemPaste.getText());
            if (pasteUri == null) {
                pasteUri = Uri.parse(pasteString);
            }
            if (pasteUri != null) {
                if (DbContentProvider.ENCOUNTERPREP.equals(getContext().getContentResolver().getType(pasteUri))) {
                    menu.findItem(R.id.menu_paste).setVisible(true);
                }
            }
        }*/
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setFabVisibility(false);
        if(navMode){
            ((MainActivity)getActivity()).enableNavigationDrawer();
        }else{
            ((MainActivity)getActivity()).disableNavigationDrawer();
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Spells");
        getActivity().invalidateOptionsMenu();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static int getSelectedType() {
        return selectedType;
    }

    public static void setSelectedType(int selectedType) {
        SpellViewPagerDialogFragment.selectedType = selectedType;
    }

    public static int getSelectedPos() {
        return selectedPos;
    }

    public static void setSelectedPos(int selectedPos) {
        SpellViewPagerDialogFragment.selectedPos = selectedPos;
    }

    public static int getHighlightedPos() {
        return highlightedPos;
    }

    public static void setHighlightedPos(int highlightedPos) {
        SpellViewPagerDialogFragment.highlightedPos = highlightedPos;
    }

    public void setFabVisibility(boolean visibility){
        if(visibility){
            fabSpellVP.setVisibility(View.VISIBLE);
        }else{
            fabSpellVP.setVisibility(View.GONE);
        }
    }

    public SpellViewPagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }
}
//Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + monsterId);

