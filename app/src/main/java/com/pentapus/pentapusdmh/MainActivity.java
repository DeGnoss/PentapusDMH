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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
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
import com.pentapus.pentapusdmh.Fragments.Campaign.CampaignTableFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.MonsterViewPagerDialogFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC.NPCViewPagerDialogFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.EncounterFragment;
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterTableFragment;
import com.pentapus.pentapusdmh.Fragments.PC.PcTableFragment;
import com.pentapus.pentapusdmh.Fragments.Session.SessionTableFragment;
import com.pentapus.pentapusdmh.Fragments.SettingsFragment;
import com.pentapus.pentapusdmh.Fragments.Spells.MySpellTableFragment;
import com.pentapus.pentapusdmh.Fragments.Spells.PHBSpellTableFragment;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean pressedTwice = false;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fab;
    private FilterManager filterManager;
    private static MainActivity instance;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        copyAssets();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.opendrawer, R.string.closedrawer);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        // tintNavigationViewItems(navigationView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClick(v);
            }
        });

        filterManager = new FilterManager();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("", ""));

        SessionTableFragment ftable = new SessionTableFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerFrame, ftable, "FT_SESSION")
                .addToBackStack("FT_SESSION")
                .commit();
    }


    private void tintNavigationViewItems(NavigationView navigationView) {
        //pcs
        Drawable oldIcon = navigationView
                .getMenu()
                .findItem(R.id.nav_party)
                .getIcon();

        if (!(oldIcon instanceof BitmapDrawable)) {
            return;
        }

        Bitmap immutable = ((BitmapDrawable) oldIcon).getBitmap();
        Bitmap mutable = immutable.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(mutable);
        Paint p = new Paint();
        p.setColor(Color.parseColor("#3F51B5"));
        p.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#3F51B5"), PorterDuff.Mode.MULTIPLY));
        c.drawBitmap(mutable, 0.f, 0.f, p);
        BitmapDrawable newIcon = new BitmapDrawable(getResources(), mutable);

        navigationView
                .getMenu()
                .findItem(R.id.nav_party)
                .setIcon(newIcon);

        //monsters
        oldIcon = navigationView
                .getMenu()
                .findItem(R.id.nav_monsters)
                .getIcon();

        if (!(oldIcon instanceof BitmapDrawable)) {
            return;
        }

        immutable = ((BitmapDrawable) oldIcon).getBitmap();
        mutable = immutable.copy(Bitmap.Config.ARGB_8888, true);
        c = new Canvas(mutable);
        p = new Paint();
        p.setColor(Color.parseColor("#F44336"));
        p.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.MULTIPLY));
        c.drawBitmap(mutable, 0.f, 0.f, p);
        newIcon = new BitmapDrawable(getResources(), mutable);

        navigationView
                .getMenu()
                .findItem(R.id.nav_party)
                .setIcon(newIcon);

        //npcs
        oldIcon = navigationView
                .getMenu()
                .findItem(R.id.nav_monsters)
                .getIcon();

        if (!(oldIcon instanceof BitmapDrawable)) {
            return;
        }

        immutable = ((BitmapDrawable) oldIcon).getBitmap();
        mutable = immutable.copy(Bitmap.Config.ARGB_8888, true);
        c = new Canvas(mutable);
        p = new Paint();
        p.setColor(Color.parseColor("#4caf50"));
        p.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#4caf50"), PorterDuff.Mode.MULTIPLY));
        c.drawBitmap(mutable, 0.f, 0.f, p);
        newIcon = new BitmapDrawable(getResources(), mutable);

        navigationView
                .getMenu()
                .findItem(R.id.nav_party)
                .setIcon(newIcon);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterManager.setQuery(newText);
               /* if("F_SPELL_PAGER".equals(getCurrentFragmentTag())){
                    ((PHBSpellTableFragment)((SpellViewPagerDialogFragment)getSupportFragmentManager().findFragmentByTag("F_SPELL_PAGER")).getPagerAdapter().getRegisteredFragment(1)).filterData(newText);
                    ((MySpellTableFragment)((SpellViewPagerDialogFragment)getSupportFragmentManager().findFragmentByTag("F_SPELL_PAGER")).getPagerAdapter().getRegisteredFragment(0)).filterData(newText);

                }*/
                return false;
            }
        });
        return true;
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

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/ /*else if (id == R.id.campaign_settings) {
            CampaignTableFragment myFragment = (CampaignTableFragment) getSupportFragmentManager().findFragmentByTag("FT_CAMPAIGN");
            if (myFragment != null) {
                if (!myFragment.isVisible()) {
                    CampaignTableFragment ftable = new CampaignTableFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.ContainerFrame, ftable, "FT_CAMPAIGN")
                            .addToBackStack("FT_CAMPAIGN")
                            .commit();
                }
                return true;
            } else {
                CampaignTableFragment ftable = new CampaignTableFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ContainerFrame, ftable, "FT_CAMPAIGN")
                        .addToBackStack("FT_CAMPAIGN")
                        .commit();
                return true;
            }

        }  else*/
        if (id == R.id.play_mode) {
            TrackerFragment ftable = new TrackerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ContainerFrame, ftable, "F_TRACKER")
                    .addToBackStack("F_TRACKER")
                    .commit();
            return true;
        }/* else if (id == R.id.player_settings) {

            Bundle bundle = new Bundle();
            bundle.putInt("campaignId", SharedPrefsHelper.loadCampaignId(this));
            bundle.putString("campaignName", SharedPrefsHelper.loadCampaignName(this));
            loadPCs(bundle);
        }*/ else if (id == R.id.menu_paste) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            String pasteString = String.valueOf(itemPaste.getText());
            if (pasteUri == null) {
                pasteUri = Uri.parse(pasteString);
            }
            pasteEntry(pasteUri);

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
                        values.put(DataBaseHandler.KEY_TYPE, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)));
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
                        values.put(DataBaseHandler.KEY_TYPE, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)));
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
                        values.put(DataBaseHandler.KEY_TYPE, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)));
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
                    fm.popBackStack();
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

                } else {
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


    private void loadPCs(Bundle bundle) {
        Fragment fragment;
        fragment = new PcTableFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerFrame, fragment, "FT_PC")
                .addToBackStack("FT_PC")
                .commit();
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

    public boolean isNavMode(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        String str = backEntry.getName();
        boolean topBackEntryIsNav = false;
        if (str.equals("NAV_F_PC")||str.equals("NAV_F")||str.equals("NAV_F")||str.equals("NAV_F")||str.equals("NAV_F")) {
            topBackEntryIsNav = true;
        }
        return topBackEntryIsNav;
    }

    public void removeNavModeFragments(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        String str = backEntry.getName();
        switch(str){
            case "NAV_F_MONSTER":
                Fragment fragment = fragmentManager.findFragmentByTag("F_MONSTER_PAGER");
                FragmentManager childFM = fragment.getChildFragmentManager();
                while (childFM.getBackStackEntryCount() > 0) {
                    childFM.popBackStack();
                }
                fragmentManager.popBackStack();
                removeNavModeFragments();
                break;
            case "NAV_F_NPC":
                fragment = fragmentManager.findFragmentByTag("F_NPC_PAGER");
                childFM = fragment.getChildFragmentManager();
                while (childFM.getBackStackEntryCount() > 0) {
                    childFM.popBackStack();
                }
                fragmentManager.popBackStack();
                removeNavModeFragments();
                break;
            case "NAV_F_SPELLS":
                fragment = fragmentManager.findFragmentByTag("F_SPELL_PAGER");
                childFM = fragment.getChildFragmentManager();
                while (childFM.getBackStackEntryCount() > 0) {
                    childFM.popBackStack();
                }
                fragmentManager.popBackStack();
                removeNavModeFragments();
                break;
            case "NAV_F_PC":
                fragmentManager.popBackStack();
                removeNavModeFragments();
                break;
            case "NAV_F_CAMPAIGN":
                fragmentManager.popBackStack();
                removeNavModeFragments();
                break;
            case "NAV_F_SETTINGS":
                fragmentManager.popBackStack();
                removeNavModeFragments();
                break;
            default:
                break;
        }
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
                removeNavModeFragments();
                //Fragment f = getSupportFragmentManager().findFragmentById(R.id.FrameTop);
                //((SessionTableFragment)f).loadNPCTable(bundle);

                //fragment.setSharedElementEnterTransition(new FabTransition());
                //fragment.setEnterTransition(new Slide(Gravity.TOP));
                //fragment.setSharedElementReturnTransition(new FabTransition());
                //Fragment f = getSupportFragmentManager().findFragmentById(R.id.FrameTop);
                fragmentManager.beginTransaction()
                        .replace(R.id.ContainerFrame, fragment, "FT_PC")
                        .addToBackStack("NAV_F_PC")
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
                removeNavModeFragments();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.ContainerFrame, fragment, "F_MONSTER_PAGER")
                        .addToBackStack("NAV_F_MONSTER")
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
                removeNavModeFragments();

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.ContainerFrame, fragment, "F_NPC_PAGER")
                        .addToBackStack("NAV_F_NPC")
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
                removeNavModeFragments();

                fragmentManager.beginTransaction().replace(R.id.ContainerFrame, fragment, "FT_CAMPAIGN")
                        .addToBackStack("NAV_F_CAMPAIGN")
                        .commit();
                break;
            case R.id.nav_spells:
                fragmentClass = SpellViewPagerDialogFragment.class;
                bundle.putBoolean("navMode", true);
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removeNavModeFragments();

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.ContainerFrame, fragment, "F_SPELL_PAGER")
                        .addToBackStack("NAV_F_SPELLS")
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
                removeNavModeFragments();

                fragmentManager.beginTransaction().replace(R.id.ContainerFrame, fragment, "F_SETTINGS")
                        .addToBackStack("NAV_F_SETTINGS")
                        .commit();
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void enableNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toggle.setDrawerIndicatorEnabled(true);
    }

    public void disableNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.setDrawerIndicatorEnabled(false);
    }

    public void onFabClick(View view) {
        if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof SessionTableFragment) {
            ((SessionTableFragment) getSupportFragmentManager().findFragmentByTag("FT_SESSION")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof EncounterTableFragment) {
            ((EncounterTableFragment) getSupportFragmentManager().findFragmentByTag("FT_ENCOUNTER")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof EncounterFragment) {
            ((EncounterFragment) getSupportFragmentManager().findFragmentByTag("F_ENCOUNTER")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof CampaignTableFragment) {
            ((CampaignTableFragment) getSupportFragmentManager().findFragmentByTag("FT_CAMPAIGN")).onFabClick();
        } else if (getSupportFragmentManager().findFragmentById(R.id.ContainerFrame) instanceof PcTableFragment) {
            ((PcTableFragment) getSupportFragmentManager().findFragmentByTag("FT_PC")).onFabClick();
        }
    }

    public void setFabVisibility(boolean visibility) {
        if (visibility) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    public static FilterManager getFilterManager() {
        return instance.filterManager; // return the observable class
    }

    public static void closeSearchView(){
        instance.searchView.setIconified(true);
        instance.searchView.setIconified(true);
    }
}
