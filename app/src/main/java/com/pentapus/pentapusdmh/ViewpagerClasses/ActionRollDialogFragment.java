package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.R;

/**
 * Created by Koni on 10.11.2016.
 */

public class ActionRollDialogFragment extends DialogFragment {

    private int mod, add;
    private String name, description, roll1, roll2, type1, type2;
    private int lrStr= 1000, lrDex=1000, lrCon=1000, lrInt=1000, lrWis=1000, lrCha=1000;
    private TextView tvActionTitle, tvActionRoll, tvActionDamage, tvActionAdd;
    private ColorStateList mTextColor;
    private int mDialogBackgroundResId;
    private int mTheme = -1;

    static ActionRollDialogFragment newInstance(String name, String description, int add, int mod, String roll1, String type1, String roll2, String type2) {
        ActionRollDialogFragment f = new ActionRollDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("description", description);
        args.putInt("mod", mod);
        args.putInt("add", add);
        args.putString("roll1", roll1);
        args.putString("roll2", roll2);
        args.putString("type1", type1);
        args.putString("type2", type2);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTheme = R.style.BetterPickersDialogFragment;
        name = getArguments().getString("name");
        description = getArguments().getString("description");
        mod = getArguments().getInt("mod");
        add = getArguments().getInt("add");
        roll1 = getArguments().getString("roll1");
        roll2 = getArguments().getString("roll2");
        type1 = getArguments().getString("type1");
        type2 = getArguments().getString("type2");

        mDialogBackgroundResId = R.drawable.dialog_full_holo_dark;

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        if (mTheme != -1) {
            TypedArray a = getActivity().getApplicationContext().obtainStyledAttributes(mTheme, R.styleable.BetterPickersDialogFragment);

            mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
            mDialogBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpDialogBackground, mDialogBackgroundResId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actionfragment_rolldialog, null);

        tvActionTitle  = (TextView) view.findViewById(R.id.tvActionTitle);
        tvActionRoll  = (TextView) view.findViewById(R.id.tvActionRoll);
        tvActionDamage  = (TextView) view.findViewById(R.id.tvActionDamage);
        tvActionAdd  = (TextView) view.findViewById(R.id.tvActionAdd);

        tvActionTitle.setText(name);
        tvActionRoll.setText(roll1);
        tvActionDamage.setText(type1);
        if(add == 1){
            tvActionAdd.setText(description);
        }else{
            tvActionAdd.setVisibility(View.GONE);
        }



        getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);

        return view;
    }

    /*
    private void rollStStr(){
        int mod = AbilityModifierCalculator.calculateMod(str);
        int rolled = DiceHelper.d20();
        if(lrStr != 1000){
            tvLrStr.setText(Html.fromHtml("Last Roll: " + lrStr));
        }else{
            tvLrStr.setText(Html.fromHtml("Last Roll: -"));
        }
        if(stStr != 0 && stStr > mod){
            tvStrResult.setText(String.valueOf(rolled + stStr));
            if(stStr >= 0){
                tvStrDetail.setText(Html.fromHtml(rolled + " + " + stStr + " = "));
            }else{
                tvStrDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(stStr) + " = "));
            }
            lrStr = rolled + stStr;
        }else{
            tvStrResult.setText(String.valueOf(rolled + mod));
            if(mod >= 0){
                tvStrDetail.setText(Html.fromHtml(rolled + " + " + mod + " = "));
            }else{
                tvStrDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(mod) + " = "));
            }
            lrStr = rolled+mod;
        }
    }
    */


}
