package com.rg.phone_away;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SteadyRunActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_steady_run);
		
		Button back = (Button)findViewById(R.id.back_main_btn);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		//按钮的跳转动作。都是跳到同一个Activity，给intent一个标识值，根据标识值显示不同文字图片
		Button item0_btn = (Button)findViewById(R.id.steady_run_btn0);
		item0_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SteadyRunActivity.this,SRItemActivity.class);
				intent.putExtra("ItemNo", 0);
				SteadyRunActivity.this.startActivity(intent);
			}
		});
		
		Button item1_btn = (Button)findViewById(R.id.steady_run_btn1);
		item1_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SteadyRunActivity.this,SRItemActivity.class);
				intent.putExtra("ItemNo", 1);
				SteadyRunActivity.this.startActivity(intent);
			}
		});
		
		Button item2_btn = (Button)findViewById(R.id.steady_run_btn2);
		item2_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SteadyRunActivity.this,SRItemActivity.class);
				intent.putExtra("ItemNo", 2);
				SteadyRunActivity.this.startActivity(intent);
			}
		});
		
		Button item3_btn = (Button)findViewById(R.id.steady_run_btn3);
		item3_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SteadyRunActivity.this,SRItemActivity.class);
				intent.putExtra("ItemNo", 3);
				SteadyRunActivity.this.startActivity(intent);
			}
		});
		
		Button item4_btn = (Button)findViewById(R.id.steady_run_btn4);
		item4_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SteadyRunActivity.this,SRItemActivity.class);
				intent.putExtra("ItemNo", 4);
				SteadyRunActivity.this.startActivity(intent);
			}
		});
		
		Button item5_btn = (Button)findViewById(R.id.steady_run_btn5);
		item5_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SteadyRunActivity.this,SRItemActivity.class);
				intent.putExtra("ItemNo", 5);
				SteadyRunActivity.this.startActivity(intent);
			}
		});
		
		Button item6_btn = (Button)findViewById(R.id.steady_run_btn6);
		item6_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SteadyRunActivity.this,SRItemActivity.class);
				intent.putExtra("ItemNo", 6);
				SteadyRunActivity.this.startActivity(intent);
			}
		});
		
		Button item7_btn = (Button)findViewById(R.id.steady_run_btn7);
		item7_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SteadyRunActivity.this,SRItemActivity.class);
				intent.putExtra("ItemNo", 7);
				SteadyRunActivity.this.startActivity(intent);
			}
		});
	}
}

