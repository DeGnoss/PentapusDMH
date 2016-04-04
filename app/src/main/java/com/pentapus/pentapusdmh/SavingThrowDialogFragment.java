package com.pentapus.pentapusdmh;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Koni on 02.04.2016.
 */
public class SavingThrowDialogFragment extends Fragment {
    private static final String ARG_PAGE = "ARG_PAGE";

    private StatusAdapter statusAdapter;
    private GridLayoutManager gridLayoutManager;
    private int id;
    private boolean blinded;


    public static SavingThrowDialogFragment newInstance(int page) {
        SavingThrowDialogFragment fragment = new SavingThrowDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.dialog_savingthrow, container, false);

        final TextView strengthMod = (TextView) view.findViewById(R.id.tvStrengthMod);
        final TextView dexMod = (TextView) view.findViewById(R.id.tvDexMod);
        final TextView constMod = (TextView) view.findViewById(R.id.tvConstMod);
        final TextView intMod = (TextView) view.findViewById(R.id.tvIntMod);
        final TextView wisMod = (TextView) view.findViewById(R.id.tvWisdomMod);
        final TextView charMod = (TextView) view.findViewById(R.id.tvCharismaMod);



        Button bRollStrength = (Button) view.findViewById(R.id.bRollStrength);
        bRollStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                strengthMod.setText("Modifier of 0 plus a roll of "+roll+" = "+roll);
            }
        });
        Button bRollDex = (Button) view.findViewById(R.id.bRollDex);
        bRollDex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                dexMod.setText("Modifier of 0 plus a roll of "+roll+" = "+roll);
            }
        });
        Button bRollConst = (Button) view.findViewById(R.id.bRollConst);
        bRollConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                constMod.setText("Modifier of 0 plus a roll of " + roll + " = " + roll);
            }
        });
        Button bRollWisdom = (Button) view.findViewById(R.id.bRollWisdom);
        bRollWisdom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                intMod.setText("Modifier of 0 plus a roll of " + roll + " = " + roll);
            }
        });
        Button bRollInt = (Button) view.findViewById(R.id.bRollInt);
        bRollInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                wisMod.setText("Modifier of 0 plus a roll of "+roll+" = "+roll);
            }
        });
        Button bRollCharisma = (Button) view.findViewById(R.id.bRollCharisma);
        bRollCharisma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll=DiceHelper.d20();
                charMod.setText("Modifier of 0 plus a roll of " + roll + " = " + roll);
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