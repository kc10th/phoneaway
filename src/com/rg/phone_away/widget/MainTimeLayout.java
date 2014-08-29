package com.rg.phone_away.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rg.phone_away.R;
import com.welljun.CircularSeekBar;

/**
 * @author: YangJun
 * @date: 2014 2014-8-6 下午8:51:27
 */
public class MainTimeLayout extends RelativeLayout {

    /**
     * @param context
     * @param attrs
     */
    public MainTimeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private TextView hourTextView;

    private TextView minuteTextView;

    private TextView secondTextView;

    private CircularSeekBar circularSeekBar;

    private boolean uninitialized = true;

    public void init() {
        hourTextView = (TextView)this.findViewById(R.id.hourTextView);
        minuteTextView = (TextView)this.findViewById(R.id.minuteTextView);
        secondTextView = (TextView)this.findViewById(R.id.secondTextView);

        Typeface fontFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/btseps2.TTF");
        hourTextView.setTypeface(fontFace);
        minuteTextView.setTypeface(fontFace);
        if (secondTextView != null) {
            secondTextView.setTypeface(fontFace);
        }
        circularSeekBar = (CircularSeekBar)this.findViewById(R.id.timeSeekBar);
        circularSeekBar.setStandardValue(60);
        circularSeekBar.setMaxValue(4 * 60);

    }

    public void setTime(int hour, int minute) {
        setTime(hour, minute, 0);
    }

    public void setTime(int hour, int minute, int second) {
        if (uninitialized) {
            init();
        }
        hourTextView.setText(hour + "h");
        minuteTextView.setText((minute < 10 ? "0" : "") + minute + "min");
        if (secondTextView != null) {
            secondTextView.setText(second + "s");
        }
        circularSeekBar.setValue(hour * 60 + minute);
    }

}
