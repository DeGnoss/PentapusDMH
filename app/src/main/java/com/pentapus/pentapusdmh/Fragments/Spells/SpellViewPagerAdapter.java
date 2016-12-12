package com.pentapus.pentapusdmh.Fragments.Spells;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by Koni on 4/4/16.
 */
public class SpellViewPagerAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"My Spells", "All Spells"};
    private Context context;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private int id;
    private File[] imageUri;
    private int mode;
    private ArrayList<String> spellsKnown = new ArrayList<String>();
    Bundle bundle = new Bundle();
    private String level, scclass;


    public SpellViewPagerAdapter(FragmentManager fm, Context context, int id, int mode, Bundle args, String level, String scclass) {
        super(fm);
        this.context = context;
        this.id = id;
        this.mode = mode;
        this.bundle = args;
        this.level = level;
        this.scclass = scclass;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MySpellTableFragment f1 = MySpellTableFragment.newInstance("CUSTOM", mode);
                return f1;
            case 1:
                PHBSpellTableFragment f2 = PHBSpellTableFragment.newInstance("", mode, bundle, level, scclass);
                return f2;
            default:
                return null;
        }
    }


    public void setImageUri(File[] imageUri) {
        this.imageUri = imageUri;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(object);
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
        if(object instanceof MySpellTableFragment){
            MySpellTableFragment f2 = (MySpellTableFragment) object;
        }else if(object instanceof PHBSpellTableFragment){
            PHBSpellTableFragment f1 = (PHBSpellTableFragment) object;
        }
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
