package com.fortyk.studentapp.customfonts.custombutton;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class KgCandyButton extends AppCompatButton {

    public KgCandyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public KgCandyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KgCandyButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "KGCANDYCANESTRIPE.TTF");
            setTypeface(tf);
        }
    }
}
