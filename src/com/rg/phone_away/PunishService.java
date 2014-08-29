
package com.rg.phone_away;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.rg.phone_away.weibo.SendMessageThread;
import com.rg.phone_away.weibo.SendMessageThread.OnSendMessageFinishListener;

/**
 * @author YangJun
 * @date 2013 2013-12-18 下午11:34:35
 */
public class PunishService extends Service {

    MyApp app;

    int[] numberImage = new int[] {
            0, R.drawable.count_down_number_1, R.drawable.count_down_number_2,
            R.drawable.count_down_number_3, R.drawable.count_down_number_4,
            R.drawable.count_down_number_5, R.drawable.count_down_number_6,
            R.drawable.count_down_number_7, R.drawable.count_down_number_8,
            R.drawable.count_down_number_9, R.drawable.count_down_number_10
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {

        public void onTick(long millisUntilFinished) {
            int remainSeconds = (int)(millisUntilFinished / 1000);

            if (app.imageViewCountDownTime != null) {

                app.imageViewCountDownTime.setImageResource(numberImage[remainSeconds]);

            }
            app.countDownTimeRemainTime = remainSeconds;
            if (remainSeconds > 7)
                return;
            if (MyApp.PunishMode == 1) {
                if (isAccessNetwork()) {
                    new SendMessageThread(app, app.mainActivity.myAccessToken.accessToken,
                            punishString, onSendMessageFinishListener).start();
                    this.cancel();

                    app.punishActivity.hasPunish = true;
                    stopForeground(true);
                    stopSelf();
                    if (app.punishActivity != null)
                        app.punishActivity.finish();
                    app.countDownTimeRemainTime = -1;
                }

            }

        }

        public void onFinish() {

            stopForeground(true);
            stopSelf();
            if (app.punishActivity != null)
                app.punishActivity.finish();
            app.countDownTimeRemainTime = -1;
        }
    };

    OnSendMessageFinishListener onSendMessageFinishListener = new OnSendMessageFinishListener() {

        @Override
        public void onFinish(boolean isSuccess) {
            if (isSuccess) {
                app.handler.sendEmptyMessage(MyApp.SendWeiboState_Success);
                app.mainActivity.editor.putString("lastSend", punishString).commit();

            } else {
                Toast.makeText(PunishService.this, "由于网络原因，微博发送失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    String punishString;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        punishString = intent.getStringExtra("punishString");
        Log.i("com.rg.phone_away.PunishService.onStartCommand", " ");

        app = (MyApp)getApplication();
        /*
         * 设置进程优先级 防止被一键清理 *已测试长按home 可以避免服务进程被杀死 360等软件的清理 暂时未测试 * 对应
         * onDestroy中的stopForeground方法
         * @param id The identifier for this notification as per
         * NotificationManager.notify(int, Notification); must not be 0. *
         * @param notification The Notification to be displayed. *
         */
        int idForNotification = 1;
        startForeground(idForNotification, new Notification());

        countDownTimer.start();

        return super.onStartCommand(intent, flags, startId);
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

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("com.rg.phone_away.PunishService.onCreate", " ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        Log.i("com.rg.phone_away.PunishService.onDestroy", " ");
    }
}
