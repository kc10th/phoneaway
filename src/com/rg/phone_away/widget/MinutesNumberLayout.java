package com.rg.phone_away.widget;

import com.rg.phone_away.R;
import com.rg.phone_away.R.drawable;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MinutesNumberLayout extends LinearLayout {

	int unitsDigit = 0;
	int tensDigit = 0;
	Context context;

	ImageView unitsDigitView;
	ImageView tensDigitView;

	public MinutesNumberLayout(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public MinutesNumberLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);

		// TODO Auto-generated constructor stub
	}

	public MinutesNumberLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

		// TODO Auto-generated constructor stub
	}

	void init(Context c) {
		context = c;
		this.setOrientation(HORIZONTAL);
		tensDigitView = new ImageView(context);
		addView(tensDigitView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		unitsDigitView = new ImageView(context);
		addView(unitsDigitView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		setNumber(0);
	}

	public void setNumber(int num) {
		unitsDigit = num % 10;
		tensDigit = num / 10;
		tensDigitView.setImageResource(getDrawable(tensDigit));
		unitsDigitView.setImageResource(getDrawable(unitsDigit));
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
