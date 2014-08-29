package com.rg.phone_away.weibo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rg.phone_away.MyApp;
import com.rg.phone_away.R;

public class WebViewActivity extends Activity {

	MyApp myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		myApp = (MyApp) getApplication();

		WebView wv = (WebView) findViewById(R.id.web);

		try {

			wv.clearCache(true);
			wv.clearHistory();

			String url = ConstantsOfWeibo.AUTH_URL;

			Log.i("com.rg webview  url", url);

			wv.setVerticalScrollBarEnabled(false);
			wv.setHorizontalScrollBarEnabled(false);
			wv.requestFocus();

			WebSettings webSettings = wv.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
			wv.loadUrl(url);

			WebViewClient wvc = new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {

					Uri uri = Uri.parse(url);
					String host = uri.getHost();

					// 测试要加载的url地址是否是 认证成功后的回调地址 回调地址为 "http://www.sina.com"
					if (host.contains("www.sina.com")) {
						CookieSyncManager.createInstance(WebViewActivity.this);
						CookieSyncManager.getInstance().startSync();
						CookieManager.getInstance().removeSessionCookie();
						if (url.contains("error")) {
							Intent intent = new Intent();
							WebViewActivity.this
									.setResult(
											ConstantsOfWeibo.ResutlCode.FromWebViewActivity_Fail,
											intent);
							WebViewActivity.this.finish();
							return true;
						} else {
							int k = url.indexOf("code=");
							String code = url.substring(k + 5, url.length());
							if (code.equals("")) {
								Intent intent = new Intent();
								WebViewActivity.this
										.setResult(
												ConstantsOfWeibo.ResutlCode.FromWebViewActivity_Fail,
												intent);
								WebViewActivity.this.finish();
								return true;
							}
							new Thread(new MyRunnable(code)).start();

						}

						return true;
					} else {
						view.loadUrl(url);
						return true;
					}

				}

				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					view.getSettings().setBlockNetworkImage(false);

				}
			};
			wv.setWebViewClient(wvc);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class MyRunnable implements Runnable {

		String code;

		public MyRunnable(String string) {
			// TODO Auto-generated constructor stub
			code = string;

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			myApp.mainActivity.myAccessToken = getToken(code);
			myApp.mainActivity.myAccessToken
					.keepAccessToken(myApp.mainActivity);

			if (myApp.mainActivity.myAccessToken != null) {
				Intent intent = new Intent();

				clearCache(WebViewActivity.this, 0);

				JSONObject jsonObject = GetInfo
						.getUserInfo(myApp.mainActivity.myAccessToken);

				try {
					String username = jsonObject.getString("screen_name");
					String sex = jsonObject.getString("gender");
					myApp.mainActivity.myAccessToken.username = username;
					myApp.mainActivity.myAccessToken.sex = sex;

					myApp.mainActivity.myAccessToken
							.keepAccessToken(myApp.mainActivity);

					GetInfo.getUserIcon("userIcon.jpg", WebViewActivity.this,
							jsonObject.getString("profile_image_url"));

					WebViewActivity.this
							.setResult(
									ConstantsOfWeibo.ResutlCode.FromWebViewActivity_Success,
									intent);
					WebViewActivity.this.finish();
					
					
					SharedPreferences friends = WebViewActivity.this.getSharedPreferences(
							"friends_info", Context.MODE_PRIVATE);
					Editor editor = friends.edit();
					editor.clear().commit();
					WebViewActivity.this.getSharedPreferences("at_list", Context.MODE_PRIVATE)
							.edit().clear().commit();
					
					
					GetInfo.getFriendInfo(WebViewActivity.this,
							myApp.mainActivity.myAccessToken, 1);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	MyAccessToken getToken(String code) {

		MyAccessToken myToken = null;
		if (code != null) {
			HttpPost httpPost = new HttpPost(
					"https://api.weibo.com/oauth2/access_token");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("client_id",
					ConstantsOfWeibo.APP_KEY));
			params.add(new BasicNameValuePair("client_secret",
					ConstantsOfWeibo.APP_SECRET));
			params.add(new BasicNameValuePair("grant_type",
					"authorization_code"));
			params.add(new BasicNameValuePair("redirect_uri",
					ConstantsOfWeibo.REDIRECT_URL));
			params.add(new BasicNameValuePair("code", code));
			HttpClient httpClient = new DefaultHttpClient();
			String result = "";
			try {

				httpPost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				result = EntityUtils.toString(httpResponse.getEntity());
				JSONObject json = new JSONObject(result);
				String token = json.getString("access_token");
				String expires_in = json.getString("expires_in");
				String uid = json.getString("uid");
				myToken = new MyAccessToken(token, expires_in, uid);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return myToken;
	}

	static String TAG = "com.rg";

	static int clearCacheFolder(final File dir, final int numDays) {

		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {

					// first delete subdirectories recursively
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}

					// then delete the files and subdirectories in this dir
					// only empty directories can be deleted, so subdirs have
					// been done first
					if (child.lastModified() < new Date().getTime() - numDays
							* DateUtils.DAY_IN_MILLIS) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				Log.e(TAG,
						String.format("Failed to clean the cache, error %s",
								e.getMessage()));
			}
		}
		return deletedFiles;
	}

	/*
	 * Delete the files older than numDays days from the application cache 0
	 * means all files.
	 */
	public static void clearCache(final Context context, final int numDays) {
		Log.i(TAG, String.format(
				"Starting cache prune, deleting files older than %d days",
				numDays));
		int numDeletedFiles = clearCacheFolder(context.getCacheDir(), numDays);
		Log.i(TAG, String.format("Cache pruning completed, %d files deleted",
				numDeletedFiles));
	}

}
