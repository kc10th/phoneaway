
package com.rg.phone_away;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rg.phone_away.weibo.MyAccessToken;
import com.rg.phone_away.weibo.SendMessageThread;
import com.rg.phone_away.weibo.SendMessageThread.OnSendMessageFinishListener;

public class AwardActivity extends Activity {

    ImageView imageView;

    MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);

        imageView = (ImageView)findViewById(R.id.award_picture);

        if (app.isCustomerMode()) {
            if (app.mainActivity.myAccessToken.sex.equals("m")) {
                imageView.setImageResource(R.drawable.customer_award_man);
            } else {

                imageView.setImageResource(R.drawable.customer_award_woman);
            }
            ((TextView)findViewById(R.id.award_tv)).setText(app.mainActivity.AppIni.getString(
                    MyApp.CustomerKeyStrings[1], "Success!"));
        } else {

            imageView.setImageResource(Constants.getAwardPicture());
        }

        app = (MyApp)getApplication();
        findViewById(R.id.success_message_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (!isAccessNetwork()) {
                    Toast.makeText(AwardActivity.this, "请先连接网络~", Toast.LENGTH_SHORT).show();
                    return;
                }
                award();
                AwardActivity.this.finish();
            }
        });
    }

    String awardString = "success!" + System.currentTimeMillis();

    void award() {
        MyAccessToken myAccessToken = MyAccessToken.readAccessToken(AwardActivity.this);

        int[] arrays = {
                R.array.award_study, R.array.award_work, R.array.award_party,
        };
        String[] strs = null;

        for (int i = 0; i < 3; ++i) {
            if (MyApp.Scene.equals(MyApp.SceneStrings[i])) {
                strs = getResources().getStringArray(arrays[i]);
            }
        }

        String last = app.mainActivity.AppIni.getString("lastSend", "");
        if (strs != null) {
            int index = (int)(System.currentTimeMillis() % strs.length);
            awardString = strs[index];
            if (awardString.equals(last)) {
                awardString = strs[(index + 1) % strs.length];
            }
        }
        new SendMessageThread(AwardActivity.this, myAccessToken.accessToken, awardString,
                new OnSendMessageFinishListener() {

                    @Override
                    public void onFinish(boolean isSuccess) {
                        if (isSuccess) {

                            ((MyApp)getApplication()).handler
                                    .sendEmptyMessage(MyApp.SendWeiboState_Success);
                            app.mainActivity.editor.putString("lastSend", awardString).commit();
                        } else {
                            ((MyApp)getApplication()).handler
                                    .sendEmptyMessage(MyApp.SendWeiboState_Fail);

                        }
                    }
                }).start();
    }

    boolean isAccessNetwork() {
        ConnectivityManager cManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            // 能联网
            return true;
        } else {
            // 不能联网
            return false;
        }
    }
}
