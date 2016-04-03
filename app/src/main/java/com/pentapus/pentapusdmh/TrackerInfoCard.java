package com.pentapus.pentapusdmh;

/**
 * Created by Koni on 30/3/16.
 */
public class TrackerInfoCard {
        public String name;
        public String initiative;
        public String initiativeMod;
        public String maxHp;
        public String ac;
        public String type;
        public boolean dead;
        public boolean selected;
        protected static final String NAME_PREFIX = "Name_";
        protected static final String INITIATIVE_PREFIX = "Initiative_";


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
