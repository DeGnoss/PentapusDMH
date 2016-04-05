package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Koni on 4/4/16.
 */
public class AdjustFragmentPagerAdapter extends FragmentPagerAdapter {
final int PAGE_COUNT = 3;
private String tabTitles[] = new String[] { "Saving Throws", "OverView", "Conditions" };
private Context context;

    public AdjustFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return SavingThrowFragment.newInstance(position);
            case 1:
                return HpOverviewFragment.newInstance(position);
            case 2:
                return StatusFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
