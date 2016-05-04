package com.pentapus.pentapusdmh;

import android.database.Cursor;
import android.database.MatrixCursor;

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

}
