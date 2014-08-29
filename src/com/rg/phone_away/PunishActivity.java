
package com.rg.phone_away;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rg.phone_away.VoiceThread.OnMusicFinishListener;

public class PunishActivity extends Activity {

    MyApp app;

    ImageView imageView;

    boolean hasPunish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (MyApp)getApplication();
        Log.i("com.rg.phone_away.PunishActivity.onCreate", " " + MyApp.Pattern);

        if (MyApp.PunishMode == 2) {
            setContentView(R.layout.activity_punish_mode2);

            findViewById(R.id.know).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    PunishActivity.this.finish();
                }
            });

            return;
        }

        if (MyApp.isCustomerMode()) {

            setContentView(R.layout.acivity_punish_customer);
            ((TextView)findViewById(R.id.punish_textview)).setText(app.mainActivity.AppIni
                    .getString(MyApp.CustomerKeyStrings[2], ""));
        } else {

            setContentView(R.layout.acivity_punish);
            findViewById(R.id.punish_picture).setBackgroundResource(Constants.getPunishPicture(-1));
        }

        imageView = (ImageView)findViewById(R.id.number_punish_countdown);

        app.imageViewCountDownTime = imageView;
        app.punishActivity = this;

        if (MyApp.PunishMode == 3) {
            imageView.setVisibility(View.GONE);

            // 聚会模式下 发声
            new VoiceThread(this, "voice/party_punish_voice.mp3", new OnMusicFinishListener() {

                @Override
                public void onMusicFinish() {
                    Intent intent = new Intent(PunishActivity.this, TruthOrDareActivity.class);
                    PunishActivity.this.startActivity(intent);
                }
            }).start();

        } else if (MyApp.PunishMode == 2) {

        } else {
            if (app.countDownTimeRemainTime == -1) {

                int[] arrays = {
                        R.array.punish_study, R.array.punish_work, R.array.punish_party,
                };

                String[] strs = null;

                for (int i = 0; i < 3; ++i) {
                    if (MyApp.Scene.equals(MyApp.SceneStrings[i])) {
                        strs = getResources().getStringArray(arrays[i]);
                    }
                }

                String punishString = "Fail!" + System.currentTimeMillis();

                String last = app.mainActivity.AppIni.getString("lastSend", "");
                if (strs != null) {
                    int index = (int)(System.currentTimeMillis() % strs.length);
                    punishString = strs[index];
                    if (punishString.equals(last)) {
                        punishString = strs[(index + 1) % strs.length];
                    }
                }
                Intent intent = new Intent(this, PunishService.class);

                intent.putExtra("punishString", punishString);

                startService(intent);
            }
        }

    }

    @Override
    protected void onPause() {
        app.imageViewCountDownTime = null;
        app.punishActivity = null;
        if (MyApp.PunishMode == 1) {
            if (!hasPunish) {
                // 惩罚声音
                Intent intent = new Intent(this, PunishVoiceActivity.class);
                startActivity(intent);
                new VoiceThread(this, "voice/punish/" + System.currentTimeMillis() % 8+ ".mp3",
                        new OnMusicFinishListener() {

                            @Override
                            public void onMusicFinish() {
                                sendBroadcast(new Intent(Constants.punishVoiceFinish));
                            }

                        }).start();
            }

        }
        super.onPause();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        if (MyApp.PunishMode == 1) {
            Toast.makeText(this, "请接受惩罚", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

}
