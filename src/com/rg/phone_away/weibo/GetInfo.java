package com.rg.phone_away.weibo;

import java.io.FileOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.rg.phone_away.Constants;

public class GetInfo {

    public static JSONObject getUserInfo(MyAccessToken myAccessToken) {
        String url = "https://api.weibo.com/2/users/show.json";
        url = url + "?" + "uid=" + myAccessToken.uid + "&access_token=" + myAccessToken.accessToken;
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(result);
                return jsonObject;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void getUserIcon(String filename, Context context, String iconUrl) {

        try {
            FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(iconUrl);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            entity.writeTo(outStream);

            outStream.flush();
            outStream.close();

            Log.i("Jun", "getUserIcon" + "url : " + iconUrl);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void getFriendInfo(Context context, MyAccessToken myAccessToken, int page) {
        String url = "https://api.weibo.com/2/friendships/friends/bilateral.json";
        url = url + "?" + "uid=" + myAccessToken.uid + "&access_token=" + myAccessToken.accessToken
                + "&page=" + page;
        Log.i("Jun", "getFriendInfo" + "getInfoUrl " + url);

        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(result);
                SharedPreferences friends = context.getSharedPreferences("friends_info",
                        Context.MODE_PRIVATE);
                Editor editor = friends.edit();

                Log.i("Jun", "getFriendInfo json :" + jsonObject);

                JSONArray users = jsonObject.getJSONArray("users");
                JSONObject user;
                int num = users.length();
                for (int i = 0; i < num; ++i) {

                    user = users.getJSONObject(i);
                    String screen_name = user.getString("screen_name");

                    String profile_image_url = user.getString("profile_image_url");
                    editor.putString("friend_name_" + ((page - 1) * 50 + i), screen_name);
                    getUserIcon("friend_icon_" + ((page - 1) * 50 + i) + ".jpg", context,
                            profile_image_url);

                }

                // int total_number = jsonObject.getInt("total_number");
                int total_number = num;
                editor.putInt("total_number", total_number);
                editor.commit();

                if (page * 50 < total_number) {
                    getFriendInfo(context, myAccessToken, page + 1);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Constants.finishDownloadIconAction);
                    context.sendBroadcast(intent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
