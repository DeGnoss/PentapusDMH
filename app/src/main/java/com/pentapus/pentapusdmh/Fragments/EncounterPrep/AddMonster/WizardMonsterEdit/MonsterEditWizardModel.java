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
                        .setChoices("Tiny", "Small", "Medium", "Large", "Huge", "Gargantuan")
        );
    }
}
