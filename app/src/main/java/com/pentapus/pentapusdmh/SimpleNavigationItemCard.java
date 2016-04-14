package com.pentapus.pentapusdmh;

import android.database.Cursor;
import android.net.Uri;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;

/**
 * Created by Koni on 30/3/16.
 */
public class SimpleNavigationItemCard {
    public String name;
    public String info;
    public int rowId;
    public boolean selected;
    protected static final String NAME_PREFIX = "Name_";
    protected static final String INITIATIVE_PREFIX = "Initiative_";



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getRowId() {
        return rowId;
    }

    public static SimpleNavigationItemCard fromCursor(Cursor cursor) {
        SimpleNavigationItemCard simpleItemCard = new SimpleNavigationItemCard();
        simpleItemCard.name = cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_NAME));
        simpleItemCard.info = cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_INFO));
        simpleItemCard.rowId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        return simpleItemCard;
    }
}
