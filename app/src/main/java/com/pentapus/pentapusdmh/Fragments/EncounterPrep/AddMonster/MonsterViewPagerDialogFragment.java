package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageFragmentPagerAdapter;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageGridAdapter;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.MonsterEditFragment;
import com.pentapus.pentapusdmh.R;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Koni on 4/4/16.
 */
public class MonsterViewPagerDialogFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static final String ARG_PAGE = "ARG_PAGE";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MonsterViewPagerAdapter pagerAdapter;
    private FloatingActionButton fabImageVP;
    private int id;
    private static final String MODE = "modeUpdate";
    private Button bDone;


    public MonsterViewPagerDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static MonsterViewPagerDialogFragment newInstance() {
        MonsterViewPagerDialogFragment fragment = new MonsterViewPagerDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.monster_viewpager_tab_layout, parent, false);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        fabImageVP = (FloatingActionButton) view.findViewById(R.id.fabImageVP);

        bDone = (Button) view.findViewById(R.id.bDone);
        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        fabImageVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(MODE, false);
                addMonster(bundle);
            }
        });




        return view;
    }

    private void addMonster(Bundle bundle) {
        Fragment fragment;
        fragment = new MonsterEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment, "FE_MONSTER")
                .addToBackStack("FT_MONSTER")
                .commit();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pagerAdapter = new MonsterViewPagerAdapter(getChildFragmentManager(), getContext(), id);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0 :
                fabImageVP.setVisibility(View.VISIBLE);
                break;
            case 1:
                fabImageVP.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }



    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
