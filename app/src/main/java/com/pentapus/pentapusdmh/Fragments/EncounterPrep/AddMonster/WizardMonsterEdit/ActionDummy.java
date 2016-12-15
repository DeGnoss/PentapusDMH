package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

/**
 * Created by Koni on 09.12.2016.
 */

public class ActionDummy {
    private String name;
    private String desc;
    private String atkmod;
    private String dmg1;
    private String dmg1Type;
    private String dmg2;
    private String dmg2Type;
    private boolean autoroll;
    private boolean additional;

    public ActionDummy(String name, String desc, String atkmod, String dmg1, String dmg1tye, String dmg2, String dmg2type, boolean autoroll, boolean additional){
        this.name = name;
        this.desc = desc;
        this.atkmod = atkmod;
        this.dmg1 = dmg1;
        this.dmg1Type = dmg1tye;
        this.dmg2 = dmg2;
        this.dmg2Type = dmg2type;
        this.autoroll = autoroll;
        this.additional = additional;
    }

    public ActionDummy(String name, String desc, String atkmod, String dmg1, String dmg1tye, String dmg2, String dmg2type, int autoroll, int additional){
        this.name = name;
        this.desc = desc;
        this.atkmod = atkmod;
        this.dmg1 = dmg1;
        this.dmg1Type = dmg1tye;
        this.dmg2 = dmg2;
        this.dmg2Type = dmg2type;
        if(autoroll == 1){
            this.autoroll = true;
        }else{
            this.autoroll = false;
        }

        if(additional == 1){
            this.additional = true;
        }else{
            this.additional = false;
        }
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getAtkmod() {
        return atkmod;
    }

    public String getDmg1() {
        return dmg1;
    }

    public String getDmg1Type() {
        return dmg1Type;
    }

    public String getDmg2() {
        return dmg2;
    }

    public String getDmg2Type() {
        return dmg2Type;
    }

    public boolean isAutoroll() {
        return autoroll;
    }

    public int getAutoRoll(){
        if(isAutoroll()){
            return 1;
        }else{
            return 0;
        }
    }

    public boolean isAdditional() {
        return additional;
    }

    public int getAdditional(){
        if(isAutoroll()){
            return 1;
        }else{
            return 0;
        }
    }
}
