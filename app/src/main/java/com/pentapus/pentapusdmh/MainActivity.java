package com.pentapus.pentapusdmh;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        }else if(id == R.id.campaign_settings){
            CampaignTableFragment myFragment = (CampaignTableFragment)getSupportFragmentManager().findFragmentByTag("FT_CAMPAIGN");
            if(myFragment != null){
                if(!myFragment.isVisible()){
                    CampaignTableFragment ftable = new CampaignTableFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FrameTop, ftable, "FT_CAMPAIGN")
                            .addToBackStack("FT_CAMPAIGN")
                            .commit();
                }
                return true;
            } else{
                    CampaignTableFragment ftable = new CampaignTableFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FrameTop, ftable, "FT_CAMPAIGN")
                            .addToBackStack("FT_CAMPAIGN")
                            .commit();
                return true;
            }

        }else if(id == R.id.play_mode){
            TrackerFragment ftable = new TrackerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.FrameTop, ftable, "F_TRACKER")
                    .addToBackStack("F_TRACKER")
                    .commit();
            return true;
        }else if(id == R.id.player_settings) {

            Bundle bundle = new Bundle();
            bundle.putInt("campaignId", SharedPrefsHelper.loadCampaignId(this));
            bundle.putString("campaignName", SharedPrefsHelper.loadCampaignName(this));
            loadPCs(bundle);
        }else if(id == R.id.menu_paste){
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            pasteEntry(pasteUri);

        }

        return super.onOptionsItemSelected(item);
    }

    private void pasteEntry(Uri pasteUri) {
        // If the clipboard contains a URI reference
        if (pasteUri != null) {
            Log.d("Paste", pasteUri.toString());
            // Is this a content URI?
            ContentResolver cr = getContentResolver();
            String uriMimeType = cr.getType(pasteUri);
            if(uriMimeType != null){
                String[] projection;
                Cursor pasteCursor;
                int campaignId;
                Log.d("MainActivity", uriMimeType);
                switch(uriMimeType){
                    case "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.npc":
                        int encounterId = ((EncounterFragment) getSupportFragmentManager().findFragmentByTag("F_ENCOUNTER")).getEncounterId();
                        projection = new String[]{
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
                                DataBaseHandler.KEY_CHARISMA
                        };
                        pasteCursor = cr.query(pasteUri, projection, null, null, null);
                        if(pasteCursor != null){
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
                            values.put(DataBaseHandler.KEY_BELONGSTO, encounterId);
                            cr.insert(DbContentProvider.CONTENT_URI_NPC, values);
                            pasteCursor.close();
                        }
                        break;
                    case "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.encounter":
                        int sessionId = ((EncounterTableFragment) getSupportFragmentManager().findFragmentByTag("FT_ENCOUNTER")).getSessionId();
                        projection = new String[]{
                                DataBaseHandler.KEY_ROWID,
                                DataBaseHandler.KEY_NAME,
                                DataBaseHandler.KEY_INFO};
                        pasteCursor = cr.query(pasteUri, projection, null, null, null);
                        if(pasteCursor != null){
                            pasteCursor.moveToFirst();
                            ContentValues values = new ContentValues();
                            values.put(DataBaseHandler.KEY_NAME, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
                            values.put(DataBaseHandler.KEY_INFO, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
                            values.put(DataBaseHandler.KEY_BELONGSTO, sessionId);
                            cr.insert(DbContentProvider.CONTENT_URI_ENCOUNTER, values);
                            pasteCursor.close();
                        }
                        break;
                    case "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.session":
                        campaignId = ((SessionTableFragment) getSupportFragmentManager().findFragmentByTag("FT_SESSION")).getCampaignId();
                        projection = new String[]{
                                DataBaseHandler.KEY_ROWID,
                                DataBaseHandler.KEY_NAME,
                                DataBaseHandler.KEY_INFO};
                        pasteCursor = cr.query(pasteUri, projection, null, null, null);
                        if(pasteCursor != null){
                            pasteCursor.moveToFirst();
                            ContentValues values = new ContentValues();
                            values.put(DataBaseHandler.KEY_NAME, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
                            values.put(DataBaseHandler.KEY_INFO, pasteCursor.getString(pasteCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
                            values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);
                            cr.insert(DbContentProvider.CONTENT_URI_SESSION, values);
                            pasteCursor.close();
                        }
                        break;
                    case "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.pc":
                        campaignId = ((PcTableFragment) getSupportFragmentManager().findFragmentByTag("FT_PC")).getCampaignId();
                        projection = new String[]{
                                DataBaseHandler.KEY_ROWID,
                                DataBaseHandler.KEY_NAME,
                                DataBaseHandler.KEY_INFO,
                                DataBaseHandler.KEY_INITIATIVEBONUS,
                                DataBaseHandler.KEY_MAXHP,
                                DataBaseHandler.KEY_AC
                        };
                        pasteCursor = cr.query(pasteUri, projection, null, null, null);
                        if(pasteCursor != null){
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
                        break;
                    default:
                        break;
                }

            }
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
