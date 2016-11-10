package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;


public class DetailMonsterFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NPC_ID = "npcId";
    private static final String NPC_NAME = "npcName";

    private int npcId;
    private String npcName;
    private Uri myFile;

    ImageView ivAvatar;
    TextView tvName, tvType, tvHp, tvAc, tvSpeed, tvStr, tvDex, tvCon, tvInt, tvWis, tvCha, tvDmgVul, tvDmgRes, tvDmgIm, tvConIm, tvSenses, tvLanguages, tvCR, tvAbility1, tvAbility2, tvAbility3, tvAbility4, tvAbility5, tvActions, tvMultiattack, tvAction1, tvAction2, tvAction3, tvAction4;
    View vLine4;
    RelativeLayout.LayoutParams rlp;

    public DetailMonsterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param npcId    Parameter 2.
     * @return A new instance of fragment EncounterEditFragment.
     */
    public static DetailMonsterFragment newInstance(int npcId) {
        DetailMonsterFragment fragment = new DetailMonsterFragment();
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
        tvDmgVul = (TextView) charEditView.findViewById(R.id.tvDmgVul);
        tvDmgRes = (TextView) charEditView.findViewById(R.id.tvDmgRes);
        tvDmgIm = (TextView) charEditView.findViewById(R.id.tvDmgIm);
        tvConIm = (TextView) charEditView.findViewById(R.id.tvConIm);
        tvSenses = (TextView) charEditView.findViewById(R.id.tvSenses);
        tvLanguages = (TextView) charEditView.findViewById(R.id.tvLanguages);
        tvCR = (TextView) charEditView.findViewById(R.id.tvCR);
        tvAbility1 = (TextView) charEditView.findViewById(R.id.tvAbility1);
        tvAbility2 = (TextView) charEditView.findViewById(R.id.tvAbility2);
        tvAbility3 = (TextView) charEditView.findViewById(R.id.tvAbility3);
        tvAbility4 = (TextView) charEditView.findViewById(R.id.tvAbility4);
        tvAbility5 = (TextView) charEditView.findViewById(R.id.tvAbility5);
        tvActions = (TextView) charEditView.findViewById(R.id.tvActions);
        tvMultiattack = (TextView) charEditView.findViewById(R.id.tvMultiattack);
        tvAction1 = (TextView) charEditView.findViewById(R.id.tvAction1);
        tvAction2 = (TextView) charEditView.findViewById(R.id.tvAction2);
        tvAction3 = (TextView) charEditView.findViewById(R.id.tvAction3);
        tvAction4 = (TextView) charEditView.findViewById(R.id.tvAction4);
        vLine4 = charEditView.findViewById(R.id.line4);


        ivAvatar = (ImageView) charEditView.findViewById(R.id.ivAvatar);



        loadMonsterInfo(tvName, tvType, tvAc, tvHp, tvSpeed, tvStr, tvDex, tvCon, tvInt, tvWis, tvCha, tvDmgVul, tvDmgRes, tvDmgIm, tvConIm, tvSenses, tvLanguages, tvCR, tvAbility1, tvAbility2, tvAbility3, tvAbility4, tvAbility5, tvActions, tvMultiattack, tvAction1, tvAction2, tvAction3, tvAction4, ivAvatar, npcId);

        // Inflate the layout for this fragment
        return charEditView;
    }


    private void loadMonsterInfo(TextView tvName, TextView tvType, TextView tvAc, TextView tvHp, TextView tvSpeed, TextView tvStr, TextView tvDex, TextView tvCon, TextView tvInt, TextView tvWis, TextView tvCha, TextView tvDmgVul, TextView tvDmgRes, TextView tvDmgIm, TextView tvConIm, TextView tvSenses, TextView tvLanguages, TextView tvCR, TextView tvAbility1, TextView tvAbility2, TextView tvAbility3, TextView tvAbility4, TextView tvAbility5, TextView tvActions, TextView tvMultiattack, TextView tvAction1, TextView tvAction2, TextView tvAction3, TextView tvAction4, ImageView ivAvatar, int id) {
        Spanned myDmgVul = null;
        Spanned myDmgRes = null;
        Spanned myDmgIm = null;
        Spanned myConIm = null;
        Spanned myAc = null;
        Spanned myAbility1 = null;
        Spanned myAbility2 = null;
        Spanned myAbility3 = null;
        Spanned myAbility4 = null;
        Spanned myAbility5 = null;
        Spanned myMultiAttack = null;
        Spanned myAction1 = null;
        Spanned myAction2 = null;
        Spanned myAction3 = null;
        Spanned myAction4 = null;




        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_MONSTER_TEMPLATE, null, null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            //Name & Type
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            Spanned myType = Html.fromHtml("<i>" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MONSTERTYPE)) + ", " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ALIGNMENT)) + "</i>");

            //HP, AC & Speed
            Spanned myHp = Html.fromHtml("<b>Hit Points </b>" +cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP)) + " (" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_HPROLL)) + ")");

            String tempAcType = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ACTYPE));
            String tempAc2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC2));
            if(tempAcType != null && !tempAcType.isEmpty()){
                if(tempAc2 != null && !tempAc2.isEmpty()){
                    myAc = Html.fromHtml("<b>AC </b>" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)) + " (" + tempAcType + ")" + ", " + tempAc2 + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC2TYPE)));
                }else{
                    myAc = Html.fromHtml("<b>AC </b>" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)) + " (" + tempAcType + ")");
                }
            }else{
                myAc = Html.fromHtml("<b>AC </b>" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)));
                if(tempAc2 != null && !tempAc2.isEmpty()){
                    myAc = Html.fromHtml("<b>AC </b>" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)) + ", " + tempAc2 + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC2TYPE)));
                }else{
                    myAc = Html.fromHtml("<b>AC </b>" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)));
                }
            }

            Spanned mySpeed = Html.fromHtml("<b>Speed </b>" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SPEED)));


            //Abilities
            int STR = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH));
            String mySTR = String.valueOf(STR) + " (" + AbilityModifierCalculator.calculateModString(STR) + ")";
            int DEX = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY));
            String myDEX = String.valueOf(DEX) + " (" + AbilityModifierCalculator.calculateModString(DEX) + ")";
            int CON = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION));
            String myCON = String.valueOf(CON) + " (" + AbilityModifierCalculator.calculateModString(CON) + ")";
            int INT = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE));
            String myINT = String.valueOf(INT) + " (" + AbilityModifierCalculator.calculateModString(INT) + ")";
            int WIS = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM));
            String myWIS = String.valueOf(WIS) + " (" + AbilityModifierCalculator.calculateModString(WIS) + ")";
            int CHA = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA));
            String myCHA = String.valueOf(CHA) + " (" + AbilityModifierCalculator.calculateModString(CHA) + ")";


            //Icon
            String tempUri = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON));
            if(tempUri != null && !tempUri.isEmpty()){
                myFile = Uri.parse(tempUri);
            }else{
                myFile = Uri.parse("android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight");
            }


            //Damage Vulnerabilities
            String tempDmgVul = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGVUL));
            if(tempDmgVul != null && !tempDmgVul.isEmpty()){
                myDmgVul = Html.fromHtml("<b>Damage Vulnerabilities</b> " + tempDmgVul);
            }else{
                tvDmgVul.setVisibility(View.GONE);
            }


            //Damage Resistances
            String tempDmgRes = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGRES));
            if(tempDmgRes != null && !tempDmgRes.isEmpty()){
                myDmgRes = Html.fromHtml("<b>Damage Resistances</b> " + tempDmgRes);
            }else{
                tvDmgRes.setVisibility(View.GONE);
            }


            //Damage Immunities
            String tempDmgIm = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGIM));
            if(tempDmgIm != null && !tempDmgIm.isEmpty()){
                myDmgIm = Html.fromHtml("<b>Damage Immunities</b> " + tempDmgIm);
            }else{
                tvDmgIm.setVisibility(View.GONE);
            }


            //Condition Immunities
            String tempConIm = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONIM));
            if(tempConIm != null && !tempConIm.isEmpty()){
                myConIm = Html.fromHtml("<b>Condition Immunities</b> " + tempConIm);
            }else{
                tvConIm.setVisibility(View.GONE);
            }


            //Senses, Languages & CR
            Spanned mySenses = Html.fromHtml("<b>Senses </b> " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SENSES)));
            String tempLanguages = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LANGUAGES));
            if (tempLanguages == null || tempLanguages.isEmpty()) {
                tempLanguages = "-";
            }
            Spanned myLanguages = Html.fromHtml("<b>Languages</b> " + tempLanguages);
            Spanned myCR = Html.fromHtml("<b>Challenge</b> " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CR)) + " (" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_XP)) + " XP" + ")");


            //Feats
            String tempAbility = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1NAME));
            if(tempAbility != null && !tempAbility.isEmpty()){
                myAbility1 = Html.fromHtml("<b>" + tempAbility + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1DESC)));
            }else{
                tvAbility1.setVisibility(View.GONE);
                vLine4.setVisibility(View.GONE);
            }
            tempAbility = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY2NAME));
            if(tempAbility != null && !tempAbility.isEmpty()){
                myAbility2 = Html.fromHtml("<b>" + tempAbility + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY2DESC)));
            }else{
                tvAbility2.setVisibility(View.GONE);
            }
            tempAbility = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY3NAME));
            if(tempAbility != null && !tempAbility.isEmpty()){
                myAbility3 = Html.fromHtml("<b>" + tempAbility + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY3DESC)));
            }else{
                tvAbility3.setVisibility(View.GONE);
            }
            tempAbility = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY4NAME));
            if(tempAbility != null && !tempAbility.isEmpty()){
                myAbility4 = Html.fromHtml("<b>" + tempAbility + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY4DESC)));
            }else{
                tvAbility4.setVisibility(View.GONE);
            }
            tempAbility = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY5NAME));
            if(tempAbility != null && !tempAbility.isEmpty()){
                myAbility5 = Html.fromHtml("<b>" + tempAbility + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY5DESC)));
            }else{
                tvAbility5.setVisibility(View.GONE);
            }


            //MultiAttack & Actions
            String tempMultiAttack = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MULTIATTACK));
            if(tempMultiAttack != null && !tempMultiAttack.isEmpty()){
                myMultiAttack = Html.fromHtml("<b>Multiattack. </b>" + tempMultiAttack);
            }else{
                tvMultiattack.setVisibility(View.GONE);
            }

            String tempAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1NAME));
            if(tempAction != null && !tempAction.isEmpty()){
                myAction1 = Html.fromHtml("<b>" + tempAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DESC)));
            }else{
                tvAction1.setVisibility(View.GONE);
            }
            tempAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2NAME));
            if(tempAction != null && !tempAction.isEmpty()){
                myAction2 = Html.fromHtml("<b>" + tempAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK2DESC)));
            }else{
                tvAction2.setVisibility(View.GONE);
            }
            tempAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3NAME));
            if(tempAction != null && !tempAction.isEmpty()){
                myAction3 = Html.fromHtml("<b>" + tempAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK3DESC)));
            }else{
                tvAction3.setVisibility(View.GONE);
            }
            tempAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4NAME));
            if(tempAction != null && !tempAction.isEmpty()){
                myAction4 = Html.fromHtml("<b>" + tempAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK4DESC)));
            }else{
                tvAction4.setVisibility(View.GONE);
            }


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
            tvDmgVul.setText(myDmgVul);
            tvDmgRes.setText(myDmgRes);
            tvDmgIm.setText(myDmgIm);
            tvConIm.setText(myConIm);
            tvSenses.setText(mySenses);
            tvLanguages.setText(myLanguages);
            tvCR.setText(myCR);
            tvAbility1.setText(myAbility1);
            tvAbility2.setText(myAbility2);
            tvAbility3.setText(myAbility3);
            tvAbility4.setText(myAbility4);
            tvAbility5.setText(myAbility5);
            tvMultiattack.setText(myMultiAttack);
            tvAction1.setText(myAction1);
            tvAction2.setText(myAction2);
            tvAction3.setText(myAction3);
            tvAction4.setText(myAction4);
            ivAvatar.setImageURI(myFile);

            assert cursor != null;
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
