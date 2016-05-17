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
}
