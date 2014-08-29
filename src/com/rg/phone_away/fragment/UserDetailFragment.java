package com.rg.phone_away.fragment;

import java.io.FileInputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rg.phone_away.Constants;
import com.rg.phone_away.MainActivity;
import com.rg.phone_away.MyApp;
import com.rg.phone_away.R;
import com.rg.phone_away.weibo.MyAccessToken;

public class UserDetailFragment extends Fragment {

    public View view;

    MainActivity mainActivity;

    TextView textView;

    ImageView userIcon;

    MyBaseExpandableListAdapter listAdapter;

    ExpandableListView userSetList;

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            new AlertDialog.Builder(mainActivity).setMessage("确认登出？")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated
                            // method
                            // stub
                            MyAccessToken.clear(mainActivity);
                            SharedPreferences friends = mainActivity.getSharedPreferences(
                                    "friends_info", Context.MODE_PRIVATE);
                            int total_num = friends.getInt("total_number", 0);
                            friends.edit().clear().commit();
                            for (int i = 0; i < total_num; ++i) {
                                mainActivity.deleteFile("friend_icon_" + i + ".jpg");
                            }
                            mainActivity.deleteFile("userIcon.jpg");
                            mainActivity.getSharedPreferences("at_list", Context.MODE_PRIVATE)
                                    .edit().clear().commit();
                            mainActivity.editor.clear().commit();
                            mainActivity.setContentView();

                        }
                    }).setNegativeButton("取消", null).show();

        }
    };

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_user_detail, container, false);

        textView = (TextView)view.findViewById(R.id.userinfo);

        userSetList = (ExpandableListView)view.findViewById(R.id.userSetList);

        textView.setText(mainActivity.myAccessToken.username);

        userIcon = (ImageView)view.findViewById(R.id.userIcon);

        try {
            FileInputStream inStream = mainActivity.openFileInput("userIcon.jpg");

            Bitmap bm = BitmapFactory.decodeStream(inStream);
            userIcon.setImageBitmap(bm);
            inStream.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        view.findViewById(R.id.logoutButton).setOnClickListener(onClickListener);

        listAdapter = new MyBaseExpandableListAdapter(mainActivity);

        userSetList.setAdapter(listAdapter);

        userSetList.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandablelistview, View clickedView,
                    int groupPosition, final int childPosition, long childId) {

                Log.i("com.rg.phone_away.fragment.UserDetailFragment.onCreateView(...).new OnChildClickListener() {...}.onChildClick",
                        " group " + groupPosition + "childposition" + childPosition);
                if (groupPosition == 0) {
                    final EditText edit = new EditText(mainActivity);

                    int[] maxTextLength = new int[] {
                            24, 20, 25
                    };
                    edit.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(maxTextLength[childPosition])
                    });
                    String keyString = MyApp.CustomerKeyStrings[childPosition];
                    edit.setText(mainActivity.AppIni.getString(keyString, ""));
                    new AlertDialog.Builder(mainActivity)
                            .setTitle(
                                    "请输入" + listAdapter.childStrings[childPosition] + " (最多"
                                            + maxTextLength[childPosition] + "字" + ")")
                            .setView(edit)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String keyString = MyApp.CustomerKeyStrings[childPosition];
                                    mainActivity.editor.putString(keyString,
                                            edit.getText().toString()).commit();
                                }

                            }).setNegativeButton("取消", null).show();

                } else {

                    boolean b = listAdapter.listState[childPosition];
                    listAdapter.listState[childPosition] = !b;
                    listAdapter.notifyDataSetChanged();

                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Editor editor = listAdapter.at_list.edit();
        for (int i = 0; i < listAdapter.numOfFriends; ++i) {
            boolean p = listAdapter.listState[i];
            if (p) {
                editor.putBoolean(listAdapter.names[i], p);
            } else {
                editor.remove(listAdapter.names[i]);
            }
        }
        editor.commit();

    }

    public LoadFinishReceiver receiver = new LoadFinishReceiver();

    public IntentFilter filter = new IntentFilter(Constants.finishDownloadIconAction);

    class LoadFinishReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            listAdapter.loadData();
            listAdapter.notifyDataSetChanged();
            mainActivity.unregisterReceiver(receiver);
            Toast.makeText(mainActivity, "微博好友列表已加载完毕，可左滑选择哦", Toast.LENGTH_SHORT).show();
        }

    }
}
