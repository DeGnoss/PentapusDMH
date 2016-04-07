package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.pentapus.pentapusdmh.Fragments.TrackerFragment;
import com.pentapus.pentapusdmh.R;

import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Koni on 02.04.2016.
 */
public class HpOverviewFragment extends Fragment implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2{

    private int id;
    private Button bDamage;
    private TextView tvDamage, tvIdentifier;
    private int hpDiff;
    private int hpCurrent;
    private int hpMax;


    public static HpOverviewFragment newInstance(int id) {
        HpOverviewFragment fragment = new HpOverviewFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
        hpCurrent = ((TrackerFragment)getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().getHp(id);
    }


    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_hpoverview, container, false);

        tvDamage = (TextView) view.findViewById(R.id.tvNumber);
        tvIdentifier = (TextView) view.findViewById(R.id.tvIdent);
        bDamage = (Button) view.findViewById(R.id.buttonDamage);

        bDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setLabelText("Damage/Healing")
                        .setDecimalVisibility(0)
                        .setTargetFragment(HpOverviewFragment.this);
                npb.show();
            }
        });


        return view;
    }

    public void saveChanges(){
        ((TrackerFragment)getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().setHp(id, hpDiff);
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        hpDiff = number.intValue();
        tvDamage.setText(String.valueOf(Math.abs(number.intValue())));
        if(isNegative){
            tvIdentifier.setText("Healing");
        }else{
            tvIdentifier.setText("Damage");
        }
    }
}