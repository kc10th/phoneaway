package com.rg.phone_away;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rg.phone_away.Constants.PackageName;
import com.rg.phone_away.widget.AddWhiteListDialog;

public class WhiteListActivity extends Activity implements
		android.view.View.OnClickListener, OnItemLongClickListener,
		OnClickListener {

	SharedPreferences AppIni;
	Editor editor;

	List<PackageInfo> sysApps = new ArrayList<PackageInfo>(); // 程序list(包含系统app)
	List<Boolean> sysAppsState = new ArrayList<Boolean>();
	List<PackageInfo> userApps = new ArrayList<PackageInfo>(); // 用户程序list
	List<Boolean> userAppsState = new ArrayList<Boolean>();
	List<PackageInfo> userWhiteList = new ArrayList<PackageInfo>();

	List<String> whiteList_Must;

	ListView appListView;

	WhiteListAdapter userWhiteListAdapter;

	@Override
	protected void onPause() {
		super.onPause();
		String string;
		for (int i = 0; i < userAppsState.size(); ++i) {
			string = userApps.get(i).packageName;
			if (userAppsState.get(i)) {
				editor.putBoolean(string, true);
			} else {
				editor.remove(string);
			}
		}
		for (int i = 0; i < sysAppsState.size(); ++i) {
			string = sysApps.get(i).packageName;
			if (sysAppsState.get(i)) {
				editor.putBoolean(string, true);
			} else {
				editor.remove(string);
			}
		}
		editor.commit();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题栏

		setContentView(R.layout.activity_white_list);

		appListView = (ListView) findViewById(R.id.myapplist);
		
		findViewById(R.id.title_back_button).setOnClickListener(this);

		findViewById(R.id.title_add_button).setOnClickListener(this);

		getWhiteList_Must();

		AppIni = getSharedPreferences("configure", Context.MODE_PRIVATE);
		editor = AppIni.edit();

		PackageManager packageManager = this.getPackageManager();
		List<PackageInfo> allApps = packageManager.getInstalledPackages(0);
		for (int i = 0; i < allApps.size(); i++) {
			PackageInfo pak = allApps.get(i);
			// 判断是否为非系统预装的应用程序
			if (whiteList_Must.contains(pak.packageName))
				continue;
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				userApps.add(pak);
				if (AppIni.getBoolean(pak.packageName, false)) {
					userWhiteList.add(pak);
					userAppsState.add(Boolean.valueOf(true));
				} else {
					userAppsState.add(Boolean.valueOf(false));
				}
			} else {
				sysApps.add(pak);
				if (AppIni.getBoolean(pak.packageName, false)) {
					userWhiteList.add(pak);
					sysAppsState.add(Boolean.valueOf(true));
				} else {
					sysAppsState.add(Boolean.valueOf(false));
				}
			}

		}

		userWhiteListAdapter = new WhiteListAdapter(WhiteListActivity.this,
				userWhiteList);

		appListView.setAdapter(userWhiteListAdapter);

		appListView.setOnItemLongClickListener(this);

	}

	void getWhiteList_Must() {

		whiteList_Must = new ArrayList<String>();
		whiteList_Must.add(PackageName.packageName);
		whiteList_Must.add(PackageName.contactsPackageName);
		whiteList_Must.add(PackageName.mmsPackageName);
		whiteList_Must.add(PackageName.phonePackageName);
		whiteList_Must.add(PackageName.settingsPackageName);
		// 白名单中添加启动器 Launder
		PackageManager packageManager = getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);

		for (ResolveInfo ri : resolveInfo) {
			whiteList_Must.add(ri.activityInfo.packageName);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.title_back_button:
			WhiteListActivity.this.finish();
			break;
		case R.id.title_add_button:
			new AddWhiteListDialog(this, sysApps, sysAppsState, userApps,
					userAppsState, this).show();
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {

		final WhiteListAdapter adapter = (WhiteListAdapter) parent.getAdapter();
		Builder builder = new Builder(WhiteListActivity.this);
		builder.setTitle("移出白名单？");
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PackageInfo r = adapter.apps.remove(position);
				adapter.notifyDataSetChanged();
				for (int i = 0; i < userApps.size(); ++i) {
					if (userApps.get(i).equals(r)) {
						userAppsState.set(i, Boolean.valueOf(false));
						return;
					}
				}
				for (int i = 0; i < sysApps.size(); ++i) {
					if (sysApps.get(i).equals(r)) {
						sysAppsState.set(i, Boolean.valueOf(false));
						return;
					}
				}

			}
		});
		builder.show();
		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		userWhiteList.clear();
		
		if (which == DialogInterface.BUTTON_POSITIVE ) {
			for(int i = 0; i < userApps.size(); ++ i){
				if(userAppsState.get(i)){
					userWhiteList.add(userApps.get(i));
				}
			}
			for(int i = 0; i < sysApps.size(); ++ i){
				if(sysAppsState.get(i)){
					userWhiteList.add(sysApps.get(i));
				}
			}
		}
		
		userWhiteListAdapter.notifyDataSetChanged();	
				
	}

}

class WhiteListAdapter extends BaseAdapter {

	final class ViewHolder {
		public ImageView AppIcon;
		public TextView AppName;
		public CheckBox AppLockState;
	}

	ViewHolder holder;
	LayoutInflater mInflater;
	List<PackageInfo> apps;
	Context context;
	SharedPreferences AppIni;

	public WhiteListAdapter(Context context, List<PackageInfo> apps) {
		 
		holder = new ViewHolder();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.apps = apps;
		AppIni = context
				.getSharedPreferences("configure", Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return apps.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.item_white_list, null);

			holder.AppIcon = (ImageView) convertView.findViewById(R.id.AppIcon);
			holder.AppName = (TextView) convertView.findViewById(R.id.AppName);
			holder.AppLockState = (CheckBox) convertView
					.findViewById(R.id.AppLockState);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PackageManager pm = context.getPackageManager(); // 得到pm对象
		PackageInfo Info = apps.get(position);
		ApplicationInfo applicationInfo = Info.applicationInfo;

		holder.AppIcon.setImageDrawable(pm.getApplicationIcon(applicationInfo));
		holder.AppName.setText(pm.getApplicationLabel(applicationInfo));

		return convertView;
	}

}
