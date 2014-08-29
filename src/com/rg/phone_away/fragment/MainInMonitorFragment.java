package com.rg.phone_away.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.rg.phone_away.MainActivity;
import com.rg.phone_away.MonitorService;
import com.rg.phone_away.MyApp;
import com.rg.phone_away.PunishActivity;
import com.rg.phone_away.R;
import com.rg.phone_away.widget.MainTimeLayout;
import com.welljun.CircularSeekBar;

public class MainInMonitorFragment extends Fragment {
    public View view;

    MainActivity mainActivity;

    MyApp myApp;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
        myApp = mainActivity.myApp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_main_in_monitor, container, false);

        mainActivity.remainTimeLayout = (MainTimeLayout)view.findViewById(R.id.main_time_layout);

        CircularSeekBar circularSeekBar = (CircularSeekBar)view.findViewById(R.id.timeSeekBar);
        circularSeekBar.setEnabled(false);

        view.findViewById(R.id.stop).setOnTouchListener(new OnTouchListener() {

            float lastX;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() > lastX + view.getWidth() / 4 && event.getY() > 0
                            && event.getY() < view.getHeight()) {
                        stopTask();
                    }
                }
                return true;
            }
        });

        return view;
    }

    void stopTask() {
        mainActivity.editor.putBoolean("isInMonitor", false).commit();
        Intent intent = new Intent(mainActivity, MonitorService.class);
        mainActivity.stopService(intent);
        MyApp.punish();
        startActivity(new Intent(mainActivity, PunishActivity.class));
        myApp.state = (MyApp.NOTINMONITOR);

        mainActivity.setContentView();
    }

}
