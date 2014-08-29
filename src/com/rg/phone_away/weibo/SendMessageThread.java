package com.rg.phone_away.weibo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SendMessageThread extends Thread {

	String accessToken, message;
	Context context;

	OnSendMessageFinishListener onSendMessageFinish;

	public interface OnSendMessageFinishListener {
		public void onFinish(boolean isSuccess);
	}

	public SendMessageThread(Context context, String accessToken,
			String message, OnSendMessageFinishListener onSendMessageFinish) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.accessToken = accessToken;
		this.onSendMessageFinish = onSendMessageFinish;
		SharedPreferences atList = context.getSharedPreferences("at_list",
				Context.MODE_PRIVATE);

		String at = "";
		Map<String, Boolean> map = (Map<String, Boolean>) atList.getAll();

		for (String key : map.keySet()) {
			if (map.get(key) == true) {
				at += "@";
				at += key;
				at += " ";
			}
		}

		this.message = at + message;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		sendMessage();
		super.run();
	}

	void sendMessage() {

		String api_url = "https://api.weibo.com/2/statuses/update.json";

		try {

			HttpPost post = new HttpPost(api_url);

			post.addHeader("charset", HTTP.UTF_8);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add

			(new BasicNameValuePair("access_token", accessToken));

			params.add

			(new BasicNameValuePair("status", message));

			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			Log.i("com.rg.phone_away.weibo.SendMessageThread.sendMessage",
					"token " + accessToken);

			Log.i("com.rg.phone_away.weibo.SendMessageThread.sendMessage",
					"message " + message);

			HttpResponse httpResponse = new DefaultHttpClient().execute(post);

			int statuscode = httpResponse.getStatusLine().getStatusCode();

			Log.i("com.rg.phone_away.weibo.SendMessageThread.sendMessage",
					"status = " + statuscode);

			if (statuscode == 200) {

				if (onSendMessageFinish != null)
					onSendMessageFinish.onFinish(true);

			} else {

				if (onSendMessageFinish != null)
					onSendMessageFinish.onFinish(false);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}
