package com.rg.phone_away.widget;

import com.rg.phone_away.R;
import com.rg.phone_away.R.drawable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class HoursNumberView extends ImageView {

	public HoursNumberView(Context context) {
		super(context);
	}

	public HoursNumberView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public HoursNumberView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setNumber(int n) {
		this.setImageResource(getDrawable(n));
	}

	public int getDrawable(int n) {
		switch (n) {
		case 1:
			return R.drawable.number_1;
		case 2:
			return R.drawable.number_2;
		case 3:
			return R.drawable.number_3;
		case 4:
			return R.drawable.number_4;
		case 5:
			return R.drawable.number_5;
		case 6:
			return R.drawable.number_6;
		case 7:
			return R.drawable.number_7;
		case 8:
			return R.drawable.number_8;
		case 9:
			return R.drawable.number_9;
		default:
			return R.drawable.number_0;
		}
	}

}
