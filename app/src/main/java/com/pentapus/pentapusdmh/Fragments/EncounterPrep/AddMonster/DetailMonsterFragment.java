package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;

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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.HelperClasses.HitDiceCalculator;
import com.pentapus.pentapusdmh.HelperClasses.Utils;
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
    TextView tvName, tvType, tvHp, tvAc, tvSpeed, tvStr, tvDex, tvCon, tvInt, tvWis, tvCha, tvDmgVul, tvDmgRes, tvDmgIm, tvConIm, tvSenses, tvLanguages, tvCR, tvAbility1,
            tvAbility2, tvAbility3, tvAbility4, tvAbility5, tvActions, tvMultiattack, tvAction1, tvAction2, tvAction3, tvAction4, tvAction5, tvSkills, tvInnate, tvSpellcasting,
            tvReactions, tvReaction1, tvReaction2, tvReaction3, tvReaction4, tvReaction5, tvLActions, tvLMultiattack, tvLAction1, tvLAction2, tvLAction3, tvLAction4, tvLAction5;
    View vLine4, vLine5, vLine6, vLine7;

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
        tvSkills = (TextView) charEditView.findViewById(R.id.tvSkills);
        tvDmgVul = (TextView) charEditView.findViewById(R.id.tvDmgVul);
        tvDmgRes = (TextView) charEditView.findViewById(R.id.tvDmgRes);
        tvDmgIm = (TextView) charEditView.findViewById(R.id.tvDmgIm);
        tvConIm = (TextView) charEditView.findViewById(R.id.tvConIm);
        tvSenses = (TextView) charEditView.findViewById(R.id.tvSenses);
        tvLanguages = (TextView) charEditView.findViewById(R.id.tvLanguages);
        tvCR = (TextView) charEditView.findViewById(R.id.tvCR);
        tvInnate = (TextView) charEditView.findViewById(R.id.tvInnate);
        tvSpellcasting = (TextView) charEditView.findViewById(R.id.tvSpellcasting);
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
        tvAction5 = (TextView) charEditView.findViewById(R.id.tvAction5);
        tvReactions = (TextView) charEditView.findViewById(R.id.tvReactions);
        tvReaction1 = (TextView) charEditView.findViewById(R.id.tvReaction1);
        tvReaction2 = (TextView) charEditView.findViewById(R.id.tvReaction2);
        tvReaction3 = (TextView) charEditView.findViewById(R.id.tvReaction3);
        tvReaction4 = (TextView) charEditView.findViewById(R.id.tvReaction4);
        tvReaction5 = (TextView) charEditView.findViewById(R.id.tvReaction5);
        tvLActions = (TextView) charEditView.findViewById(R.id.tvLActions);
        tvLMultiattack = (TextView) charEditView.findViewById(R.id.tvLMultiattack);
        tvLAction1 = (TextView) charEditView.findViewById(R.id.tvLAction1);
        tvLAction2 = (TextView) charEditView.findViewById(R.id.tvLAction2);
        tvLAction3 = (TextView) charEditView.findViewById(R.id.tvLAction3);
        tvLAction4 = (TextView) charEditView.findViewById(R.id.tvLAction4);
        tvLAction5 = (TextView) charEditView.findViewById(R.id.tvLAction5);
        vLine4 = charEditView.findViewById(R.id.line4);
        vLine5 = charEditView.findViewById(R.id.line5);
        vLine6 = charEditView.findViewById(R.id.line6);
        vLine7 = charEditView.findViewById(R.id.line7);


        ivAvatar = (ImageView) charEditView.findViewById(R.id.ivAvatar);



        loadMonsterInfo(tvName, tvType, tvAc, tvHp, tvSpeed, tvStr, tvDex, tvCon, tvInt, tvWis, tvCha, tvSkills, tvDmgVul, tvDmgRes, tvDmgIm, tvConIm, tvSenses, tvLanguages, tvCR, tvAbility1, tvAbility2, tvAbility3, tvAbility4, tvAbility5, tvActions, tvMultiattack, tvAction1, tvAction2, tvAction3, tvAction4, tvAction5, tvReactions, tvReaction1, tvReaction2, tvReaction3, tvReaction4, tvReaction5, tvLActions, tvLMultiattack, tvLAction1, tvLAction2, tvLAction3, tvLAction4, tvLAction5, ivAvatar, npcId);

        // Inflate the layout for this fragment
        return charEditView;
    }


    private void loadMonsterInfo(TextView tvName, TextView tvType, TextView tvAc, TextView tvHp, TextView tvSpeed, TextView tvStr, TextView tvDex, TextView tvCon, TextView tvInt, TextView tvWis, TextView tvCha, TextView tvSkills, TextView tvDmgVul, TextView tvDmgRes, TextView tvDmgIm, TextView tvConIm, TextView tvSenses, TextView tvLanguages, TextView tvCR, TextView tvAbility1, TextView tvAbility2, TextView tvAbility3, TextView tvAbility4, TextView tvAbility5, TextView tvActions, TextView tvMultiattack, TextView tvAction1, TextView tvAction2, TextView tvAction3, TextView tvAction4, TextView tvAction5, TextView tvReactions, TextView tvReaction1, TextView tvReaction2, TextView tvReaction3, TextView tvReaction4, TextView tvReaction5, TextView tvLActions, TextView tvLMultiattack, TextView tvLAction1, TextView tvLAction2, TextView tvLAction3, TextView tvLAction4, TextView tvLAction5, ImageView ivAvatar, int id) {
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
        Spanned myAction5 = null;
        Spanned myCR = null;
        Spanned myHp = null;
        Spanned myType = null;
        Spanned innate = null;
        Spanned spellcasting = null;
        Spanned myReaction1 = null;
        Spanned myReaction2 = null;
        Spanned myReaction3 = null;
        Spanned myReaction4 = null;
        Spanned myReaction5 = null;

        Spanned myLMultiAttack = null;
        Spanned myLAction1 = null;
        Spanned myLAction2 = null;
        Spanned myLAction3 = null;
        Spanned myLAction4 = null;
        Spanned myLAction5 = null;




        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_MONSTER_TEMPLATE, null, null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            //Name, Size, Type & Alignment
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));

            String tempSize = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SIZE));
            if(tempSize == null){
                tempSize = "";
            }
            String tempType = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE));
            if(tempType == null){
                tempType = "";
            }
            String tempAlignment = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ALIGNMENT));
            if(tempAlignment == null){
                tempAlignment = "";
            }

            if(tempAlignment == null || tempAlignment.isEmpty()){
                myType = Html.fromHtml("<i>" + tempSize + " " + tempType + "</i>");
            }else{
                myType = Html.fromHtml("<i>" + tempSize + " " + tempType + ", " + tempAlignment + "</i>");
            }


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

            //HP, AC & Speed
            String tempHpMax = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
            int tempHpDice = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_HPROLL));
            if(tempHpDice == 0){
                if(tempHpMax == null || tempHpMax.isEmpty()){
                    tempHpMax = "-";
                }
                myHp = Html.fromHtml("<b>Hit Points</b> " + tempHpMax);
            }else{
                if(tempHpMax == null){
                    tempHpMax = "";
                }
                String hpDice = HitDiceCalculator.calculateHdType(tempHpDice, tempSize, AbilityModifierCalculator.calculateMod(CON));
                myHp = Html.fromHtml("<b>Hit Points</b> " + tempHpMax + " (" + tempHpDice + hpDice + ")");
            }

            String tempAcType = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ACTYPE));
            String tempAc2Type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC2TYPE));
            String tempAc2 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC2));
            String tempAc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
            if(tempAc == null){
                tempAc = "";
            }
            if(tempAcType != null && !tempAcType.isEmpty()){
                if(tempAc2Type != null && !tempAc2Type.isEmpty()){
                    myAc = Html.fromHtml("<b>Armor Class </b>" + tempAc + " (" + tempAcType + ")" + ", " + tempAc2 + " " + " (" + tempAc2Type + ")");
                }else{
                    myAc = Html.fromHtml("<b>Armor Class </b>" + tempAc + " (" + tempAcType + ")");
                }
            }else{
                if(tempAc2Type != null && !tempAc2Type.isEmpty()){
                    myAc = Html.fromHtml("<b>Armor Class </b>" + tempAc + ", " + tempAc2 + " (" + tempAc2Type + ")");
                }else{
                    if(tempAc == null){
                        tempAc = "-";
                    }
                    myAc = Html.fromHtml("<b>Armor Class </b>" + tempAc);
                }
            }

            String tempSpeed = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SPEED));
            if(tempSpeed == null){
                tempSpeed = "-";
            }
            Spanned mySpeed = Html.fromHtml("<b>Speed </b>" + tempSpeed);


            //Icon
            String tempUri = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON));
            if(tempUri != null && !tempUri.isEmpty()){
                myFile = Uri.parse(tempUri);
            }else{
                myFile = Uri.parse("android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight");
            }

            //Skills
            String tempSkills ="";
            String[] skillKeysArray = {DataBaseHandler.KEY_ACROBATICS, DataBaseHandler.KEY_ANIMALHANDLING, DataBaseHandler.KEY_ARCANA, DataBaseHandler.KEY_ATHLETICS, DataBaseHandler.KEY_DECEPTION, DataBaseHandler.KEY_HISTORY, DataBaseHandler.KEY_INSIGHT, DataBaseHandler.KEY_INTIMIDATION, DataBaseHandler.KEY_INVESTIGATION, DataBaseHandler.KEY_MEDICINE, DataBaseHandler.KEY_NATURE, DataBaseHandler.KEY_PERCEPTION, DataBaseHandler.KEY_PERFORMANCE, DataBaseHandler.KEY_PERSUASION, DataBaseHandler.KEY_RELIGION, DataBaseHandler.KEY_SLEIGHTOFHAND, DataBaseHandler.KEY_STEALTH, DataBaseHandler.KEY_SURVIVAL};
            String[] skillNameArray = {"Acrobatics", "Animal Handling", "Arcana", "Athletics", "Deception", "History", "Insight", "Intimidation", "Investigation", "Medicine", "Nature", "Perception", "Performance", "Persuasion", "Religion", "Sleight of Hand", "Stealth", "Survival"};

            for(int i=0; i<skillKeysArray.length; i++) {
                if (cursor.getInt(cursor.getColumnIndexOrThrow(skillKeysArray[i])) != 0) {
                    if (tempSkills.isEmpty()) {
                        if (cursor.getInt(cursor.getColumnIndexOrThrow(skillKeysArray[i])) > 0) {
                            tempSkills = Html.fromHtml("<b>Skills: </b> " ) + skillNameArray[i] + " +" + cursor.getInt(cursor.getColumnIndexOrThrow(skillKeysArray[i]));
                        } else {
                            tempSkills = Html.fromHtml("<b>Skills: </b> " ) + skillNameArray[i] + "+" + cursor.getInt(cursor.getColumnIndexOrThrow(skillKeysArray[i]));
                        }
                    } else {
                        if (cursor.getInt(cursor.getColumnIndexOrThrow(skillKeysArray[i])) > 0) {
                            tempSkills = tempSkills + ", " + skillNameArray[i] + " +" + cursor.getInt(cursor.getColumnIndexOrThrow(skillKeysArray[i]));
                        } else {
                            tempSkills = tempSkills + ", " + skillNameArray[i] + " +" + cursor.getInt(cursor.getColumnIndexOrThrow(skillKeysArray[i]));
                        }
                    }
                }
            }
            if(!tempSkills.isEmpty()){
                tvSkills.setText(tempSkills);
            }else{
                tvSkills.setVisibility(View.GONE);
            }


            //Damage Vulnerabilities
            String tempDmgVul = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGVUL));
            if(tempDmgVul != null && !tempDmgVul.isEmpty()){
                myDmgVul = Html.fromHtml("<b>Damage Vulnerabilities</b> " + tempDmgVul.toLowerCase());
            }else{
                tvDmgVul.setVisibility(View.GONE);
            }


            //Damage Resistances
            String tempDmgRes = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGRES));
            if(tempDmgRes != null && !tempDmgRes.isEmpty()){
                myDmgRes = Html.fromHtml("<b>Damage Resistances</b> " + tempDmgRes.toLowerCase());
            }else{
                tvDmgRes.setVisibility(View.GONE);
            }


            //Damage Immunities
            String tempDmgIm = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DMGIM));
            if(tempDmgIm != null && !tempDmgIm.isEmpty()){
                myDmgIm = Html.fromHtml("<b>Damage Immunities</b> " + tempDmgIm.toLowerCase());
            }else{
                tvDmgIm.setVisibility(View.GONE);
            }


            //Condition Immunities
            String tempConIm = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONIM));
            if(tempConIm != null && !tempConIm.isEmpty()){
                myConIm = Html.fromHtml("<b>Condition Immunities</b> " + tempConIm.toLowerCase());
            }else{
                tvConIm.setVisibility(View.GONE);
            }


            //Senses
            String tempSenses = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SENSES));
            if(tempSenses == null || tempSenses.isEmpty()){
                tempSenses = "-";
            }
            Spanned mySenses = Html.fromHtml("<b>Senses </b> " + tempSenses);

            //Languages
            String tempLanguages = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LANGUAGES));
            if (tempLanguages == null || tempLanguages.isEmpty()) {
                tempLanguages = "-";
            }
            Spanned myLanguages = Html.fromHtml("<b>Languages</b> " + tempLanguages);

            //CR & XP
            String tempCr = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CR));
            String tempXP = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_XP));

            if(tempXP == null || tempXP.isEmpty()){
                if(tempCr == null || tempCr.isEmpty()){
                    tempCr = "-";
                }
                myCR = Html.fromHtml("<b>Challenge</b> " + tempCr);
            }else{
                if(tempCr == null){
                    tempCr = "";
                }
                myCR = Html.fromHtml("<b>Challenge</b> " + tempCr + " (" + tempXP + " XP" + ")");
            }

            //Innate Spellcasting & Spellcasting
            String tempInnate = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INDESCRIPTION));
            String tempInnateSpellsKnown = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INSPELLSSTRING));
            if(tempInnate != null){
                innate = Html.fromHtml("<b>Innate Spellcasting. </b>" + tempInnate);
                if(tempInnateSpellsKnown != null){
                    innate = (Spanned) TextUtils.concat(innate, Html.fromHtml("<br/>" + tempInnateSpellsKnown));
                }
                tvInnate.setText(Utils.trimTrailingWhitespace(innate));
            }else{
                tvInnate.setVisibility(View.GONE);
                vLine4.setVisibility(View.GONE);
            }


            String tempSc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SCDESCRIPTION));
            String tempScSpellsKnown = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SCSPELLSSTRING));
            if(tempSc != null){
                spellcasting = Html.fromHtml("<b>Spellcasting. </b>" + tempSc);
                if(tempScSpellsKnown != null){
                    spellcasting = (Spanned) TextUtils.concat(spellcasting, Html.fromHtml("<br/>" + tempScSpellsKnown));
                }
                vLine4.setVisibility(View.VISIBLE);
                tvSpellcasting.setText(Utils.trimTrailingWhitespace(spellcasting));
            }else{
                tvSpellcasting.setVisibility(View.GONE);
            }


            //Traits
            String tempAbility = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1NAME));
            if(tempAbility != null && !tempAbility.isEmpty()){
                myAbility1 = Html.fromHtml("<b>" + tempAbility + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ABILITY1DESC)));
                vLine4.setVisibility(View.VISIBLE);
            }else{
                tvAbility1.setVisibility(View.GONE);
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
                tvActions.setVisibility(View.GONE);
                vLine5.setVisibility(View.GONE);
            }

            String tempAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1NAME));
            if(tempAction != null && !tempAction.isEmpty()){
                myAction1 = Html.fromHtml("<b>" + tempAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK1DESC)));
                vLine5.setVisibility(View.VISIBLE);
                tvActions.setVisibility(View.VISIBLE);
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
            tempAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5NAME));
            if(tempAction != null && !tempAction.isEmpty()){
                myAction5 = Html.fromHtml("<b>" + tempAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ATK5DESC)));
            }else{
                tvAction5.setVisibility(View.GONE);
            }


            String tempReaction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION1NAME));
            if(tempReaction != null && !tempReaction.isEmpty()){
                myReaction1 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION1DESC)));
                vLine6.setVisibility(View.VISIBLE);
                tvReactions.setVisibility(View.VISIBLE);
            }else{
                tvAction1.setVisibility(View.GONE);
                vLine6.setVisibility(View.GONE);
                tvReactions.setVisibility(View.GONE);
            }
            tempReaction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION2NAME));
            if(tempReaction != null && !tempReaction.isEmpty()){
                myReaction2 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION2DESC)));
            }else{
                tvReaction2.setVisibility(View.GONE);
            }
            tempReaction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION3NAME));
            if(tempReaction != null && !tempReaction.isEmpty()){
                myReaction3 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION3DESC)));
            }else{
                tvReaction3.setVisibility(View.GONE);
            }
            tempReaction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION4NAME));
            if(tempReaction != null && !tempReaction.isEmpty()){
                myReaction4 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION4DESC)));
            }else{
                tvReaction4.setVisibility(View.GONE);
            }
            tempReaction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION5NAME));
            if(tempReaction != null && !tempReaction.isEmpty()){
                myReaction5 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_REACTION5DESC)));
            }else{
                tvReaction5.setVisibility(View.GONE);
            }




            //Legendary Actions
            String tempLMultiAttack = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LMULTIATTACK));
            if(tempLMultiAttack != null && !tempLMultiAttack.isEmpty()){
                myLMultiAttack = Html.fromHtml(tempLMultiAttack);
            }else{
                tvLMultiattack.setVisibility(View.GONE);
                tvLActions.setVisibility(View.GONE);
                vLine7.setVisibility(View.GONE);
            }

            String tempLAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1NAME));
            if(tempLAction != null && !tempLAction.isEmpty()){
                myLAction1 = Html.fromHtml("<b>" + tempLAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK1DESC)));
                vLine7.setVisibility(View.VISIBLE);
                tvLActions.setVisibility(View.VISIBLE);
            }else{
                tvLAction1.setVisibility(View.GONE);
            }
            tempLAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2NAME));
            if(tempLAction != null && !tempLAction.isEmpty()){
                myLAction2 = Html.fromHtml("<b>" + tempLAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK2DESC)));
            }else{
                tvLAction2.setVisibility(View.GONE);
            }
            tempLAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3NAME));
            if(tempLAction != null && !tempLAction.isEmpty()){
                myLAction3 = Html.fromHtml("<b>" + tempLAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK3DESC)));
            }else{
                tvLAction3.setVisibility(View.GONE);
            }
            tempLAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4NAME));
            if(tempLAction != null && !tempLAction.isEmpty()){
                myLAction4 = Html.fromHtml("<b>" + tempLAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK4DESC)));
            }else{
                tvLAction4.setVisibility(View.GONE);
            }
            tempLAction = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5NAME));
            if(tempLAction != null && !tempLAction.isEmpty()){
                myLAction5 = Html.fromHtml("<b>" + tempLAction + "</b>" + ". " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LATK5DESC)));
            }else{
                tvLAction5.setVisibility(View.GONE);
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
            tvAction5.setText(myAction5);
            tvReaction1.setText(myReaction1);
            tvReaction2.setText(myReaction2);
            tvReaction3.setText(myReaction3);
            tvReaction4.setText(myReaction4);
            tvReaction5.setText(myReaction5);
            tvLMultiattack.setText(myLMultiAttack);
            tvLAction1.setText(myLAction1);
            tvLAction2.setText(myLAction2);
            tvLAction3.setText(myLAction3);
            tvLAction4.setText(myLAction4);
            tvLAction5.setText(myLAction5);
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
