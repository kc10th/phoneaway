package com.rg.phone_away;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LockActivity extends Activity {

	Intent homeIntent;
	ImageView imageView = null;
	TextView textView = null;
	MyApp app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (MyApp) getApplication();
		if (app.Pattern.equals(app.PatternStrings[2])) {
			setContentView(R.layout.activity_lock_customor);
			textView = (TextView) findViewById(R.id.lock_word);
			textView.setText(app.mainActivity.AppIni.getString(
					MyApp.CustomerKeyStrings[0], ""));
		} else {
			setContentView(R.layout.activity_lock);
			imageView = (ImageView) findViewById(R.id.lock_word);
			imageView.setImageBitmap(Constants.getLockPicture(app));

		}

		homeIntent = new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);

		findViewById(R.id.home).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(homeIntent);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			startActivity(homeIntent);
		}
		return super.onKeyDown(keyCode, event);

	}

}
