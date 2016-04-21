package com.pentapus.pentapusdmh;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by konrad.fellmann on 21.04.2016.
 */
public class MyQueryHandler extends AsyncQueryHandler {
    public MyQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        // query() completed
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        // insert() completed
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        // update() completed
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        // delete() completed
    }
}
