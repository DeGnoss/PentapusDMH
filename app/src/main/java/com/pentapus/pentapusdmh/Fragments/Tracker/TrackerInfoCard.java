package com.pentapus.pentapusdmh.Fragments.Tracker;

import android.net.Uri;

/**
 * Created by Koni on 30/3/16.
 */
public class TrackerInfoCard {
    public String name;
    public int enteredInitiative;
    public String initiative;
    public String initiativeMod;
    public int maxHp;
    public int tempHp;
    public int ac;
    public int type;
    public int hp;
    public int strength = 0, dexterity = 0, constitution = 0, intelligence = 0, wisdom = 0, charisma = 0;
    public boolean[] statuses = new boolean[15];
    public boolean dead;
    public boolean selected;
    public Uri iconUri;

    public String monsterType, acType, size, speed, source, multiattack, senses, alignment, languages, dmgRes, dmgIm, dmgVul, conIm;
    public String atk1name, atk1desc, atk1dmg1roll, atk1dmg1type, atk1dmg2roll, atk1dmg2type;
    public String atk2name, atk2desc, atk2dmg1roll, atk2dmg1type, atk2dmg2roll, atk2dmg2type;
    public String atk3name, atk3desc, atk3dmg1roll, atk3dmg1type, atk3dmg2roll, atk3dmg2type;
    public String atk4name, atk4desc, atk4dmg1roll, atk4dmg1type, atk4dmg2roll, atk4dmg2type;
    public String reaction1name, reaction1desc;
    public String ability1name, ability1desc, ability2name, ability2desc,ability3name, ability3desc,ability4name, ability4desc,ability5name, ability5desc;

    public int xp, sourcepage;
    public int atk1mod, atk1autoroll, atk1additional;
    public int atk2mod, atk2autoroll, atk2additional;
    public int atk3mod, atk3autoroll, atk3additional;
    public int atk4mod, atk4autoroll, atk4additional;
    public int acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival;
    public int stStr, stDex, stCon, stInt, stWis, stCha;



    protected static final String NAME_PREFIX = "Name_";
    protected static final String INITIATIVE_PREFIX = "Initiative_";


    public int getTempHp() {
        return tempHp;
    }

    public void setTempHp(int tempHp) {
        this.tempHp = tempHp;
    }

    public int getType() {
        return type;
    }

    public String getInitiativeMod() {
        return initiativeMod;
    }

    public int getEnteredInitiative() {
        return enteredInitiative;
    }

    public void setEnteredInitiative(int enteredInitiative) {
        this.enteredInitiative = enteredInitiative;
    }

    public String getMonsterType() {
        return monsterType;
    }

    public String getInitiative() {
        return initiative;
    }

    public void setInitiative(String initiative) {
        this.initiative = initiative;
    }

    public Uri getIconUri() {
        return iconUri;
    }

