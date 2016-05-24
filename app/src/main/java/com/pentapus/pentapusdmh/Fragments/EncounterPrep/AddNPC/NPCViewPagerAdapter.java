package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageGridFragment;

import java.io.File;

/**
 * Created by Koni on 4/4/16.
 */
public class NPCViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"Campaign", "All"};
    private Context context;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private boolean isNavMode;
    private int id;
    private File[] imageUri;


    public NPCViewPagerAdapter(FragmentManager fm, Context context, boolean isNavMode, int id) {
        super(fm);
        this.context = context;
        this.id = id;
        this.isNavMode = isNavMode;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MyNPCTableFragment.newInstance(isNavMode);
            case 1:
                return NPCTableFragment.newInstance(isNavMode);
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
        if (object instanceof ImageGridFragment) {
            ((ImageGridFragment) object).update(imageUri);
        }
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
