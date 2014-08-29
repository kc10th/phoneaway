package com.rg.phone_away;

import java.util.Calendar;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MonitorService extends Service {

    MyApp myApp;

    Handler handler;

    Runnable runnable;

    ActivityManager mActivityManager;

    SharedPreferences AppIni;

    Calendar endTime;

    public String TAG = "com.rg.phone_away.MonitorService";

    @Override
    public void onCreate() {

        // TODO Auto-generated method stub

        /*
         * 设置进程优先级 防止被一键清理 *已测试长按home 可以避免服务进程被杀死 360等软件的清理 暂时未测试 * 对应
         * onDestroy中的stopForeground方法
         * @param id The identifier for this notification as per
         * NotificationManager.notify(int, Notification); must not be 0. *
         * @param notification The Notification to be displayed. *
         */
        //
        int idForNotification = 1;
        startForeground(idForNotification, new Notification());

        myApp = (MyApp)getApplication();

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onDestroy");

        stopForeground(true);

        myApp.state = (MyApp.NOTINMONITOR);

        handler.removeCallbacks(runnable);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        Log.i(TAG, "onStartCommand");

        int remainTimeInMinutes = intent.getIntExtra("remainTimeInMinutes", 0);

        int remainTimeInMillis = remainTimeInMinutes * 60 * 1000;

        myApp.state = (MyApp.INMONITOR);

        endTime = Calendar.getInstance();
        // endTime.setTimeInMillis(System.currentTimeMillis() + 5000);
        endTime.setTimeInMillis(System.currentTimeMillis() + remainTimeInMillis);

        init();

        handler.postDelayed(runnable, 100);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    void init() {
        handler = new Handler();

        AppIni = getSharedPreferences("configure", Context.MODE_PRIVATE);
        mActivityManager = (ActivityManager)getSystemService("activity");

        runnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                checkTopActivity();
                // Log.i("com.rg", "runnable is going");

                setTimeDisplay();

                handler.postDelayed(this, 250);

            }
        };
    }

    long now;

    long end;

    long tmp;

    void setTimeDisplay() {
        now = System.currentTimeMillis();
        end = endTime.getTimeInMillis();

        tmp = (end - now) / 1000;
        if (tmp <= 0) {

            Intent intent = new Intent(this, AwardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            myApp.state = MyApp.NOTINMONITOR;
            myApp.mainActivity.setContentView();

            stopSelf();

        }
        if (myApp.state == MyApp.NOTINMONITOR_INVISIBLE || myApp.state == MyApp.INMONITOR_INVISIBLE) {
            return;
        }
        if (myApp.state == MyApp.NOTINMONITOR) {
            Intent intent = new Intent(myApp, MonitorService.class);
            myApp.stopService(intent);
            return;
        }
        if (myApp.state == MyApp.INMONITOR) {

            int seconds = (int)(tmp % 60);
            int minutes = (int)((tmp / 60) % 60);
            int hours = (int)(tmp / 3600);
            myApp.mainActivity.remainTimeLayout.setTime(hours, minutes, seconds);
        }
    }

    void checkTopActivity() {
        ComponentName topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
        String packageName = topActivity.getPackageName();
        String className = topActivity.getClassName();

        // 检测是否在白名单中

        if (AppIni.getBoolean(packageName, false)) {

        } else {
            Intent intent = new Intent(this, LockActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
