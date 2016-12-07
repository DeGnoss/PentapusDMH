package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.HelperClasses.HitDiceCalculator;
import com.wizardpager.wizard.model.ModelCallbacks;
import com.wizardpager.wizard.model.Page;
import com.wizardpager.wizard.model.ReviewItem;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Koni on 11.11.2016.
 */

public class AbilitiesPage extends Page {
    String ac;

    public static final String STR_DATA_KEY = "str";
    public static final String DEX_DATA_KEY = "dex";
    public static final String CON_DATA_KEY = "con";
    public static final String INT_DATA_KEY = "int";
    public static final String WIS_DATA_KEY = "wis";
    public static final String CHA_DATA_KEY = "cha";

    public static final String HP_DATA_KEY = "hp";
    public static final String HITDICE_DATA_KEY = "hitdice";

    public static final String AC1_DATA_KEY = "ac1";
    public static final String AC1TYPE_DATA_KEY = "ac1type";
    public static final String AC2_DATA_KEY = "ac2";
    public static final String AC2TYPE_DATA_KEY = "ac2type";

    public static final String SIZE_DATA_KEY = "size";

    public static final String SPEED_DATA_KEY = "speed";




    public AbilitiesPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return AbilitiesFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
       /* dest.add(new ReviewItem("STR", mData.getString(STR_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("DEX", mData.getString(DEX_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("CON", mData.getString(CON_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("INT", mData.getString(INT_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("WIS", mData.getString(WIS_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("CHA", mData.getString(CHA_DATA_KEY), getKey(), -1));*/

        String ac1 = mData.getString(AC1_DATA_KEY);
        String ac1type = mData.getString(AC1TYPE_DATA_KEY);
        String ac2 = mData.getString(AC2_DATA_KEY);
        String ac2type = mData.getString(AC2TYPE_DATA_KEY);
        if (ac1type == null || ac1type.isEmpty()) {
            ac = String.valueOf(ac1);
        } else {
            ac = ac1 + " (" + ac1type + ") ";
        }
        if (ac2 != null && !ac2.isEmpty()) {
            if(ac2type != null && !ac2type.isEmpty()){
                if(ac == null || ac.isEmpty()){
                    ac = ac2 + " (" + ac2type + ")";
                }else{
                    ac = ac + ", " + ac2 + " (" + ac2type + ")";
                }
            }else{
                if(ac == null || ac.isEmpty()){
                    ac = ac2;
                }else{
                    ac = ac + ", " + ac2;
                }
            }
        }
        dest.add(new ReviewItem("Armor Class", ac, getKey(), -1));


        String hpString;
        String size = mData.getString(AbilitiesPage.SIZE_DATA_KEY);
        String hp = mData.getString(AbilitiesPage.HP_DATA_KEY);
        int hitdice = mData.getInt(AbilitiesPage.HITDICE_DATA_KEY);

        if (hp != null && !hp.isEmpty() && !Objects.equals(hp, "0")) {
            hpString = hp + " (" + hitdice + HitDiceCalculator.calculateHdType(hitdice, size, AbilityModifierCalculator.calculateMod(Integer.valueOf(mData.getString(AbilitiesPage.CON_DATA_KEY)))) + ")";
        } else {
            hpString = "0, click to add";
        }

        dest.add(new ReviewItem("Hit Points", hpString, getKey(), -1));

        dest.add(new ReviewItem("Speed", mData.getString(AbilitiesPage.SPEED_DATA_KEY), getKey(), -1));

    }

    @Override
    public String getAvatarUri() {
        return null;
    }


    @Override
    public boolean isCompleted() {
        return true;
    }
}
