package com.pentapus.pentapusdmh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pentapus.pentapusdmh.Fragments.CampaignTableFragment;
import com.pentapus.pentapusdmh.Fragments.SessionTableFragment;
import com.pentapus.pentapusdmh.Fragments.TrackerFragment;

public class MainActivity extends AppCompatActivity implements TrackerActivityListener{

    private TrackerActivityListener mListener;

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
            CampaignTableFragment ftable = new CampaignTableFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.FrameTop, ftable, "FT_CAMPAIGN")
                    .addToBackStack("FT_CAMPAIGN")
                    .commit();
            return true;
        }else if(id == R.id.play_mode){
            TrackerFragment ftable = new TrackerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.FrameTop, ftable, "F_TRACKER")
                    .addToBackStack("F_TRACKER")
                    .commit();
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
    public Fragment getTrackerFragment() {
        Fragment test = ((TrackerFragment)getSupportFragmentManager().findFragmentByTag("F_TRACKER"));
        return ((TrackerFragment)getSupportFragmentManager().findFragmentByTag("F_TRACKER"));
    }
}
