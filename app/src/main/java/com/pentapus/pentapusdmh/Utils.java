package com.pentapus.pentapusdmh;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Rect;
import android.view.Window;

/**
 * Created by konrad.fellmann on 04.05.2016.
 */
public class Utils {

    public static MatrixCursor matrixCursorFromCursors(Cursor cursor1, Cursor cursor2) {
        if (cursor1 == null && cursor2 == null) {
            return null;
        }

        String[] columnNames = cursor1.getColumnNames();
        if (columnNames == null) {
            columnNames = new String[] {};
        }
        MatrixCursor joinedCursor = new MatrixCursor(columnNames);
        int numColumns = cursor1.getColumnCount();
        String data[] = new String[numColumns];
        cursor1.moveToPosition(-1);
        while (cursor1.moveToNext()) {
            for (int i = 0; i < numColumns; i++) {
                data[i] = cursor1.getString(i);
            }
            joinedCursor.addRow(data);
        }
        cursor2.moveToPosition(-1);
        while (cursor2.moveToNext()) {
            for (int i = 0; i < numColumns; i++) {
                data[i] = cursor2.getString(i);
            }
            joinedCursor.addRow(data);
        }
        return joinedCursor;
    }


    public static int getStatusBarHeight(Activity activity){
        Rect rectangle= new Rect();
        Window window= activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight= rectangle.top;
        int contentViewTop=
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight= contentViewTop - statusBarHeight;
        return statusBarHeight;
    }

}
