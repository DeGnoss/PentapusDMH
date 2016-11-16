package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.NumberPicker.NumberPicker;
import com.pentapus.pentapusdmh.R;

/**
 * Created by Koni on 10.11.2016.
 */

public class SavingThrowDialogFragment extends DialogFragment {

    private int str, dex, con, intel, wis, cha, stStr, stDex, stCon, stInt, stWis, stCha;
    private int lrStr= 1000, lrDex=1000, lrCon=1000, lrInt=1000, lrWis=1000, lrCha=1000;
    private CardView cardStrength, cardDex, cardCon, cardInt, cardWis, cardCha;
    private TextView tvStrResult, tvDexResult, tvConResult, tvIntResult, tvWisResult, tvChaResult, tvStrDetail, tvDexDetail, tvConDetail, tvIntDetail, tvWisDetail, tvChaDetail, tvLrStr, tvLrDex, tvLrCon, tvLrInt, tvLrWis, tvLrCha;
    private ColorStateList mTextColor;
    private int mDialogBackgroundResId;
    private int mTheme = -1;

    static SavingThrowDialogFragment newInstance(int str, int dex, int con, int intel, int wis, int cha, int stStr, int stDex, int stCon, int stInt, int stWis, int stCha) {
        SavingThrowDialogFragment f = new SavingThrowDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("str", str);
        args.putInt("dex", dex);
        args.putInt("con", con);
        args.putInt("intel", intel);
        args.putInt("wis", wis);
        args.putInt("cha", cha);
        args.putInt("stStr", stStr);
        args.putInt("stDex", stDex);
        args.putInt("stCon", stCon);
        args.putInt("stInt", stInt);
        args.putInt("stWis", stWis);
        args.putInt("stCha", stCha);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTheme = R.style.BetterPickersDialogFragment;
        str = getArguments().getInt("str");
        dex = getArguments().getInt("dex");
        con = getArguments().getInt("con");
        intel = getArguments().getInt("intel");
        wis = getArguments().getInt("wis");
        cha = getArguments().getInt("cha");
        stStr = getArguments().getInt("stStr");
        stDex = getArguments().getInt("stDex");
        stCon = getArguments().getInt("stCon");
        stInt = getArguments().getInt("stInt");
        stWis = getArguments().getInt("stWis");
        stCha = getArguments().getInt("stCha");


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
        View view = inflater.inflate(R.layout.savingthrow_dialogfragment, null);

        cardStrength = (CardView) view.findViewById(R.id.cardSTStrength);
        cardDex = (CardView) view.findViewById(R.id.cardSTDex);
        cardCon = (CardView) view.findViewById(R.id.cardSTCon);
        cardInt = (CardView) view.findViewById(R.id.cardSTInt);
        cardWis = (CardView) view.findViewById(R.id.cardSTWis);
        cardCha = (CardView) view.findViewById(R.id.cardSTCha);

        tvStrResult = (TextView) view.findViewById(R.id.tvStrengthResult);
        tvStrDetail = (TextView) view.findViewById(R.id.tvStrengthDetail);
        tvLrStr = (TextView) view.findViewById(R.id.tvLrStrength);

        tvDexResult = (TextView) view.findViewById(R.id.tvDexResult);
        tvDexDetail = (TextView) view.findViewById(R.id.tvDexDetail);
        tvLrDex = (TextView) view.findViewById(R.id.tvLrDex);

        tvConResult = (TextView) view.findViewById(R.id.tvConResult);
        tvConDetail = (TextView) view.findViewById(R.id.tvConDetail);
        tvLrCon = (TextView) view.findViewById(R.id.tvLrCon);

        tvIntResult = (TextView) view.findViewById(R.id.tvIntResult);
        tvIntDetail = (TextView) view.findViewById(R.id.tvIntDetail);
        tvLrInt = (TextView) view.findViewById(R.id.tvLrInt);

        tvWisResult = (TextView) view.findViewById(R.id.tvWisResult);
        tvWisDetail = (TextView) view.findViewById(R.id.tvWisDetail);
        tvLrWis = (TextView) view.findViewById(R.id.tvLrWis);

        tvChaResult = (TextView) view.findViewById(R.id.tvChaResult);
        tvChaDetail = (TextView) view.findViewById(R.id.tvChaDetail);
        tvLrCha = (TextView) view.findViewById(R.id.tvLrCha);


        cardStrength.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rollStStr();
            }
        });


        cardDex.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rollStDex();
            }
        });

        cardCon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rollStCon();
            }
        });

        cardInt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rollStInt();
            }
        });

        cardWis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rollStWis();
            }
        });

        cardCha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rollStCha();
            }
        });

        rollStStr();
        rollStDex();
        rollStCon();
        rollStInt();
        rollStWis();
        rollStCha();

        getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);

        return view;
    }

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

    private void rollStDex(){
        int mod = AbilityModifierCalculator.calculateMod(dex);
        int rolled = DiceHelper.d20();
        if(lrDex != 1000){
            tvLrDex.setText(Html.fromHtml("Last Roll: " + lrDex));
        }else{
            tvLrDex.setText(Html.fromHtml("Last Roll: -"));
        }
        if(stDex != 0 && stDex > mod){
            tvDexResult.setText(String.valueOf(rolled + stDex));
            if(stDex >= 0){
                tvDexDetail.setText(Html.fromHtml(rolled + " + " + stDex + " = "));
            }else{
                tvDexDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(stDex) + " = "));
            }
            lrDex = rolled + stDex;
        }else{
            tvDexResult.setText(String.valueOf(rolled + mod));
            if(mod >= 0){
                tvDexDetail.setText(Html.fromHtml(rolled + " + " + mod + " = "));
            }else{
                tvDexDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(mod) + " = "));
            }
            lrDex = rolled+mod;
        }
    }

    private void rollStCon(){
        int mod = AbilityModifierCalculator.calculateMod(con);
        int rolled = DiceHelper.d20();
        if(lrCon != 1000){
            tvLrCon.setText(Html.fromHtml("Last Roll: " + lrCon));
        }else{
            tvLrCon.setText(Html.fromHtml("Last Roll: -"));
        }
        if(stCon != 0 && stCha > mod){
            tvConResult.setText(String.valueOf(rolled + stCon));
            if(stCon >= 0){
                tvConDetail.setText(Html.fromHtml(rolled + " + " + stCon + " = "));
            }else{
                tvConDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(stCon) + " = "));
            }
            lrCon = rolled + stCon;
        }else{
            tvConResult.setText(String.valueOf(rolled + mod));
            if(mod >= 0){
                tvConDetail.setText(Html.fromHtml(rolled + " + " + mod + " = "));
            }else{
                tvConDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(mod) + " = "));
            }
            lrCon = rolled+mod;
        }
    }

    private void rollStInt(){
        int mod = AbilityModifierCalculator.calculateMod(intel);
        int rolled = DiceHelper.d20();
        if(lrInt != 1000){
            tvLrInt.setText(Html.fromHtml("Last Roll: " + lrInt));
        }else{
            tvLrInt.setText(Html.fromHtml("Last Roll: -"));
        }
        if(stInt != 0 && stInt > mod){
            tvIntResult.setText(String.valueOf(rolled + stInt));
            if(stInt >= 0){
                tvIntDetail.setText(Html.fromHtml(rolled + " + " + stInt + " = "));
            }else{
                tvIntDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(stInt) + " = "));
            }
            lrInt = rolled + stInt;
        }else{
            tvIntResult.setText(String.valueOf(rolled + mod));
            if(mod >= 0){
                tvIntDetail.setText(Html.fromHtml(rolled + " + " + mod + " = "));
            }else{
                tvIntDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(mod) + " = "));
            }
            lrInt = rolled+mod;
        }
    }

    private void rollStWis(){
        int mod = AbilityModifierCalculator.calculateMod(wis);
        int rolled = DiceHelper.d20();
        if(lrWis != 1000){
            tvLrWis.setText(Html.fromHtml("Last Roll: " + lrWis));
        }else{
            tvLrWis.setText(Html.fromHtml("Last Roll: -"));
        }
        if(stWis != 0 && stWis > mod){
            tvWisResult.setText(String.valueOf(rolled + stWis));
            if(stWis >= 0){
                tvWisDetail.setText(Html.fromHtml(rolled + " + " + stWis + " = "));
            }else{
                tvWisDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(stWis) + " = "));
            }
            lrWis = rolled + stWis;
        }else{
            tvWisResult.setText(String.valueOf(rolled + mod));
            if(mod >= 0){
                tvWisDetail.setText(Html.fromHtml(rolled + " + " + mod + " = "));
            }else{
                tvWisDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(mod) + " = "));
            }
            lrWis = rolled+mod;
        }
    }

    private void rollStCha(){
        int mod = AbilityModifierCalculator.calculateMod(cha);
        int rolled = DiceHelper.d20();
        if(lrCha != 1000){
            tvLrCha.setText(Html.fromHtml("Last Roll: " + lrCha));
        }else{
            tvLrCha.setText(Html.fromHtml("Last Roll: -"));
        }
        if(stCha != 0 && stCha > mod){
            tvChaResult.setText(String.valueOf(rolled + stWis));
            if(stCha >= 0){
                tvChaDetail.setText(Html.fromHtml(rolled + " + " + stCha + " = "));
            }else{
                tvChaDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(stCha) + " = "));
            }
            lrCha = rolled + stInt;
        }else{
            tvChaResult.setText(String.valueOf(rolled + mod));
            if(mod >= 0){
                tvChaDetail.setText(Html.fromHtml(rolled + " + " + mod + " = "));
            }else{
                tvChaDetail.setText(Html.fromHtml(rolled + " - " + Math.abs(mod) + " = "));
            }
            lrCha = rolled+mod;
        }
    }


}
