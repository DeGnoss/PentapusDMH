package com.pentapus.pentapusdmh.HelperClasses;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

/**
 * Created by Koni on 25.11.2016.
 */

public class CustomMultiAutoCompleteTextView extends MultiAutoCompleteTextView {

    public CustomMultiAutoCompleteTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    // this is how to disable AutoCompleteTextView filter
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
       // String filterText = "";
       // super.performFiltering(filterText, keyCode);
    }

    /*
     * after a selection we have to capture the new value and append to the existing text
     */
    @Override
    protected void replaceText(final CharSequence text) {
        super.replaceText(text);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            performFiltering(getText(), 0);
        }
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }
}
