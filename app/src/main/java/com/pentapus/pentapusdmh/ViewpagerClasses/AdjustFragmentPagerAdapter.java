package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Koni on 4/4/16.
 */
public class AdjustFragmentPagerAdapter extends FragmentPagerAdapter {
final int PAGE_COUNT = 3;
private String tabTitles[] = new String[] { "Saving Throws", "OverView", "Conditions" };
private Context context;
    private SavingThrowFragment savingThrowFragment;
    private StatusFragment statusFragment;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private int id;


    public AdjustFragmentPagerAdapter(FragmentManager fm, Context context, int id) {
        super(fm);
        this.context = context;
        this.id=id;
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
                return HpOverviewFragment.newInstance(id);
            case 2:
                return StatusFragment.newInstance(id);
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
