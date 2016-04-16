package com.pentapus.pentapusdmh;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.CampaignTableFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterTableFragment;
import com.pentapus.pentapusdmh.Fragments.PcTableFragment;
import com.pentapus.pentapusdmh.Fragments.SessionTableFragment;
import com.pentapus.pentapusdmh.Fragments.TrackerFragment;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
        ContentResolver cr = getContentResolver();
        int encounterId = ((EncounterFragment) getSupportFragmentManager().findFragmentByTag("F_ENCOUNTER")).getEncounterId();
        String[] projection = new String[]{
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO,
                DataBaseHandler.KEY_INITIATIVEBONUS,
                DataBaseHandler.KEY_MAXHP,
                DataBaseHandler.KEY_AC,
                DataBaseHandler.KEY_STRENGTH,
                DataBaseHandler.KEY_DEXTERITY,
                DataBaseHandler.KEY_CONSTITUTION,
                DataBaseHandler.KEY_INTELLIGENCE,
                DataBaseHandler.KEY_WISDOM,
                DataBaseHandler.KEY_CHARISMA,
                DataBaseHandler.KEY_ICON,
                DataBaseHandler.KEY_TYPE
        };
        Cursor pasteCursor = cr.query(pasteUri, projection, null, null, null);
        if (pasteCursor != null) {
            pasteCursor.moveToFirst();
            ContentValues values = new ContentValues();
            values.put(DataBaseHandler.KEY_NAME, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            values.put(DataBaseHandler.KEY_INFO, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
            values.put(DataBaseHandler.KEY_INITIATIVEBONUS, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS)));
            values.put(DataBaseHandler.KEY_MAXHP, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP)));
            values.put(DataBaseHandler.KEY_AC, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)));
            values.put(DataBaseHandler.KEY_STRENGTH, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH)));
            values.put(DataBaseHandler.KEY_DEXTERITY, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY)));
            values.put(DataBaseHandler.KEY_CONSTITUTION, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION)));
            values.put(DataBaseHandler.KEY_INTELLIGENCE, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE)));
            values.put(DataBaseHandler.KEY_WISDOM, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM)));
            values.put(DataBaseHandler.KEY_CHARISMA, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA)));
            values.put(DataBaseHandler.KEY_ICON, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
            values.put(DataBaseHandler.KEY_TYPE, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)));
            values.put(DataBaseHandler.KEY_BELONGSTO, encounterId);
            cr.insert(DbContentProvider.CONTENT_URI_NPC, values);
            pasteCursor.close();
        }
    }

    private void pasteEncounter(Uri pasteUri) {
        ContentResolver cr = getContentResolver();
        int sessionId = ((EncounterTableFragment) getSupportFragmentManager().findFragmentByTag("FT_ENCOUNTER")).getSessionId();
        String[] projection = new String[]{
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO};
        Cursor pasteCursor = cr.query(pasteUri, projection, null, null, null);
        if (pasteCursor != null) {
            pasteCursor.moveToFirst();
            ContentValues values = new ContentValues();
            values.put(DataBaseHandler.KEY_NAME, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            values.put(DataBaseHandler.KEY_INFO, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
            values.put(DataBaseHandler.KEY_BELONGSTO, sessionId);
            Uri newUri = cr.insert(DbContentProvider.CONTENT_URI_ENCOUNTER, values);
            int newId = (int) ContentUris.parseId(newUri);
            int oldId = pasteCursor.getInt(pasteCursor.getColumnIndex(DataBaseHandler.KEY_ROWID));
            pasteCursor.close();
            nestedPasteNPC(oldId, newId);
        }
    }

    private void nestedPasteNPC(int oldId, int newId) {
        Uri pasteUri = DbContentProvider.CONTENT_URI_NPC;
        ContentResolver cr = getContentResolver();
        String[] selectionArgs = new String[]{String.valueOf(oldId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        Cursor pasteCursor = cr.query(pasteUri, null, selection, selectionArgs, null);
        if (pasteCursor != null) {
            while (pasteCursor.moveToNext()) {
                Log.d("Test", pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
                ContentValues values = new ContentValues();
                values.put(DataBaseHandler.KEY_NAME, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
                values.put(DataBaseHandler.KEY_INFO, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
                values.put(DataBaseHandler.KEY_INITIATIVEBONUS, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS)));
                values.put(DataBaseHandler.KEY_MAXHP, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP)));
                values.put(DataBaseHandler.KEY_AC, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)));
                values.put(DataBaseHandler.KEY_STRENGTH, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH)));
                values.put(DataBaseHandler.KEY_DEXTERITY, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY)));
                values.put(DataBaseHandler.KEY_CONSTITUTION, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION)));
                values.put(DataBaseHandler.KEY_INTELLIGENCE, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE)));
                values.put(DataBaseHandler.KEY_WISDOM, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM)));
                values.put(DataBaseHandler.KEY_CHARISMA, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA)));
                values.put(DataBaseHandler.KEY_ICON, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
                values.put(DataBaseHandler.KEY_TYPE, pasteCursor.getInt(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)));
                values.put(DataBaseHandler.KEY_BELONGSTO, newId);
                cr.insert(DbContentProvider.CONTENT_URI_NPC, values);
            }
            pasteCursor.close();
        }
    }

    private void pasteSession(Uri pasteUri) {
        ContentResolver cr = getContentResolver();
        int campaignId = ((SessionTableFragment) getSupportFragmentManager().findFragmentByTag("FT_SESSION")).getCampaignId();
        String[] projection = new String[]{
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO};
        Cursor pasteCursor = cr.query(pasteUri, projection, null, null, null);
        if (pasteCursor != null) {
            pasteCursor.moveToFirst();
            ContentValues values = new ContentValues();
            values.put(DataBaseHandler.KEY_NAME, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            values.put(DataBaseHandler.KEY_INFO, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
            values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);
            Uri newUri = cr.insert(DbContentProvider.CONTENT_URI_SESSION, values);
            int newId = (int) ContentUris.parseId(newUri);
            int oldId = pasteCursor.getInt(pasteCursor.getColumnIndex(DataBaseHandler.KEY_ROWID));
            pasteCursor.close();
            nestedPasteEncounter(oldId, newId);
        }
    }

    private void nestedPasteEncounter(int oldId, int newId) {
        Uri pasteUri = DbContentProvider.CONTENT_URI_ENCOUNTER;
        ContentResolver cr = getContentResolver();
        String[] selectionArgs = new String[]{String.valueOf(oldId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        Cursor pasteCursor = cr.query(pasteUri, null, selection, selectionArgs, null);
        if (pasteCursor != null) {
            while (pasteCursor.moveToNext()) {
                ContentValues values = new ContentValues();
                values.put(DataBaseHandler.KEY_NAME, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
                values.put(DataBaseHandler.KEY_INFO, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
                values.put(DataBaseHandler.KEY_BELONGSTO, newId);
                Uri newUri = cr.insert(DbContentProvider.CONTENT_URI_ENCOUNTER, values);
                int newIdEnc = (int) ContentUris.parseId(newUri);
                int oldIdEnc = pasteCursor.getInt(pasteCursor.getColumnIndex(DataBaseHandler.KEY_ROWID));
                nestedPasteNPC(oldIdEnc, newIdEnc);
            }
            pasteCursor.close();
        }
    }

    private void pastePc(Uri pasteUri) {
        ContentResolver cr = getContentResolver();
        int campaignId = ((PcTableFragment) getSupportFragmentManager().findFragmentByTag("FT_PC")).getCampaignId();
        String[] projection = new String[]{
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO,
                DataBaseHandler.KEY_INITIATIVEBONUS,
                DataBaseHandler.KEY_MAXHP,
                DataBaseHandler.KEY_AC
        };
        Cursor pasteCursor = cr.query(pasteUri, projection, null, null, null);
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
       /* EncounterFragment encounterFragment = (EncounterFragment) fm.findFragmentByTag("F_ENCOUNTER");
        EncounterTableFragment encounterTableFragment = (EncounterTableFragment) fm.findFragmentByTag("FT_ENCOUNTER");
        SessionTableFragment sessionTableFragment = (SessionTableFragment) fm.findFragmentByTag("FT_SESSION");
        CampaignTableFragment campaignTableFragment = (CampaignTableFragment) fm.findFragmentByTag("FT_CAMPAIGN");
        if (campaignTableFragment != null && campaignTableFragment.isVisible() && CampaignAdapter.getSelectedPos() != -1) {
            //int oldPos = CampaignAdapter.getSelectedPos();
            //CampaignAdapter.setSelectedPos(-1);
            //campaignTableFragment.getmCampaignAdapter().notifyItemChanged(oldPos);
        } else if (sessionTableFragment != null && sessionTableFragment.isVisible() && SessionAdapter.getSelectedPos() != -1) {
            int oldPos = SessionAdapter.getSelectedPos();
            //SessionAdapter.setSelectedPos(-1);
            //sessionTableFragment.getmSessionAdapter().notifyItemChanged(oldPos);
        } else if (encounterFragment != null && encounterFragment.isVisible() && CursorRecyclerViewAdapter.selectedPos != -1) {
            CursorRecyclerViewAdapter.selectedPos = -1;
            encounterFragment.getMergeAdapter().notifyDataSetChanged();
        } else if (encounterTableFragment != null && encounterTableFragment.isVisible() && CursorRecyclerNavigationViewAdapter.selectedPos != -1) {
            CursorRecyclerNavigationViewAdapter.selectedPos = -1;
            encounterTableFragment.getDataAdapterEncounters().notifyDataSetChanged();
        } else {*/
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
