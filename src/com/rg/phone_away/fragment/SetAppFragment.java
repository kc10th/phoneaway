package com.rg.phone_away.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.rg.phone_away.AboutActivity;
import com.rg.phone_away.MainActivity;
import com.rg.phone_away.R;
import com.rg.phone_away.SteadyRunActivity;
import com.rg.phone_away.WhiteListActivity;

public class SetAppFragment extends Fragment {

	public View view;
	MainActivity mainActivity;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mainActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_set_app, container,
				false);
		view.findViewById(R.id.WhiteList).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(mainActivity,
								WhiteListActivity.class));
					}
				});

		view.findViewById(R.id.steadyRun).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(mainActivity,
								SteadyRunActivity.class));
					}
				});
		view.findViewById(R.id.about).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(mainActivity, AboutActivity.class));
			}
		});

		view.findViewById(R.id.closeApp).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mainActivity.finish();
					}
				});

		return view;
	}
}