    public void setIconUri(Uri iconUri) {
        this.iconUri = iconUri;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public String getName() {
        return name;
    }

    public void setStatuses(boolean[] statuses){
        this.statuses = statuses;
    }

    public boolean[] getStatuses(){
        return statuses;
    }


    public boolean isBlinded() {
        return statuses[0];
    }

    public boolean isCharmed() {
        return statuses[1];
    }


    public boolean isDeafened() {
        return statuses[2];
    }


    public boolean isFrightened() {
        return statuses[3];
    }


    public boolean isGrappled() {
        return statuses[4];
    }


    public boolean isIncapacitated() {
        return statuses[5];
    }


    public boolean isInvisible() {
        return statuses[6];
    }


    public boolean isParalyzed() {
        return statuses[7];
    }


    public boolean isPetrified() {
        return statuses[8];
    }


    public boolean isPoisoned() {
        return statuses[9];
    }


    public boolean isProne() {
        return statuses[10];
    }


    public boolean isRestrained() {
        return statuses[11];
    }


    public boolean isStunned() {
        return statuses[12];
    }


    public boolean isUnconscious() {
        return statuses[13];
    }


    public boolean isCustom() {
        return statuses[14];
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean selected) {
        this.dead = dead;
    }


    public int getAcrobatics() {
        return acrobatics;
    }

    public int getAnimalhandling() {
        return animalhandling;
    }

    public int getArcana() {
        return arcana;
    }

    public int getAthletics() {
        return athletics;
    }

    public int getDeception() {
        return deception;
    }

    public int getHistory() {
        return history;
    }

    public int getInsight() {
        return insight;
    }

    public int getIntimidation() {
        return intimidation;
    }

    public int getInvestigation() {
        return investigation;
    }

    public int getMedicine() {
        return medicine;
    }

    public int getNature() {
        return nature;
    }

    public int getPerception() {
        return perception;
    }

    public int getPerformance() {
        return performance;
    }

    public int getPersuasion() {
        return persuasion;
    }

    public int getReligion() {
        return religion;
    }

    public int getSleightofhand() {
        return sleightofhand;
    }

    public int getStealth() {
        return stealth;
    }

    public int getSurvival() {
        return survival;
    }


    public String getAcType() {
        return acType;
    }

    public String getSize() {
        return size;
    }

    public String getSpeed() {
        return speed;
    }

    public String getSource() {
        return source;
    }

    public int getSourcepage() {
        return sourcepage;
    }

    public String getMultiattack() {
        return multiattack;
    }

    public String getSenses() {
        return senses;
    }

    public String getAlignment() {
        return alignment;
    }

    public String getLanguages() {
        return languages;
    }

    public String getDmgRes() {
        return dmgRes;
    }

    public String getDmgIm() {
        return dmgIm;
    }

    public String getDmgVul() {
        return dmgVul;
    }

    public String getConIm() {
        return conIm;
    }

    public String getAtk1name() {
        return atk1name;
    }

    public String getAtk1desc() {
        return atk1desc;
    }

    public String getAtk1dmg1roll() {
        return atk1dmg1roll;
    }

    public String getAtk1dmg1type() {
        return atk1dmg1type;
    }

    public String getAtk1dmg2roll() {
        return atk1dmg2roll;
    }

    public String getAtk1dmg2type() {
        return atk1dmg2type;
    }

    public String getAtk2name() {
        return atk2name;
    }

    public String getAtk2desc() {
        return atk2desc;
    }

    public String getAtk2dmg1roll() {
        return atk2dmg1roll;
    }

    public String getAtk2dmg1type() {
        return atk2dmg1type;
    }

    public String getAtk2dmg2roll() {
        return atk2dmg2roll;
    }

    public String getAtk2dmg2type() {
        return atk2dmg2type;
    }

    public String getAtk3name() {
        return atk3name;
    }

    public String getAtk3desc() {
        return atk3desc;
    }

    public String getAtk3dmg1roll() {
        return atk3dmg1roll;
    }

    public String getAtk3dmg1type() {
        return atk3dmg1type;
    }

    public String getAtk3dmg2roll() {
        return atk3dmg2roll;
    }

    public String getAtk3dmg2type() {
        return atk3dmg2type;
    }

    public String getAtk4name() {
        return atk4name;
    }

    public String getAtk4desc() {
        return atk4desc;
    }

    public String getAtk4dmg1roll() {
        return atk4dmg1roll;
    }

    public String getAtk4dmg1type() {
        return atk4dmg1type;
    }

    public String getAtk4dmg2roll() {
        return atk4dmg2roll;
    }

    public String getAtk4dmg2type() {
        return atk4dmg2type;
    }

    public String getReaction1name() {
        return reaction1name;
    }

    public String getReaction1desc() {
        return reaction1desc;
    }

    public String getAbility1name() {
        return ability1name;
    }

    public String getAbility1desc() {
        return ability1desc;
    }

    public String getAbility2name() {
        return ability2name;
    }

    public String getAbility2desc() {
        return ability2desc;
    }

    public String getAbility3name() {
        return ability3name;
    }

    public String getAbility3desc() {
        return ability3desc;
    }

    public String getAbility4name() {
        return ability4name;
    }

    public String getAbility4desc() {
        return ability4desc;
    }

    public String getAbility5name() {
        return ability5name;
    }

    public String getAbility5desc() {
        return ability5desc;
    }

    public int getXp() {
        return xp;
    }

    public int getAtk1mod() {
        return atk1mod;
    }

    public int getAtk1autoroll() {
        return atk1autoroll;
    }

    public int getAtk1additional() {
        return atk1additional;
    }

    public int getAtk2mod() {
        return atk2mod;
    }

    public int getAtk2autoroll() {
        return atk2autoroll;
    }

    public int getAtk2additional() {
        return atk2additional;
    }

    public int getAtk3mod() {
        return atk3mod;
    }

    public int getAtk3autoroll() {
        return atk3autoroll;
    }

    public int getAtk3additional() {
        return atk3additional;
    }

    public int getAtk4mod() {
        return atk4mod;
    }

    public int getAtk4autoroll() {
        return atk4autoroll;
    }

    public int getAtk4additional() {
        return atk4additional;
    }

    public int getStStr() {
        return stStr;
    }

    public int getStDex() {
        return stDex;
    }

    public int getStCon() {
        return stCon;
    }

    public int getStInt() {
        return stInt;
    }

    public int getStWis() {
        return stWis;
    }

    public int getStCha() {
        return stCha;
    }
}
