package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pentapus.pentapusdmh.Fragments.TrackerFragment;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.R;

/**
 * Created by Koni on 02.04.2016.
 */
public class SavingThrowFragment extends Fragment {
    private static final String ARG_PAGE = "ARG_PAGE";

    private StatusAdapter statusAdapter;
    private GridLayoutManager gridLayoutManager;
    private int id;
    private int[] abilities;

    public static SavingThrowFragment newInstance(int page) {
        SavingThrowFragment fragment = new SavingThrowFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        abilities = ((TrackerFragment)getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().getAbilities(id);

    }



    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.viewpager_savingthrow, container, false);

        final TextView strengthMod = (TextView) view.findViewById(R.id.tvStrengthMod);
        final TextView dexMod = (TextView) view.findViewById(R.id.tvDexMod);
        final TextView constMod = (TextView) view.findViewById(R.id.tvConstMod);
        final TextView intMod = (TextView) view.findViewById(R.id.tvIntMod);
        final TextView wisMod = (TextView) view.findViewById(R.id.tvWisdomMod);
        final TextView charMod = (TextView) view.findViewById(R.id.tvCharismaMod);
        final TextView tvStrengthRes = (TextView) view.findViewById(R.id.tvStrengthRes);
        final TextView tvDexRes = (TextView) view.findViewById(R.id.tvDexRes);
        final TextView tvConstRes = (TextView) view.findViewById(R.id.tvConstRes);
        final TextView tvIntRes = (TextView) view.findViewById(R.id.tvIntRes);
        final TextView tvWisRes = (TextView) view.findViewById(R.id.tvWisdomRes);
        final TextView tvCharRes = (TextView) view.findViewById(R.id.tvCharismaRes);



        Button bRollStrength = (Button) view.findViewById(R.id.bRollStrength);
        bRollStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll= DiceHelper.d20();
                strengthMod.setText("Modifier: " + abilities[0] + "   Roll: "+roll);
                tvStrengthRes.setText(String.valueOf(roll+abilities[0]));
            }
        });
        Button bRollDex = (Button) view.findViewById(R.id.bRollDex);
        bRollDex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                dexMod.setText("Modifier: " + abilities[1] + "   Roll: "+roll);
                tvDexRes.setText(String.valueOf(roll+abilities[1]));
            }
        });
        Button bRollConst = (Button) view.findViewById(R.id.bRollConst);
        bRollConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                constMod.setText("Modifier: " + abilities[2] + "   Roll: "+roll);
                tvConstRes.setText(String.valueOf(roll+abilities[2]));
            }
        });
        Button bRollWisdom = (Button) view.findViewById(R.id.bRollInt);
        bRollWisdom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                intMod.setText("Modifier: " + abilities[3] + "   Roll: "+roll);
                tvIntRes.setText(String.valueOf(roll+abilities[3]));
            }
        });
        Button bRollInt = (Button) view.findViewById(R.id.bRollWisdom);
        bRollInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                wisMod.setText("Modifier: " + abilities[4] + "   Roll: "+roll);
                tvWisRes.setText(String.valueOf(roll+abilities[4]));
            }
        });
        Button bRollCharisma = (Button) view.findViewById(R.id.bRollCharisma);
        bRollCharisma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                charMod.setText("Modifier: " + abilities[5] + "   Roll: "+roll);
                tvCharRes.setText(String.valueOf(roll+abilities[5]));
            }
        });





        // Inflate the layout to use as dialog or embedded fragment
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
    }


}