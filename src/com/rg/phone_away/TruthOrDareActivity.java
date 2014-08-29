package com.rg.phone_away;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class TruthOrDareActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_truth_or_dare);
	
		Button truth_btn = (Button)findViewById(R.id.truth_btn);
		truth_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TruthOrDareActivity.this,TruthActivity.class);
				TruthOrDareActivity.this.startActivity(intent);
			}
		});
		
		Button dare_btn = (Button)findViewById(R.id.dare_btn);
		dare_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TruthOrDareActivity.this,DareActivity.class);
				TruthOrDareActivity.this.startActivity(intent);
			}
		});
	}

	
}
