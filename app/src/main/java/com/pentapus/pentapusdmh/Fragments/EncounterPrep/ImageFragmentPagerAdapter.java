package com.pentapus.pentapusdmh.Fragments.EncounterPrep;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by Koni on 4/4/16.
 */
public class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
final int PAGE_COUNT = 2;
private String tabTitles[] = new String[] { "My Icons", "Icons" };
private Context context;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private int id;


    public ImageFragmentPagerAdapter(FragmentManager fm, Context context, int id) {
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
                return ViewPagerMyImageGridFragment.newInstance(id);
            case 1:
                return ViewPagerImageGridFragment.newInstance(id);
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
