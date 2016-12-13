package com.pentapus.pentapusdmh;

import android.content.AsyncQueryHandler;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.Campaign.CampaignEditFragment;
import com.pentapus.pentapusdmh.Fragments.Campaign.CampaignTableFragment;
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterEditFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.MonsterViewPagerDialogFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.MyMonsterEditFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC.MyNPCEditFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC.NPCViewPagerDialogFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.EncounterFragment;
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterTableFragment;
import com.pentapus.pentapusdmh.Fragments.PC.PcEditFragment;
import com.pentapus.pentapusdmh.Fragments.PC.PcTableFragment;
import com.pentapus.pentapusdmh.Fragments.Session.SessionEditFragment;
import com.pentapus.pentapusdmh.Fragments.Session.SessionTableFragment;
import com.pentapus.pentapusdmh.Fragments.Preferences.SettingsFragment;
import com.pentapus.pentapusdmh.Fragments.Spells.SpellViewPagerDialogFragment;
import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerFragment;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.pentapus.pentapusdmh.R.drawable.abc_ic_ab_back_mtrl_am_alpha;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean pressedTwice = false;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fab;
    private static MainActivity instance;
    private SearchView searchView;
    private Toolbar toolbar;
    private String searchViewQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        copyAssets();
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setToolBarBackButton(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.opendrawer, R.string.closedrawer);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(this);
        }
        // tintNavigationViewItems(navigationView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClick(v);
            }
        });


        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("", ""));

        if (savedInstanceState == null) {
            SessionTableFragment ftable = new SessionTableFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ContainerFrame, ftable, "FT_SESSION")
                    .addToBackStack("FT_SESSION")
                    .commit();
        } else {
            Fragment fragment = (SessionTableFragment) getSupportFragmentManager().findFragmentByTag("FT_SESSION");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.play_mode) {
            TrackerFragment ftable = new TrackerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ContainerFrame, ftable, "F_TRACKER")
                    .addToBackStack("F_TRACKER")
                    .commit();
            return true;
        } else if (id == R.id.menu_paste) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            String pasteString = String.valueOf(itemPaste.getText());
            if (pasteUri == null) {
                pasteUri = Uri.parse(pasteString);
            }
            pasteEntry(pasteUri);
            return true;
        } else if (id == R.id.spell_book) {
            Fragment fragment = null;
            Class fragmentClass = SpellViewPagerDialogFragment.class;
            Bundle bundle = new Bundle();
            bundle.putBoolean("navMode", false);
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ContainerFrame, fragment, "F_SPELL_PAGER")
                    .addToBackStack("F_SPELL_PAGER")
                    .commit();
            return true;
        } else if (id == R.id.add_selected) {
            if (getSupportFragmentManager().findFragmentByTag("F_NPC_PAGER") != null) {
                ((NPCViewPagerDialogFragment) getSupportFragmentManager().findFragmentByTag("F_NPC_PAGER")).pasteNPC();
            } else if (getSupportFragmentManager().findFragmentByTag("F_MONSTER_PAGER") != null) {
                ((MonsterViewPagerDialogFragment) getSupportFragmentManager().findFragmentByTag("F_MONSTER_PAGER")).pasteMonster();
            }
            getSupportFragmentManager().popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }

    private void pasteEntry(Uri pasteUri) {
        // If the clipboard contains a URI reference
        if (pasteUri != null) {
            // Is this a content URI?
            ContentResolver cr = getContentResolver();
            String uriMimeType = cr.getType(pasteUri);
            if (uriMimeType != null) {
                switch (uriMimeType) {
                    case DbContentProvider.ENCOUNTERPREP:
                        pasteNPC(pasteUri);
                        break;
                    case DbContentProvider.ENCOUNTER:
                        pasteEncounter(pasteUri);
                        break;
                    case DbContentProvider.SESSION:
                        pasteSession(pasteUri);
                        break;
                    case DbContentProvider.PC:
                        pastePc(pasteUri);
                        break;
                    default:
                        break;
                }

            }
        }
    }

    private void pasteNPC(Uri pasteUri) {
        final int encounterId = ((EncounterFragment) getSupportFragmentManager().findFragmentByTag("F_ENCOUNTER")).getEncounterId();
        final AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {

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
                        values.put(DataBaseHandler.KEY_IDENTIFIER, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_IDENTIFIER)));
                        values.put(DataBaseHandler.KEY_BELONGSTO, encounterId);
                        startInsert(1, null, DbContentProvider.CONTENT_URI_ENCOUNTERPREP, values);
                    }
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


    private void pasteEncounter(Uri pasteUri) {
        final String oldUri = pasteUri.getPath();
        final int sessionId = ((EncounterTableFragment) getSupportFragmentManager().findFragmentByTag("FT_ENCOUNTER")).getSessionId();

        final AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
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
                        values.put(DataBaseHandler.KEY_BELONGSTO, sessionId);
                        startInsert(3, null, DbContentProvider.CONTENT_URI_ENCOUNTER, values);
                    }
                }
            }

            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                int newId = (int) ContentUris.parseId(uri);

                int oldId = Integer.parseInt(oldUri.substring(oldUri.lastIndexOf('/') + 1));
                nestedPasteNPC(oldId, newId);
            }

        };

        queryHandler.startQuery(
                3, null,
                pasteUri,
                DataBaseHandler.PROJECTION_ENCOUNTER,
                null,
                null,
                null
        );
    }

    private void nestedPasteNPC(int oldId, final int newId) {
        final AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
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
                        values.put(DataBaseHandler.KEY_IDENTIFIER, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_IDENTIFIER)));
                        values.put(DataBaseHandler.KEY_BELONGSTO, newId);
                        startInsert(2, null, DbContentProvider.CONTENT_URI_ENCOUNTERPREP, values);
                    }
                }
            }
        };

        String[] selectionArgs = new String[]{String.valueOf(oldId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";

        queryHandler.startQuery(
                2, null,
                DbContentProvider.CONTENT_URI_ENCOUNTERPREP,
                DataBaseHandler.PROJECTION_ENCOUNTERPREP,
                selection,
                selectionArgs,
                null
        );
    }

    private void pasteSession(Uri pasteUri) {
        final String oldUri = pasteUri.getPath();
        final int campaignId = ((SessionTableFragment) getSupportFragmentManager().findFragmentByTag("FT_SESSION")).getCampaignId();

        final AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
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
                        values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);
                        startInsert(5, null, DbContentProvider.CONTENT_URI_SESSION, values);
                    }
                }
            }

            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                int newId = (int) ContentUris.parseId(uri);
                int oldId = Integer.parseInt(oldUri.substring(oldUri.lastIndexOf('/') + 1));
                nestedPasteEncounter(oldId, newId);
            }

        };

        queryHandler.startQuery(
                5, null,
                pasteUri,
                DataBaseHandler.PROJECTION_SESSION,
                null,
                null,
                null
        );
    }

    private void nestedPasteEncounter(int oldId, final int newId) {
        final List<Integer> oldIds = new ArrayList<>();

        final AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                if (cursor == null) {
                    // Some providers return null if an error occurs whereas others throw an exception
                } else if (cursor.getCount() < 1) {
                    // No matches found
                } else {
                    while (cursor.moveToNext()) {
                        oldIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID)));
                        ContentValues values = new ContentValues();
                        values.put(DataBaseHandler.KEY_NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
                        values.put(DataBaseHandler.KEY_INFO, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
                        values.put(DataBaseHandler.KEY_BELONGSTO, newId);
                        startInsert(4, null, DbContentProvider.CONTENT_URI_ENCOUNTER, values);
                    }
                }
            }

            int i = 0;

            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                int newId = (int) ContentUris.parseId(uri);
                nestedPasteNPC(oldIds.get(i), newId);
                i++;
            }
        };

        String[] selectionArgs = new String[]{String.valueOf(oldId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";

        queryHandler.startQuery(
                4, null,
                DbContentProvider.CONTENT_URI_ENCOUNTER,
                DataBaseHandler.PROJECTION_ENCOUNTER,
                selection,
                selectionArgs,
                null
        );
    }


    private void pastePc(final Uri pasteUri) {
        final int campaignId = ((PcTableFragment) getSupportFragmentManager().findFragmentByTag("FT_PC")).getCampaignId();

        final AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {

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
                        values.put(DataBaseHandler.KEY_ICON, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
                        values.put(DataBaseHandler.KEY_IDENTIFIER, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_IDENTIFIER)));
                        values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);
                        startInsert(1, null, DbContentProvider.CONTENT_URI_PC, values);
                    }
                }
            }
        };

        queryHandler.startQuery(
                1, null,
                pasteUri,
                DataBaseHandler.PROJECTION_PC,
                null,
                null,
                null
        );
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                if ("F_TRACKER".equals(getCurrentFragmentTag())) {
                    pressedTwice = false;
                    new AlertDialog.Builder(this)
                            .setTitle("Exit Tracker")
                            .setMessage("Are you sure you want to exit the Tracker? The encounter will be reset.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    enableNavigationDrawer();
                                    fm.popBackStack();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .show();
                } else if ("FT_SESSION".equals(getCurrentFragmentTag())) {
                    if (pressedTwice) {
                        pressedTwice = false;
                        fm.popBackStack();
                        fm.popBackStack();
                        super.onBackPressed();
                    } else {
                        pressedTwice = true;
                    }
                } else if ("F_MONSTER_PAGER".equals(getCurrentFragmentTag())) {
                    Fragment fragment = fm.findFragmentByTag("F_MONSTER_PAGER");
                    FragmentManager childFM = fragment.getChildFragmentManager();
                    while (childFM.getBackStackEntryCount() > 0) {
                        childFM.popBackStack();
                    }
                    enableNavigationDrawer();
                    fm.popBackStack();
                    Log.d("FragmentList", getSupportFragmentManager().getFragments().toString());

                } else if ("F_SPELL_PAGER".equals(getCurrentFragmentTag())) {
                    Fragment fragment = fm.findFragmentByTag("F_SPELL_PAGER");
                    FragmentManager childFM = fragment.getChildFragmentManager();
                    while (childFM.getBackStackEntryCount() > 0) {
                        childFM.popBackStack();
                    }
                    enableNavigationDrawer();
                    fm.popBackStack();
                    Log.d("FragmentList", getSupportFragmentManager().getFragments().toString());

                } else if ("F_IMAGE_PAGER".equals(getCurrentFragmentTag())) {
                    Fragment fragment = fm.findFragmentByTag("F_IMAGE_PAGER");
                    FragmentManager childFM = fragment.getChildFragmentManager();
                    while (childFM.getBackStackEntryCount() > 0) {
                        childFM.popBackStack();
                    }
                    fm.popBackStack();
                    Log.d("FragmentList", getSupportFragmentManager().getFragments().toString());

                }else {
                    pressedTwice = false;
                    fm.popBackStack();
                    Log.d("Backstack:", String.valueOf(fm.getBackStackEntryCount()));
                    Log.d("FragmentList", getSupportFragmentManager().getFragments().toString());
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    private String getCurrentFragmentTag() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            return fragmentTag;
        } else {
            return null;
        }
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("icons");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open("icons/" + filename);
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("IconDir", Context.MODE_PRIVATE);
                File outFile = new File(directory, filename);
                //File outFile = new File(getExternalFilesDir(null), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public boolean isNavMode() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        String str = backEntry.getName();
        return str.equals("NAV_F");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass;
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (id) {
            case R.id.nav_party:
                fragmentClass = PcTableFragment.class;
                bundle.putInt("campaignId", SharedPrefsHelper.loadCampaignId(this));
                bundle.putString("campaignName", SharedPrefsHelper.loadCampaignName(this));
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isNavMode()) {
                    fragmentManager.popBackStack();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.ContainerFrame, fragment, "FT_PC")
                        .addToBackStack("NAV_F")
                        .commit();
                break;

            case R.id.nav_monsters:
                fragmentClass = MonsterViewPagerDialogFragment.class;
                bundle.putBoolean("navMode", true);
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isNavMode()) {
                    fragmentManager.popBackStack();
                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.ContainerFrame, fragment, "F_MONSTER_PAGER")
                        .addToBackStack("NAV_F")
                        .commit();
                break;

            case R.id.nav_npc:
                fragmentClass = NPCViewPagerDialogFragment.class;
                bundle.putBoolean("navMode", true);
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isNavMode()) {
                    fragmentManager.popBackStack();
                }
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.ContainerFrame, fragment, "F_NPC_PAGER")
                        .addToBackStack("NAV_F")
                        .commit();
                break;

            case R.id.nav_campaign:
                fragmentClass = CampaignTableFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isNavMode()) {
                    fragmentManager.popBackStack();
                }
                fragmentManager.beginTransaction().replace(R.id.ContainerFrame, fragment, "FT_CAMPAIGN")
                        .addToBackStack("NAV_F")
                        .commit();
                break;
            case R.id.nav_spells:
                fragmentClass = SpellViewPagerDialogFragment.class;
                bundle.putBoolean("navMode", true);
                bundle.putInt("mode", 0);
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isNavMode()) {
                    fragmentManager.popBackStack();
                }
                transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.ContainerFrame, fragment, "F_SPELL_PAGER")
                        .addToBackStack("NAV_F")
                        .commit();
                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isNavMode()) {
                    fragmentManager.popBackStack();
                }
                fragmentManager.beginTransaction().replace(R.id.ContainerFrame, fragment, "F_SETTINGS")
                        .addToBackStack("NAV_F")
                        .commit();
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    public void enableNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        toggle.setDrawerIndicatorEnabled(true);
    }

    public void disableNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        toggle.setDrawerIndicatorEnabled(false);
    }

    public void onFabClick(View view) {
        if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof SessionTableFragment) {
            ((SessionTableFragment) getSupportFragmentManager().findFragmentByTag("FT_SESSION")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof SessionEditFragment) {
            ((SessionEditFragment) getSupportFragmentManager().findFragmentByTag("FE_SESSION")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof EncounterTableFragment) {
            ((EncounterTableFragment) getSupportFragmentManager().findFragmentByTag("FT_ENCOUNTER")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof EncounterEditFragment) {
            ((EncounterEditFragment) getSupportFragmentManager().findFragmentByTag("FE_ENCOUNTER")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof EncounterFragment) {
            ((EncounterFragment) getSupportFragmentManager().findFragmentByTag("F_ENCOUNTER")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof CampaignTableFragment) {
            ((CampaignTableFragment) getSupportFragmentManager().findFragmentByTag("FT_CAMPAIGN")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof CampaignEditFragment) {
            ((CampaignEditFragment) getSupportFragmentManager().findFragmentByTag("FE_CAMPAIGN")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof PcTableFragment) {
            ((PcTableFragment) getSupportFragmentManager().findFragmentByTag("FT_PC")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof PcEditFragment) {
            ((PcEditFragment) getSupportFragmentManager().findFragmentByTag("FE_PC")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof MyMonsterEditFragment) {
            ((MyMonsterEditFragment) getSupportFragmentManager().findFragmentByTag("FE_MYMONSTER")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof MyNPCEditFragment) {
            ((MyNPCEditFragment) getSupportFragmentManager().findFragmentByTag("FE_MYNPC")).onFabClick();
        }
    }

    public void setFabVisibility(boolean visibility) {
        if (visibility) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    public void setFabIcon(boolean isAdd) {
        if (isAdd) {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done));
        }
    }

    public void closeSearchView() {
        if (searchView != null) {
            searchView.setIconified(true);
            searchView.setIconified(true);
        }
    }

    public void setToolBarBackButton(boolean isVisible) {
        if (isVisible) {
            toolbar.setNavigationIcon(abc_ic_ab_back_mtrl_am_alpha);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {

        }
    }
}
