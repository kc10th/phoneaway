package com.rg.phone_away.weibo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyAccessToken  {
	public String username;
	public String sex;
	public String accessToken;
	public	String uid;
	public	long endTimeInMillis;
	public MyAccessToken(String token, String expires_in, String uid) {
		// TODO Auto-generated constructor stub
		long remainTimeInSceonds = Long.parseLong(expires_in);
		
		remainTimeInSceonds -= 3600; // 有效期 减一小时
		
		long nowTimeMillis = System.currentTimeMillis();
		
		endTimeInMillis = nowTimeMillis + remainTimeInSceonds * 1000;
		
		this.accessToken = token;
		this.uid = uid;
		this.sex = "";
		this.username = "";
	}
	

	public MyAccessToken(String token, long endTimeInMillis, String uid) {
		// TODO Auto-generated constructor stub
		this.accessToken = token;
		this.uid = uid;
		this.endTimeInMillis = endTimeInMillis;
		this.sex = "";
		this.username = "";
	}
	

	
	public boolean isSessionValid(){
		if(accessToken.equals(""))
			return false;
		
		return endTimeInMillis > System.currentTimeMillis();
	}
	
	public void keepAccessToken(Context context){
		SharedPreferences weibo_info = context.getSharedPreferences("weibo_info", Context.MODE_PRIVATE);
		Editor editor = weibo_info.edit();
		editor.putString("AccessToken", accessToken);
		editor.putString("Uid", uid);
		editor.putLong("EndTimeInMillis", endTimeInMillis);
		editor.putString("Username", username);
		editor.putString("Sex", sex);
		editor.commit();

	}
	
	public static void clear(Context context){
		SharedPreferences weibo_info = context.getSharedPreferences("weibo_info", Context.MODE_PRIVATE);
		Editor editor = weibo_info.edit();
		editor.putString("AccessToken", "");
		editor.putString("Uid", "");
		editor.putLong("EndTimeInMillis", 0);
		editor.putString("Username","" );
		editor.putString("Sex", "");
		editor.commit();
		
	}
	
	public static MyAccessToken readAccessToken(Context context){
		MyAccessToken r = new MyAccessToken("","0","");
		SharedPreferences weibo_info = context.getSharedPreferences("weibo_info", Context.MODE_PRIVATE);
		r.accessToken = weibo_info.getString("AccessToken", "");
		r.uid = weibo_info.getString("Uid", "");
		r.endTimeInMillis = weibo_info.getLong("EndTimeInMillis", 0);
		r.sex = weibo_info.getString("Sex", "");
		r.username = weibo_info.getString("Username", "");
		return r;
		
	}
}
	
