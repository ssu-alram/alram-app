package com.example.ticker.core.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class Ticker extends View {
    public Ticker(Context context) {
        this(context, null);
    }

    public Ticker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ticker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
