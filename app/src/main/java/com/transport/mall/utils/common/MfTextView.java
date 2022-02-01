package com.transport.mall.utils.common;

import android.content.Context;
import android.util.AttributeSet;

public class MfTextView extends androidx.appcompat.widget.AppCompatTextView {

    public MfTextView(Context context) {
        super(context);
    }


    public MfTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        CustomFontHelper.setCustomFont(this, context, attrs);

    }

    public MfTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        CustomFontHelper.setCustomFont(this, context, attrs);

    }


}