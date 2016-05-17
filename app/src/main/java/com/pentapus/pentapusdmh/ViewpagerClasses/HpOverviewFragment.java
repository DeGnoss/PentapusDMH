package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerFragment;
import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerInfoCard;
import com.pentapus.pentapusdmh.NumberPicker.NumberPickerBuilder;
import com.pentapus.pentapusdmh.NumberPicker.NumberPickerDialogFragment;
import com.pentapus.pentapusdmh.R;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Koni on 02.04.2016.
 */
public class HpOverviewFragment extends Fragment implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2{

    private int id;
    private View vFrame;
    private ImageButton bDamage;
    private ImageView ivIcon;
    private TextView tvDamage, tvIdentifier, tvName, tvHpCurrent, tvHpMax, tvAc;
    private int hpDiff;
    private int hpCurrent;
    private int hpMax;
    private int ac;
    private String name;
    private Uri iconUri;
    private int type;
    private TrackerInfoCard selectedCharacter;


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
        selectedCharacter = ((TrackerFragment)getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().getItem(id);

        hpCurrent = selectedCharacter.getHp();
        hpMax = selectedCharacter.getMaxHp();
        ac = selectedCharacter.getAc();
        name = selectedCharacter.getName();
        iconUri = selectedCharacter.getIconUri();
        type = selectedCharacter.getType();

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
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvHpCurrent = (TextView) view.findViewById(R.id.tvHpCurrent);
        tvHpMax = (TextView) view.findViewById(R.id.tvHpMax);
        tvAc = (TextView) view.findViewById(R.id.tvAc);
        ivIcon = (ImageView) view.findViewById(R.id.ivAvatar);
        vFrame = (View) view.findViewById(R.id.vFrame);

        tvHpCurrent.setText("HP " + String.valueOf(hpCurrent));
        tvHpMax.setText(" / " + String.valueOf(hpMax));
        tvAc.setText("AC " + String.valueOf(ac));
        tvName.setText(name);
        ivIcon.setImageURI(iconUri);

        if(type == 1){
            vFrame.setBackgroundColor(Color.parseColor("#4caf50"));
        }else if(type == 0){
            vFrame.setBackgroundColor(Color.parseColor("#F44336"));
        }else if(type == 2){
            vFrame.setBackgroundColor(Color.parseColor("#3F51B5"));
        }else{
            vFrame.setBackgroundColor(Color.parseColor("#ffffff"));
        }



        bDamage = (ImageButton) view.findViewById(R.id.buttonDamage);
        bDamage.setColorFilter(Color.parseColor("#ffffffff"));

        bDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setLabelText("Damage")
                        .setDecimalVisibility(View.INVISIBLE)
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
    public void onDialogNumberSet(int reference, BigInteger number, boolean temporary, boolean isNegative, BigDecimal fullNumber) {
        hpDiff = number.intValue();
        tvDamage.setText(String.valueOf(Math.abs(number.intValue())));
        if(isNegative){
            if(temporary){
                tvIdentifier.setText("Temp. Hp");
            }else{
                tvIdentifier.setText("Heal");
            }
        }else{
            tvIdentifier.setText("Damage");
        }
    }
}