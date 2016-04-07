package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pentapus.pentapusdmh.Fragments.TrackerFragment;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.TrackerAdapter;
import com.pentapus.pentapusdmh.TrackerInfoCard;

import java.util.ArrayList;

/**
 * Created by Koni on 4/4/16.
 */
public class ViewPagerDialogFragment extends Fragment {

    private static final String ARG_PAGE = "ARG_PAGE";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdjustFragmentPagerAdapter pagerAdapter;
    private Button bDone;
    private int id;



    public ViewPagerDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static ViewPagerDialogFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        ViewPagerDialogFragment fragment = new ViewPagerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                id = getArguments().getInt("id");
            }
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_tab_layout, parent, false);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);

        bDone = (Button) view.findViewById(R.id.bDone);
        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                getFragmentManager().popBackStack();
                ((TrackerFragment) getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().notifyDataSetChanged();
            }
        });


        return view;
    }

    private void saveData() {
        ((HpOverviewFragment)pagerAdapter.getRegisteredFragment(1)).saveChanges();
        ((StatusFragment)pagerAdapter.getRegisteredFragment(2)).saveChanges();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pagerAdapter = new AdjustFragmentPagerAdapter(getChildFragmentManager(), getContext(), id);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }
}
