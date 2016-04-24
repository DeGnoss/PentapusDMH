package com.pentapus.pentapusdmh;

import android.content.AsyncQueryHandler;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.Campaign.CampaignTableFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.CursorRecyclerViewAdapter;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.EncounterFragment;
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterTableFragment;
import com.pentapus.pentapusdmh.Fragments.PC.PcTableFragment;
import com.pentapus.pentapusdmh.Fragments.Session.SessionTableFragment;
import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerFragment;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("", ""));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SessionTableFragment ftable = new SessionTableFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, ftable, "FT_SESSION")
                .commit();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.campaign_settings) {
            CampaignTableFragment myFragment = (CampaignTableFragment) getSupportFragmentManager().findFragmentByTag("FT_CAMPAIGN");
            if (myFragment != null) {
                if (!myFragment.isVisible()) {
                    CampaignTableFragment ftable = new CampaignTableFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FrameTop, ftable, "FT_CAMPAIGN")
                            .addToBackStack("FT_CAMPAIGN")
                            .commit();
                }
                return true;
            } else {
                CampaignTableFragment ftable = new CampaignTableFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FrameTop, ftable, "FT_CAMPAIGN")
                        .addToBackStack("FT_CAMPAIGN")
                        .commit();
                return true;
            }

        } else if (id == R.id.play_mode) {
            TrackerFragment ftable = new TrackerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.FrameTop, ftable, "F_TRACKER")
                    .addToBackStack("F_TRACKER")
                    .commit();
            return true;
        } else if (id == R.id.player_settings) {

            Bundle bundle = new Bundle();
            bundle.putInt("campaignId", SharedPrefsHelper.loadCampaignId(this));
            bundle.putString("campaignName", SharedPrefsHelper.loadCampaignName(this));
            loadPCs(bundle);
        } else if (id == R.id.menu_paste) {
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
                    case DbContentProvider.NPC:
                        pasteNpc(pasteUri);
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

    private void pasteNpc(Uri pasteUri) {
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
                        startInsert(1, null, DbContentProvider.CONTENT_URI_NPC, values);
                    }
                }
            }
        };

        String[] selectionArgs = new String[]{String.valueOf(encounterId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";

        queryHandler.startQuery(
                1, null,
                pasteUri,
                DataBaseHandler.PROJECTION_NPC,
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
                        startInsert(2, null, DbContentProvider.CONTENT_URI_NPC, values);
                    }
                }
            }
        };

        String[] selectionArgs = new String[]{String.valueOf(oldId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";

        queryHandler.startQuery(
                2, null,
                DbContentProvider.CONTENT_URI_NPC,
                DataBaseHandler.PROJECTION_NPC,
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


    //TODO add asynctask for pcs
    private void pastePc(Uri pasteUri) {
        ContentResolver cr = getContentResolver();
        int campaignId = ((PcTableFragment) getSupportFragmentManager().findFragmentByTag("FT_PC")).getCampaignId();

        Cursor pasteCursor = cr.query(pasteUri, DataBaseHandler.PROJECTION_PC, null, null, null);
        if (pasteCursor != null) {
            pasteCursor.moveToFirst();
            ContentValues values = new ContentValues();
            values.put(DataBaseHandler.KEY_NAME, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            values.put(DataBaseHandler.KEY_INFO, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
            values.put(DataBaseHandler.KEY_INITIATIVEBONUS, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS)));
            values.put(DataBaseHandler.KEY_MAXHP, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP)));
            values.put(DataBaseHandler.KEY_AC, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)));
            values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);
            cr.insert(DbContentProvider.CONTENT_URI_PC, values);
            pasteCursor.close();
        }
    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    private void loadPCs(Bundle bundle) {
        Fragment fragment;
        fragment = new PcTableFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FT_PC")
                .addToBackStack("FT_PC")
                .commit();
    }
}
