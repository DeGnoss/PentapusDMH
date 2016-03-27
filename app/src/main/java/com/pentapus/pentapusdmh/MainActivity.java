package com.pentapus.pentapusdmh;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.pentapus.pentapusdmh.Fragments.EncounterEditFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterFragment;
import com.pentapus.pentapusdmh.Fragments.EncounterTableFragment;
import com.pentapus.pentapusdmh.Fragments.NPCEditFragment;
import com.pentapus.pentapusdmh.Fragments.SessionEditFragment;
import com.pentapus.pentapusdmh.Fragments.SessionTableFragment;

public class MainActivity extends AppCompatActivity implements SessionTableFragment.OnFragmentInteractionListener, SessionEditFragment.OnFragmentInteractionListener, EncounterTableFragment.OnFragmentInteractionListener, EncounterEditFragment.OnFragmentInteractionListener, EncounterFragment.OnFragmentInteractionListener, NPCEditFragment.OnFragmentInteractionListener {


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


        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      /*  fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.FrameTop);
                String tag = currentFragment.getTag();
                Bundle bundle = new Bundle();
                switch (tag) {
                    case "FT_SESSION":
                        bundle.putString("mode", "add");
                        addSession(bundle);
                        break;
                    case "FT_ENCOUNTER":
                        bundle.putString("mode", "add");
                        addEncounter(bundle);
                        break;
                    default:
                        Log.d("TAG", "Unhandled FAB fragment tag " + tag);
                        break;
                }
            }

        });*/
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
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void addSession(Bundle bundle) {
        Fragment fragment;
        fragment = new SessionEditFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_SESSION")
                .addToBackStack("FE_SESSION")
                .commit();
    }

    @Override
    public void addEncounter(Bundle bundle) {
        Fragment fragment;
        fragment = new EncounterEditFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_ENCOUNTER")
                .addToBackStack("FE_ENCOUNTER")
                .commit();
    }

    @Override
    public void addNPC(Bundle bundle) {
        Fragment fragment;
        fragment = new NPCEditFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_NPC")
                .addToBackStack("FE_NPC")
                .commit();
    }


    @Override
    public void sessionDone() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void encounterDone() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void npcDone() {
        getSupportFragmentManager().popBackStack();
    }
}
