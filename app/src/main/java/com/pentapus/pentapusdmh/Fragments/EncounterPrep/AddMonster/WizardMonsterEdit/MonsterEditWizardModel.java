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
                new SingleFixedChoicePage(this, "Size")
                        .setChoices("Tiny", "Small", "Medium", "Large", "Huge", "Gargantuan"),
                new AbilitiesPage(this, "Abilities & Hit Points")
                        .setRequired(false),
                new SkillsPage(this, "Skills")
                        .setRequired(false),
                new DmgVulChoicePage(this, "Damage Vulnerabilities")
                .setChoices("Acid", "Cold", "Fire", "Force", "Lightning", "Necrotic", "Poison",  "Psychic", "Radiant", "Thunder"),
                new DmgVulChoicePage(this, "Damage Resistances")
                        .setChoices("Acid", "Cold", "Fire", "Force", "Lightning", "Necrotic", "Poison",  "Psychic", "Radiant", "Thunder", "Bludgeoning, piercing and slashing from nonmagical weapons", "Bludgeoning, piercing and slashing from nonmagical weapons that aren't silvered"),
                new DmgVulChoicePage(this, "Damage Immunities")
                        .setChoices("Acid", "Cold", "Fire", "Force", "Lightning", "Necrotic", "Poison",  "Psychic", "Radiant", "Thunder", "Bludgeoning, piercing and slashing from nonmagical weapons", "Bludgeoning, piercing and slashing from nonmagical weapons that aren't silvered"),
                new DmgVulChoicePage(this, "Condition Immunities")
                        .setChoices("Blinded", "Charmed", "Deafened", "Frightened", "Grappled", "Incapacitated", "Invisible",  "Paralyzed", "Petrified", "Poisoned", "Prone", "Restrained", "Stunned", "Unconscious"),
                new TraitsPage(this, "Traits"),
                new ActionPage(this, "Actions")
        );
    }
}
