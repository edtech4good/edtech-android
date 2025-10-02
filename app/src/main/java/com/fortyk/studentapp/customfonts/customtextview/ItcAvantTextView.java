package com.fortyk.studentapp.customfonts.customtextview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class ItcAvantTextView extends AppCompatTextView {

    public ItcAvantTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ItcAvantTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItcAvantTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "itcavantdermi.TTF");
            setTypeface(tf);
        }
    }
}