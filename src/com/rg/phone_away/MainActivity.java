package com.rg.phone_away;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Gallery;
import android.widget.Toast;

import com.rg.phone_away.Constants.PackageName;
import com.rg.phone_away.fragment.MainFragment;
import com.rg.phone_away.fragment.MainInMonitorFragment;
import com.rg.phone_away.fragment.SetAppFragment;
import com.rg.phone_away.fragment.UserDetailFragment;
import com.rg.phone_away.weibo.ConstantsOfWeibo;
import com.rg.phone_away.weibo.MyAccessToken;
import com.rg.phone_away.weibo.WebViewActivity;
import com.rg.phone_away.widget.MainTimeLayout;
import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity {

    public SharedPreferences AppIni;

    public Editor editor;

    public MainTimeLayout remainTimeLayout;

    public int planHour = 0, planMinute = 30;

    public SlidingMenu menu;

    public MyApp myApp;

    public MyAccessToken myAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppIni = getSharedPreferences("configure", Context.MODE_PRIVATE);
        editor = AppIni.edit();
        myApp = (MyApp)getApplication();

        myApp.mainActivity = this;

        setContentView();
        addWhiteList();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if (myApp.state == MyApp.INMONITOR)
            myApp.state = (MyApp.INMONITOR_INVISIBLE);
        if (myApp.state == MyApp.NOTINMONITOR)
            myApp.state = (MyApp.NOTINMONITOR_INVISIBLE);
        super.onPause();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (myApp.state == MyApp.INMONITOR_INVISIBLE) {
            myApp.state = (MyApp.INMONITOR);
            setContentView();
        }
        if (myApp.state == MyApp.NOTINMONITOR_INVISIBLE) {
            myApp.state = (MyApp.NOTINMONITOR);
        }
    }

    public void setContentView() {
        // TODO Auto-generated method stub

        myAccessToken = MyAccessToken.readAccessToken(this);

        if (!(myAccessToken.isSessionValid())) {

            Toast.makeText(MainActivity.this, "请登录新浪微博授权", Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_welcome);
            AutoGalleryAdapter adapter = new AutoGalleryAdapter(this);
            Gallery gallery = (Gallery)findViewById(R.id.welcome_gallery);
            gallery.setAdapter(adapter);
            findViewById(R.id.loginButton).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);

                    startActivityForResult(intent, ConstantsOfWeibo.RequestCode.FromMainActivity);
                }
            });
            return;
        }
        if (myApp.state == MyApp.INMONITOR || myApp.state == MyApp.INMONITOR_INVISIBLE) {

            setContentView(R.layout.activity_main);
            setSildingMenu();
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

            MainInMonitorFragment fragment = new MainInMonitorFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment)
                    .commitAllowingStateLoss();

            myApp.state = (MyApp.INMONITOR);

        } else {

            setContentView(R.layout.activity_main);
            setSildingMenu();

            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
            menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
            setTheme(R.style.NPWidget_Holo_Light_NumberPicker);

            MainFragment fragment = new MainFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment)
                    .commitAllowingStateLoss();

            myApp.state = (MyApp.NOTINMONITOR);

        }
    }

    Fragment leftFragment, rightFragment;

    void setSildingMenu() {
        menu = new SlidingMenu(this);

        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        menu.setMode(SlidingMenu.LEFT_RIGHT);
        // menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidth(50);
        menu.setBehindOffset(100);
        menu.setFadeDegree(0.35f);
        menu.setShadowDrawable(R.drawable.shadow_left);
        menu.setSecondaryShadowDrawable(R.drawable.shadow_right);
        menu.setMenu(R.layout.sliding_menu_left);
        menu.setSecondaryMenu(R.layout.sliding_menu_right);

        leftFragment = new UserDetailFragment();

        getFragmentManager().beginTransaction().replace(R.id.sliding_menu_left, leftFragment)
                .commitAllowingStateLoss();

        rightFragment = new SetAppFragment();
        getFragmentManager().beginTransaction().replace(R.id.sliding_menu_right, rightFragment)
                .commitAllowingStateLoss();

    }

    void addWhiteList() {
        // 添加白名单
        editor.putBoolean(PackageName.packageName, true);
        editor.putBoolean(PackageName.contactsPackageName, true);
        editor.putBoolean(PackageName.mmsPackageName, true);
        editor.putBoolean(PackageName.phonePackageName, true);
        editor.putBoolean(PackageName.settingsPackageName, true);

        // 白名单中添加启动器 Launder
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo ri : resolveInfo) {

            editor.putBoolean(ri.activityInfo.packageName, true);
        }

        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {

        Log.i("com.jun.MainAcitvity", "onactivityresult");

        if (resultCode == ConstantsOfWeibo.ResutlCode.FromWebViewActivity_Fail) {
            Toast.makeText(MainActivity.this, "授权失败，请重试", Toast.LENGTH_SHORT).show();
        }

        if (resultCode == ConstantsOfWeibo.ResutlCode.FromWebViewActivity_Success) {

            setContentView();
            if (leftFragment != null) {
                UserDetailFragment fragment = (UserDetailFragment)leftFragment;
                registerReceiver(fragment.receiver, fragment.filter);
            }
            Toast.makeText(MainActivity.this, "授权成功，系统正在努力加载您的好友列表~", Toast.LENGTH_SHORT).show();
            editor.putBoolean("isLogin", true).commit();

        }

    }
}
