package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.content.Context;

import com.wizardpager.wizard.model.AbstractWizardModel;
import com.wizardpager.wizard.model.BranchPage;
import com.wizardpager.wizard.model.CustomerInfoPage;
import com.wizardpager.wizard.model.PageList;
import com.wizardpager.wizard.model.SingleFixedChoicePage;

/**
 * Created by Koni on 11.11.2016.
 */

public class MonsterEditWizardModel extends AbstractWizardModel {

    public MonsterEditWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new BasicInfoPage(this, "Basic Info")
                        .setRequired(true),
                /*new SingleFixedChoicePage(this, "Size")
                        .setChoices("Tiny", "Small", "Medium", "Large", "Huge", "Gargantuan"),*/
                new AbilitiesPage(this, "Abilities & Hit Points")
                        .setRequired(false),
                new SkillsPage(this, "Additional Info")
                        .setRequired(false),
                new TraitsPage(this, "Traits"),
                new ActionPage(this, "Actions"),
                new ReactionPage(this, "Reactions"),
                new LegendaryActionPage(this, "Legendary Actions"),
                new CrPage(this, "CR & Spellcasting")
        );
    }
}
