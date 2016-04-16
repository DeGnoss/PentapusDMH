package com.pentapus.pentapusdmh;

import android.database.Cursor;
import android.net.Uri;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;

/**
 * Created by Koni on 30/3/16.
 */
public class SimpleItemCard {
    public int id;
    public String name;
    public String info;
    public int type;
    public boolean selected;
    public Uri iconUri;
    protected static final String NAME_PREFIX = "Name_";
    protected static final String INITIATIVE_PREFIX = "Initiative_";
    String identifier;


    public int getType() {
        return type;
    }

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

    public Uri getIconUri() {
        return iconUri;
    }

    public void setIconUri(Uri iconUri) {
        this.iconUri = iconUri;
    }


    public static SimpleItemCard fromCursor(Cursor cursor) {
        Uri uri = Uri.parse("android.resource://com.pentapus.pentapusdmh/drawable/ninja");
        SimpleItemCard simpleItemCard = new SimpleItemCard();
        simpleItemCard.id = cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_ROWID));
        simpleItemCard.name = cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_NAME));
        simpleItemCard.info = cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_INFO));
        simpleItemCard.type = cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_TYPE));
        if(!cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)).isEmpty()){
            simpleItemCard.iconUri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
        }else{
            simpleItemCard.iconUri = uri;
        }
        return simpleItemCard;
    }
}
