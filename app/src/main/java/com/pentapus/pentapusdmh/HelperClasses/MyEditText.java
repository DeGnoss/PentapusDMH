package com.pentapus.pentapusdmh.HelperClasses;

/**
 * Created by Koni on 20.12.2016.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class MyEditText extends EditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context) {
        super(context);
    }

    @Override
    public View focusSearch(int direction) {
        View v = super.focusSearch(direction);
        if (v != null) {
            if (v.isEnabled()) {
                return v;
            } else {
                // keep searching
                return v.focusSearch(direction);
            }
        }
        return v;
    }

}
