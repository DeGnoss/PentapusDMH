package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerFragment;
import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerInfoCard;
import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.HelperClasses.Utils;
import com.pentapus.pentapusdmh.NumberPicker.NumberPickerBuilder;
import com.pentapus.pentapusdmh.NumberPicker.NumberPickerDialogFragment;
import com.pentapus.pentapusdmh.R;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.pentapus.pentapusdmh.R.id.tvAbility1;

/**
 * Created by Koni on 02.04.2016.
 */
public class HpOverviewFragment extends Fragment implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2 {

    private int id;
    private View vFrame;
    private ImageButton bDamage, bHealing, bST;
    private ImageView ivIcon;
    private TextView tvDamage, tvIdentifier, tvName, tvHpCurrent, tvHpMax, tvAc, tvType, tvStr, tvDex, tvCon, tvInt, tvWis, tvCha, tvSpeed, tvSenses, tvLanguages, tvDmgVul, tvDmgRes, tvDmgIm, tvConIm,
            tvAbility1, tvAbility2, tvAbility3, tvAbility4, tvAbility5, tvReaction1, tvReactions, tvReaction2, tvReaction3, tvReaction4, tvReaction5, tvInnate, tvSpellcasting;
    private View vLine5, vLine6;
    private int hpDiff;
    private int hpCurrent;
    private int hpMax;
    private Spanned monstertype, ac, speed, senses, languages, dmgVul, dmgRes, dmgIm, conIm, ability1, ability2, ability3, ability4, ability5, reaction1;
    private Spanned str, dex, con, intel, wis, cha;
    private String name;
    private Uri iconUri;
    private int type;
    private boolean isTemp = false, isHeal = false;
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
        selectedCharacter = ((TrackerFragment) getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().getItem(id);


    }


    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_hpoverview, container, false);

        //tvDamage = (TextView) view.findViewById(R.id.tvNumber);
        // tvIdentifier = (TextView) view.findViewById(R.id.tvIdent);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvHpCurrent = (TextView) view.findViewById(R.id.tvHpCurrent);
        tvHpMax = (TextView) view.findViewById(R.id.tvHpMax);
        tvAc = (TextView) view.findViewById(R.id.tvAc);
        ivIcon = (ImageView) view.findViewById(R.id.ivAvatar);
        vFrame = (View) view.findViewById(R.id.vFrame);
        tvType = (TextView) view.findViewById(R.id.tvType);
        tvStr = (TextView) view.findViewById(R.id.tvSTR);
        tvDex = (TextView) view.findViewById(R.id.tvDEX);
        tvCon = (TextView) view.findViewById(R.id.tvCON);
        tvInt = (TextView) view.findViewById(R.id.tvINT);
        tvWis = (TextView) view.findViewById(R.id.tvWIS);
        tvCha = (TextView) view.findViewById(R.id.tvCHA);
        tvSpeed = (TextView) view.findViewById(R.id.tvSpeed);
        tvSenses = (TextView) view.findViewById(R.id.tvSenses);
        tvLanguages = (TextView) view.findViewById(R.id.tvLanguages);
        tvDmgVul = (TextView) view.findViewById(R.id.tvDmgVul);
        tvDmgRes = (TextView) view.findViewById(R.id.tvDmgRes);
        tvDmgIm = (TextView) view.findViewById(R.id.tvDmgIm);
        tvConIm = (TextView) view.findViewById(R.id.tvConIm);
        tvInnate = (TextView) view.findViewById(R.id.tvInnate);
        tvSpellcasting = (TextView) view.findViewById(R.id.tvSpellcasting);
        tvAbility1 = (TextView) view.findViewById(R.id.tvAbility1);
        tvAbility2 = (TextView) view.findViewById(R.id.tvAbility2);
        tvAbility3 = (TextView) view.findViewById(R.id.tvAbility3);
        tvAbility4 = (TextView) view.findViewById(R.id.tvAbility4);
        tvAbility5 = (TextView) view.findViewById(R.id.tvAbility5);
        tvReactions = (TextView) view.findViewById(R.id.tvReactions);
        tvReaction1 = (TextView) view.findViewById(R.id.tvReaction1);
        tvReaction2 = (TextView) view.findViewById(R.id.tvReaction2);
        tvReaction3 = (TextView) view.findViewById(R.id.tvReaction3);
        tvReaction4 = (TextView) view.findViewById(R.id.tvReaction4);
        tvReaction5 = (TextView) view.findViewById(R.id.tvReaction5);
        vLine5 = view.findViewById(R.id.line5);
        vLine6 = view.findViewById(R.id.line6);



        hpCurrent = selectedCharacter.getHp();
        hpMax = selectedCharacter.getMaxHp();
        monstertype = Html.fromHtml("<i>" + selectedCharacter.getMonsterType() + ", " + selectedCharacter.getAlignment() + "</i>");
        if(!String.valueOf(selectedCharacter.getAc()).isEmpty()){
            if(selectedCharacter.getAcType2() != null && !selectedCharacter.getAcType2().isEmpty()){
                ac = Html.fromHtml("<b>AC </b>" + String.valueOf(selectedCharacter.getAc()) + " (" + selectedCharacter.getAcType() + ")" + ", " + String.valueOf(selectedCharacter.getAc2()) + " " + selectedCharacter.getAcType2());
            }else{
                ac = Html.fromHtml("<b>AC </b>" + String.valueOf(selectedCharacter.getAc()) + " (" + selectedCharacter.getAcType() + ")");
            }
        }else{
            if(selectedCharacter.getAcType2() != null && !selectedCharacter.getAcType2().isEmpty()){
                ac = Html.fromHtml("<b>AC </b>" + String.valueOf(selectedCharacter.getAc()) + ", " + String.valueOf(selectedCharacter.getAc2()) + " " + selectedCharacter.getAcType2());
            }else{
                ac = Html.fromHtml("<b>AC </b>" + String.valueOf(selectedCharacter.getAc()));
            }
        }

        str = Html.fromHtml(String.valueOf(selectedCharacter.getStrength()) + " (" + AbilityModifierCalculator.calculateModString(selectedCharacter.getStrength()) + ")");
        dex = Html.fromHtml(String.valueOf(selectedCharacter.getDexterity()) + " (" + AbilityModifierCalculator.calculateModString(selectedCharacter.getDexterity()) + ")");
        con = Html.fromHtml(String.valueOf(selectedCharacter.getConstitution()) + " (" + AbilityModifierCalculator.calculateModString(selectedCharacter.getConstitution()) + ")");
        intel = Html.fromHtml(String.valueOf(selectedCharacter.getIntelligence()) + " (" + AbilityModifierCalculator.calculateModString(selectedCharacter.getIntelligence()) + ")");
        wis = Html.fromHtml(String.valueOf(selectedCharacter.getWisdom()) + " (" + AbilityModifierCalculator.calculateModString(selectedCharacter.getWisdom()) + ")");
        cha = Html.fromHtml(String.valueOf(selectedCharacter.getCharisma()) + " (" + AbilityModifierCalculator.calculateModString(selectedCharacter.getCharisma()) + ")");
        speed = Html.fromHtml("<b>Speed </b>" + selectedCharacter.getSpeed());
        senses = Html.fromHtml("<b>Senses </b>" + selectedCharacter.getSenses());

        if (selectedCharacter.getLanguages() == null || selectedCharacter.getLanguages().isEmpty()) {
            languages = Html.fromHtml("<b>Languages</b> -");
        } else {
            languages = Html.fromHtml("<b>Languages</b> " + selectedCharacter.getLanguages());
        }

        //Damage Vulnerabilities
        if (selectedCharacter.getDmgVul() != null && !selectedCharacter.getDmgVul().isEmpty()) {
            dmgVul = Html.fromHtml("<b>Damage Vulnerabilities</b> " + selectedCharacter.getDmgVul());
        } else {
            tvDmgVul.setVisibility(View.GONE);
        }

        //Damage Resistances
        if (selectedCharacter.getDmgRes() != null && !selectedCharacter.getDmgRes().isEmpty()) {
            dmgRes = Html.fromHtml("<b>Damage Resistances</b> " + selectedCharacter.getDmgRes());
        } else {
            tvDmgRes.setVisibility(View.GONE);
        }

        //Damage Immunities
        if (selectedCharacter.getDmgIm() != null && !selectedCharacter.getDmgIm().isEmpty()) {
            dmgIm = Html.fromHtml("<b>Damage Immunities</b> " + selectedCharacter.getDmgIm());
        } else {
            tvDmgIm.setVisibility(View.GONE);
        }

        //Condition Immunities
        if (selectedCharacter.getConIm() != null && !selectedCharacter.getConIm().isEmpty()) {
            conIm = Html.fromHtml("<b>Condition Immunities</b> " + selectedCharacter.getConIm());
        } else {
            tvConIm.setVisibility(View.GONE);
        }


        //Innate Spellcasting & Spellcasting
        Spanned innate = null;
        String tempInnate = selectedCharacter.getIndescription();
        String tempInnateSpellsKnown = selectedCharacter.getInspellstring();
        if(tempInnate != null){
            innate = Html.fromHtml("<b>Innate Spellcasting. </b>" + tempInnate);
            if(tempInnateSpellsKnown != null){
                innate = (Spanned) TextUtils.concat(innate, Html.fromHtml("<br/>" + tempInnateSpellsKnown));
            }
            tvInnate.setText(Utils.trimTrailingWhitespace(innate));
        }else{
            tvInnate.setVisibility(View.GONE);
            vLine5.setVisibility(View.GONE);
        }

        Spanned spellcasting = null;
        String tempSc = selectedCharacter.getScdescription();
        String tempScSpellsKnown = selectedCharacter.getScspellstring();
        if(tempSc != null){
            spellcasting = Html.fromHtml("<b>Spellcasting. </b>" + tempSc);
            if(tempScSpellsKnown != null){
                spellcasting = (Spanned) TextUtils.concat(spellcasting, Html.fromHtml("<br/>" + tempScSpellsKnown));
            }
            vLine5.setVisibility(View.VISIBLE);
            tvSpellcasting.setText(Utils.trimTrailingWhitespace(spellcasting));
        }else{
            tvSpellcasting.setVisibility(View.GONE);
        }


        //Feats
        if(selectedCharacter.getAbility1name() != null && !selectedCharacter.getAbility1name().isEmpty()){
            ability1 = Html.fromHtml("<b>" + selectedCharacter.getAbility1name() + "</b>" + ". " + selectedCharacter.getAbility1desc());
            vLine5.setVisibility(View.VISIBLE);
        }else{
            tvAbility1.setVisibility(View.GONE);
        }
        if(selectedCharacter.getAbility2name() != null && !selectedCharacter.getAbility2name().isEmpty()){
            ability2 = Html.fromHtml("<b>" + selectedCharacter.getAbility2name() + "</b>" + ". " + selectedCharacter.getAbility2desc());
        }else{
            tvAbility2.setVisibility(View.GONE);
        }
        if(selectedCharacter.getAbility3name() != null && !selectedCharacter.getAbility3name().isEmpty()){
            ability3 = Html.fromHtml("<b>" + selectedCharacter.getAbility3name() + "</b>" + ". " + selectedCharacter.getAbility3desc());
        }else{
            tvAbility3.setVisibility(View.GONE);
        }
        if(selectedCharacter.getAbility4name() != null && !selectedCharacter.getAbility4name().isEmpty()){
            ability4 = Html.fromHtml("<b>" + selectedCharacter.getAbility4name() + "</b>" + ". " + selectedCharacter.getAbility4desc());
        }else{
            tvAbility4.setVisibility(View.GONE);
        }
        if(selectedCharacter.getAbility5name() != null && !selectedCharacter.getAbility5name().isEmpty()){
            ability5 = Html.fromHtml("<b>" + selectedCharacter.getAbility5name() + "</b>" + ". " + selectedCharacter.getAbility5desc());
        }else{
            tvAbility5.setVisibility(View.GONE);
        }


        Spanned myReaction1 = null, myReaction2 = null, myReaction3 = null, myReaction4 = null, myReaction5 = null;
        String tempReaction = selectedCharacter.getReaction1name();
        if(tempReaction != null && !tempReaction.isEmpty()){
            myReaction1 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + selectedCharacter.getReaction1desc());
            vLine6.setVisibility(View.VISIBLE);
            tvReactions.setVisibility(View.VISIBLE);
        }else{
            if(ability1 == null){
                vLine5.setVisibility(View.GONE);
            }
            tvReaction1.setVisibility(View.GONE);
            vLine6.setVisibility(View.GONE);
            tvReactions.setVisibility(View.GONE);
        }
        tempReaction = selectedCharacter.getReaction2name();
        if(tempReaction != null && !tempReaction.isEmpty()){
            myReaction2 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + selectedCharacter.getReaction2desc());
        }else{
            tvReaction2.setVisibility(View.GONE);
        }
        tempReaction = selectedCharacter.getReaction3name();
        if(tempReaction != null && !tempReaction.isEmpty()){
            myReaction3 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + selectedCharacter.getReaction3desc());
        }else{
            tvReaction3.setVisibility(View.GONE);
        }
        tempReaction = selectedCharacter.getReaction4name();
        if(tempReaction != null && !tempReaction.isEmpty()){
            myReaction4 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + selectedCharacter.getReaction4desc());
        }else{
            tvReaction4.setVisibility(View.GONE);
        }
        tempReaction = selectedCharacter.getReaction5name();
        if(tempReaction != null && !tempReaction.isEmpty()){
            myReaction5 = Html.fromHtml("<b>" + tempReaction + "</b>" + ". " + selectedCharacter.getReaction5desc());
        }else{
            tvReaction5.setVisibility(View.GONE);
        }



        name = selectedCharacter.getName();
        iconUri = selectedCharacter.getIconUri();


        tvHpCurrent.setText(Html.fromHtml("<b>HP</b> " + String.valueOf(hpCurrent)));
        tvHpMax.setText(" / " + String.valueOf(hpMax));
        tvAc.setText(ac);
        tvName.setText(name);
        tvType.setText(monstertype);
        ivIcon.setImageURI(iconUri);
        //tvType.setText(type);
        tvStr.setText(String.valueOf(str));
        tvDex.setText(String.valueOf(dex));
        tvCon.setText(String.valueOf(con));
        tvInt.setText(String.valueOf(intel));
        tvWis.setText(String.valueOf(wis));
        tvCha.setText(String.valueOf(cha));
        tvSpeed.setText(speed);
        tvSenses.setText(senses);
        tvLanguages.setText(languages);
        tvDmgVul.setText(dmgVul);
        tvDmgRes.setText(dmgRes);
        tvDmgIm.setText(dmgIm);
        tvConIm.setText(conIm);
        tvAbility1.setText(ability1);
        tvAbility2.setText(ability2);
        tvAbility3.setText(ability3);
        tvAbility4.setText(ability4);
        tvAbility5.setText(ability5);
        tvReaction1.setText(myReaction1);
        tvReaction2.setText(myReaction2);
        tvReaction3.setText(myReaction3);
        tvReaction4.setText(myReaction4);
        tvReaction5.setText(myReaction5);


        if (type == 1) {
            vFrame.setBackgroundColor(Color.parseColor("#4caf50"));
        } else if (type == 0) {
            vFrame.setBackgroundColor(Color.parseColor("#F44336"));
        } else if (type == 2) {
            vFrame.setBackgroundColor(Color.parseColor("#3F51B5"));
        } else {
            vFrame.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        bDamage = (ImageButton) view.findViewById(R.id.buttonDamage);
        bDamage.setColorFilter(Color.parseColor("#ffffffff"));
        bHealing = (ImageButton) view.findViewById(R.id.buttonHealing);
        bHealing.setColorFilter(Color.parseColor("#ffffffff"));
        bST = (ImageButton) view.findViewById(R.id.buttonST);
        //bST.setColorFilter(Color.parseColor("#ffffffff"));

        bDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npbDamage = new NumberPickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setLabelText("Damage")
                        .setDecimalVisibility(View.INVISIBLE)
                        .setPlusMinusVisibility(View.INVISIBLE)
                        .setReference(1)
                        .setTargetFragment(HpOverviewFragment.this);
                npbDamage.show();
            }
        });

        bHealing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npbHealing = new NumberPickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setLabelText("Healing")
                        .setDecimalVisibility(View.VISIBLE)
                        .setPlusMinusVisibility(View.INVISIBLE)
                        .setReference(2)
                        .setTargetFragment(HpOverviewFragment.this);
                npbHealing.show();
            }
        });

        bST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showViewPager();
            }
        });

        return view;
    }

    public void showViewPager() {

        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        final Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("number_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final SavingThrowDialogFragment fragment = SavingThrowDialogFragment
                .newInstance(selectedCharacter.getStrength(), selectedCharacter.getDexterity(), selectedCharacter.getConstitution(), selectedCharacter.getIntelligence(), selectedCharacter.getWisdom(), selectedCharacter.getCharisma(), selectedCharacter.getStStr(), selectedCharacter.getStDex(), selectedCharacter.getStCon(), selectedCharacter.getStInt(), selectedCharacter.getStWis(), selectedCharacter.getStCha());
        //fragment.setNumberPickerDialogHandlers(mNumberPickerDialogHandlers);
        //fragment.setNumberPickerDialogHandlersV2(mNumberPickerDialogHandlersV2);
        fragment.show(ft, "number_dialog");

    }

    /*
    public void saveChanges() {
        ((TrackerFragment) getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().setHp(id, hpDiff, isTemp, isHeal);
    } */

    public void setHp(int hpDiff, boolean temporary, boolean isHeal) {
        if (temporary) {
            selectedCharacter.setTempHp(hpDiff);
            selectedCharacter.setHp(selectedCharacter.getHp() + hpDiff);
        } else {
            if (isHeal) {
                selectedCharacter.setHp(selectedCharacter.getHp() + hpDiff);
                if (selectedCharacter.getHp() > (selectedCharacter.getMaxHp() + selectedCharacter.getTempHp())) {
                    selectedCharacter.setHp(selectedCharacter.getMaxHp() + selectedCharacter.getTempHp());
                }
            } else {
                if (selectedCharacter.getTempHp() > 0) {
                    selectedCharacter.setTempHp(selectedCharacter.getTempHp() - hpDiff);
                    selectedCharacter.setHp(selectedCharacter.getHp() - hpDiff);
                    if (selectedCharacter.getTempHp() < 0) {
                        selectedCharacter.setTempHp(0);
                    }
                } else {
                    selectedCharacter.setHp(selectedCharacter.getHp() - hpDiff);
                }
            }
        }

        if (selectedCharacter.getHp() == 0) {
            selectedCharacter.setDead(true);
        } else if (selectedCharacter.getHp() < 0) {
            selectedCharacter.setDead(true);
            selectedCharacter.setHp(0);
        }
        tvHpCurrent.setText(Html.fromHtml("<b>HP</b> " + String.valueOf(selectedCharacter.getHp())));
        ((TrackerFragment) getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().notifyDataSetChanged();
    }



    @Override
    public void onDialogNumberSet(int reference, BigInteger number, boolean temporary, boolean isNegative, BigDecimal fullNumber) {
        hpDiff = number.intValue();
        if(reference == 1){
            isHeal = false;
        }else if(reference == 2){
            isHeal = true;
        }
        isTemp = temporary;
        setHp(hpDiff, isTemp, isHeal);
        //tvDamage.setText(String.valueOf(Math.abs(number.intValue())));
        /*if (isNegative) {
            if (temporary) {
                tvIdentifier.setText("Temp. Hp");
            } else {
                tvIdentifier.setText("Heal");
            }
        } else {
            tvIdentifier.setText("Damage");
        }*/
    }
}