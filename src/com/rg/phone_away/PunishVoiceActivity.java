
package com.rg.phone_away;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;

/**
 * @author YangJun
 * @date 2013 2013-12-22 上午1:54:24
 */
public class PunishVoiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punish_voice);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    public void onBackPressed() {
    }

    IntentFilter filter = new IntentFilter(Constants.punishVoiceFinish);

    MyReceiver myReceiver = new MyReceiver();

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            PunishVoiceActivity.this.finish();
            unregisterReceiver(myReceiver);
        }

    }
}
