package com.rg.phone_away;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MyApp extends Application {

	public static MainActivity mainActivity;
	public static PunishActivity punishActivity;

	public static ImageView imageViewCountDownTime = null;

	public static int countDownTimeRemainTime = -1;

	public static final int INMONITOR_INVISIBLE = 3;
	public static final int NOTINMONITOR_INVISIBLE = 2;

	public static final int INMONITOR = 1;
	public static final int NOTINMONITOR = 0;

	public static int state = NOTINMONITOR_INVISIBLE;

	public static final int SendWeiboState_Success = 1;
	public static final int SendWeiboState_Fail = -1;

	public Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == SendWeiboState_Success) {
				Toast.makeText(MyApp.this, "发送成功", Toast.LENGTH_SHORT).show();

			}
			if (msg.what == SendWeiboState_Fail) {
				Toast.makeText(MyApp.this, "发送失败", Toast.LENGTH_SHORT).show();
			}
		};

	};

	public static String Pattern = "";
	public static String Scene = "";

	public static String[] SceneStrings = { "学习场景", "工作场景", "聚会场景" };
	public static String[] PatternStrings = { "激励模式", "打击模式", "自定义模式" };

	public static String[] CustomerStrings =  { "警示语句", "奖励语句", "惩罚语句" };

	public static String[] CustomerKeyStrings = { "alert_string", "award_string",
			"punish_string" };

	public static int PunishMode = -1;
	
	public static boolean isCustomerMode(){
	    return Pattern.equals(PatternStrings[2]);
	}

	public static void punish() {

		if (Scene.equals(SceneStrings[2])) {
			// 聚会场景
			PunishMode = 3;
			return;
		}

		if (Pattern.equals(PatternStrings[0])) {
			// 激励模式
			PunishMode = 2;
			return;
		}
		// others
		PunishMode = 1;

	}
}
