package com.rg.phone_away.widget;

import com.rg.phone_away.R;
import com.rg.phone_away.R.id;
import com.rg.phone_away.R.layout;

import net.simonvt.numberpicker.NumberPicker;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

public class SetTimeDialog extends AlertDialog {

	OnSetTimeButtonClickListener onSetTimeButtonClickListener;
	NumberPicker hour;
	NumberPicker minute;

	public interface OnSetTimeButtonClickListener {
		public void onClick(int hour, int minute);
	}

	public SetTimeDialog(Context context, int nowHour, int nowMinute,OnSetTimeButtonClickListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.onSetTimeButtonClickListener = listener;
		setDialogContent();
		hour.setValue(nowHour);
		minute.setValue(nowMinute);
		
	}

	void setDialogContent() {

		setIcon(0);
		setTitle("设置时长");
		setButton(BUTTON_POSITIVE, "Set", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == BUTTON_POSITIVE) {
					hour.clearFocus();
					minute.clearFocus();
					onSetTimeButtonClickListener.onClick(hour.getValue(), minute.getValue());
				}
			}
		});
		setButton(BUTTON_NEGATIVE, "Cancel", (OnClickListener) null);

		Context themeContext = getContext();
		LayoutInflater inflater = (LayoutInflater) themeContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.dialog_time_picker, null);
		setView(view);

		hour = (NumberPicker) view.findViewById(R.id.hour);
		minute = (NumberPicker) view.findViewById(R.id.minute);
		
		hour.setFocusable(true);
		hour.setFocusableInTouchMode(true);
		minute.setFocusable(true);
		minute.setFocusableInTouchMode(true);

		hour.setMaxValue(3);
		hour.setMinValue(0);


		minute.setMaxValue(59);
		minute.setMinValue(0);

	}
}
