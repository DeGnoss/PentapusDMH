package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.TrackerAdapter;
import com.pentapus.pentapusdmh.TrackerInfoCard;

/**
 * Created by Koni on 4/4/16.
 */
public class ViewPagerDialogFragment extends Fragment {

    private static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdjustFragmentPagerAdapter pagerAdapter;
    private Button bDone;
    private TrackerInfoCard mTempTrackerInfoCard;



    public ViewPagerDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static ViewPagerDialogFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ViewPagerDialogFragment fragment = new ViewPagerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
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
            }
        });


        return view;
    }

    private void saveData() {
        ((StatusFragment)pagerAdapter.getItem(2)).saveChanges();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pagerAdapter = new AdjustFragmentPagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(pagerAdapter);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // ..... More code here
    }

    @Override
    public void onStart() {
        super.onStart();
       // getDialog().getWindow().setLayout(
              //  RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }


}
