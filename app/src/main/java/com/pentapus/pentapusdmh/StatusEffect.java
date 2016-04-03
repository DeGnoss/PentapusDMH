package com.pentapus.pentapusdmh;

/**
 * Created by Koni on 03.04.2016.
 */
public class StatusEffect {
    public String name;
    public int imageId;
    private boolean selected;

    public StatusEffect(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
    public void toggleSelected() {
        selected = !selected;
    }
}
