package com.rg.phone_away.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rg.phone_away.MainActivity;
import com.rg.phone_away.MonitorService;
import com.rg.phone_away.MyApp;
import com.rg.phone_away.R;
import com.rg.phone_away.widget.MainTimeLayout;
import com.welljun.CircularSeekBar;
import com.welljun.CircularSeekBar.OnSeekChangeListener;

public class MainFragment extends Fragment implements OnItemSelectedListener {

    public View view;

    private ArrayAdapter<String> patternAdapter;

    private ArrayAdapter<String> sceneAdapter;

    List<String> patternList = new ArrayList<String>();

    List<String> sceneList = new ArrayList<String>();

    MyApp myApp;

    Spinner patternSpinner;

    Spinner sceneSpinner;

    MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
        myApp = mainActivity.myApp;
    }

    TextView hourTextView;

    TextView minuteTextView;

    MainTimeLayout mainTimeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_main, container, false);
        mainTimeLayout = (MainTimeLayout)view.findViewById(R.id.main_time_layout);
        CircularSeekBar circularSeekBar = (CircularSeekBar)view.findViewById(R.id.timeSeekBar);
        mainTimeLayout.setTime(mainActivity.planHour, mainActivity.planMinute);
        circularSeekBar.setListener(new OnSeekChangeListener() {
            @Override
            public void onChange(int newValue) {
                mainActivity.planHour = newValue / 60;
                mainActivity.planMinute = newValue % 60;
                mainTimeLayout.setTime(mainActivity.planHour, mainActivity.planMinute);
            }
        });

        view.findViewById(R.id.start).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mainActivity.planHour == 0 && mainActivity.planMinute == 0) {
                    Toast.makeText(mainActivity, "时间不能设定为0:00哦~", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (patternSpinner.getSelectedItem().toString().equals(MyApp.PatternStrings[2])) {
                    for (int i = 0; i < MyApp.CustomerKeyStrings.length; i++) {
                        if (mainActivity.AppIni.getString(MyApp.CustomerKeyStrings[i], "").equals(
                                "")) {
                            Toast.makeText(mainActivity, "请先设置" + MyApp.CustomerStrings[i],
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                MyApp.state = (MyApp.INMONITOR);
                MyApp.Pattern = patternSpinner.getSelectedItem().toString();
                Log.i("com.rg.phone_away.fragment.MainFragment.onCreateView(...).new OnClickListener() {...}.onClick",
                        " pattern" + MyApp.Pattern);
                MyApp.Scene = sceneSpinner.getSelectedItem().toString();
                Intent intent = new Intent(mainActivity, MonitorService.class);
                intent.putExtra("remainTimeInMinutes", 60 * mainActivity.planHour
                        + mainActivity.planMinute);
                mainActivity.startService(intent);

                mainActivity.setContentView();
            }
        });

        view.findViewById(R.id.title_set_app).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mainActivity.menu.showSecondaryMenu();
            }
        });

        view.findViewById(R.id.title_user_detail).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mainActivity.menu.showMenu();
            }
        });

        for (String a : MyApp.SceneStrings) {
            sceneList.add(a);
        }
        for (String a : MyApp.PatternStrings) {
            patternList.add(a);
        }

        sceneSpinner = (Spinner)view.findViewById(R.id.sceneSpinner);
        patternSpinner = (Spinner)view.findViewById(R.id.patternSpinner);

        patternAdapter = new ArrayAdapter<String>(mainActivity, R.layout.item_spinner, patternList);
        // 第三步：为适配器设置下拉列表下拉时的菜单样式。
        patternAdapter

        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 第四步：将适配器添加到下拉列表上
        patternSpinner.setAdapter(patternAdapter);

        sceneAdapter = new ArrayAdapter<String>(mainActivity, R.layout.item_spinner, sceneList);
        // 第三步：为适配器设置下拉列表下拉时的菜单样式。
        sceneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 第四步：将适配器添加到下拉列表上
        sceneSpinner.setAdapter(sceneAdapter);

        int sceneSelected = mainActivity.AppIni.getInt("sceneSelected", 0);
        int patternSelected = mainActivity.AppIni.getInt("patternSelected", 0);

        sceneSpinner.setSelection(sceneSelected, true);
        sceneSpinner.setOnItemSelectedListener(this);
        patternSpinner.setSelection(patternSelected, true);
        patternSpinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == sceneSpinner) {
            mainActivity.editor.putInt("sceneSelected", position).commit();
        }
        if (parent == patternSpinner) {
            mainActivity.editor.putInt("patternSelected", position).commit();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
