package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;


public class DetailNPCFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NPC_ID = "npcId";
    private static final String NPC_NAME = "npcName";

    private int npcId;
    private String npcName;
    private Uri myFile;

    ImageView ivAvatar;
    TextView tvName, tvType, tvHp, tvAc, tvSpeed, tvStr, tvDex, tvCon, tvInt, tvWis, tvCha, tvDmgIm, tvConIm, tvSenses, tvLanguages, tvCR, tvAbility1, tvAbility2, tvActions, tvMultiattack, tvAction1;

    public DetailNPCFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param npcId    Parameter 2.
     * @return A new instance of fragment EncounterEditFragment.
     */
    public static DetailNPCFragment newInstance(int npcId) {
        DetailNPCFragment fragment = new DetailNPCFragment();
        Bundle args = new Bundle();
        args.putInt(NPC_ID, npcId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
                npcId = getArguments().getInt(NPC_ID);
                //npcName = getArguments().getString(NPC_NAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_monster_detail, container, false);
        //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) charEditView.getLayoutParams();
        //params.setMargins(0, Utils.getStatusBarHeight(getActivity()), 0, 0);
        charEditView.setBackgroundColor(Color.WHITE);
        tvName = (TextView) charEditView.findViewById(R.id.tvName);
        tvType = (TextView) charEditView.findViewById(R.id.tvType);
        tvAc = (TextView) charEditView.findViewById(R.id.tvAc);
        tvHp = (TextView) charEditView.findViewById(R.id.tvHp);
        tvSpeed = (TextView) charEditView.findViewById(R.id.tvSpeed);
        tvStr = (TextView) charEditView.findViewById(R.id.tvSTR);
        tvDex = (TextView) charEditView.findViewById(R.id.tvDEX);
        tvCon = (TextView) charEditView.findViewById(R.id.tvCON);
        tvInt = (TextView) charEditView.findViewById(R.id.tvINT);
        tvWis = (TextView) charEditView.findViewById(R.id.tvWIS);
        tvCha = (TextView) charEditView.findViewById(R.id.tvCHA);
        tvDmgIm = (TextView) charEditView.findViewById(R.id.tvDmgIm);
        tvConIm = (TextView) charEditView.findViewById(R.id.tvConIm);
        tvSenses = (TextView) charEditView.findViewById(R.id.tvSenses);
        tvLanguages = (TextView) charEditView.findViewById(R.id.tvLanguages);
        tvCR = (TextView) charEditView.findViewById(R.id.tvCR);
        tvAbility1 = (TextView) charEditView.findViewById(R.id.tvAbility1);
        tvAbility2 = (TextView) charEditView.findViewById(R.id.tvAbility2);
        tvActions = (TextView) charEditView.findViewById(R.id.tvActions);
        tvMultiattack = (TextView) charEditView.findViewById(R.id.tvMultiattack);
        tvAction1 = (TextView) charEditView.findViewById(R.id.tvAction1);


        ivAvatar = (ImageView) charEditView.findViewById(R.id.ivAvatar);



        loadMonsterInfo(tvName, tvType, tvAc, tvHp, tvSpeed, tvStr, tvDex, tvCon, tvInt, tvWis, tvCha, tvDmgIm, tvConIm, tvSenses, tvLanguages, tvCR, tvAbility1, tvAbility2, tvActions, tvMultiattack, tvAction1, ivAvatar, npcId);

        // Inflate the layout for this fragment
        return charEditView;
    }


    private void loadMonsterInfo(TextView tvName, TextView tvType, TextView tvAc, TextView tvHp, TextView tvSpeed, TextView tvStr, TextView tvDex, TextView tvCon, TextView tvInt, TextView tvWis, TextView tvCha, TextView tvDmgIm, TextView tvConIm, TextView tvSenses, TextView tvLanguages, TextView tvCR, TextView tvAbility1, TextView tvAbility2, TextView tvActions, TextView tvMultiattack, TextView tvAction1, ImageView ivAvatar, int id) {

        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_NPC_TEMPLATE, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            Spanned myType = Html.fromHtml("<i>Medium Construct, unaligned</i>");
            String myHp = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
            String myAc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
            String mySpeed = "25 ft.";
            String mySTR = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH)));
            String myDEX = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY)));
            String myCON = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION)));
            String myINT = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE)));
            String myWIS = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM)));
            String myCHA = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA)));
            myFile = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
            Spanned myDmgIm = Html.fromHtml("<b>Damage Immunities</b> poison, psychic");
            Spanned myConIm = Html.fromHtml("<b>Condition Immunities</b> blinded, charmed, deafened, exhaustion, frightened, paralyzed, petrified, poisoned");
            Spanned mySenses = Html.fromHtml("<b>Senses</b> blindsight 60 ft. (blind beyond this radius), passive Perception 6");
            Spanned myLanguages = Html.fromHtml("<b>Languages</b> -");
            Spanned myCR = Html.fromHtml("<b>Challenge</b> 1 (200 XP)");
            Spanned myAbility1 = Html.fromHtml("<b>Antimagic Susceptibility. </b>" + "The armor is incapacitated while in the area of an <i>antimagic field</i>. If targeted by <i>dispel magic </i>, the armor must succeed on a Constitution saving throw against the caster's spell save DC or fall unconscious for 1 minute.");
            Spanned myAbility2 = Html.fromHtml("<b>False Appearance. </b>" + "While the armor remains motionless, it is indistinguishable from a normal suit of armor.");
            Spanned myMultiAttack = Html.fromHtml("<b>Multiattack. </b>" + "The armor makes two melee attacks.");
            Spanned myAction1 = Html.fromHtml("<b>Slam. </b>" + "<i>Melee Weapon Attack:</i> +4 to hit, reach 5 ft., one target. <i>Hit:</i> 5 (1d6 + 2) bludgeoning damage.");

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myName);
            tvName.setText(myName);
            tvType.setText(myType);
            tvHp.setText(myHp);
            tvAc.setText(myAc);
            tvSpeed.setText(mySpeed);
            tvStr.setText(mySTR);
            tvDex.setText(myDEX);
            tvCon.setText(myCON);
            tvInt.setText(myINT);
            tvWis.setText(myWIS);
            tvCha.setText(myCHA);
            tvDmgIm.setText(myDmgIm);
            tvConIm.setText(myConIm);
            tvSenses.setText(mySenses);
            tvLanguages.setText(myLanguages);
            tvCR.setText(myCR);
            tvAbility1.setText(myAbility1);
            tvAbility2.setText(myAbility2);
            tvMultiattack.setText(myMultiAttack);
            tvAction1.setText(myAction1);
            ivAvatar.setImageURI(myFile);

            cursor.close();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().invalidateOptionsMenu();
        ((MainActivity)getActivity()).setFabVisibility(false);
        ((MainActivity)getActivity()).disableNavigationDrawer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
