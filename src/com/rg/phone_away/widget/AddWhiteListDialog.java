package com.rg.phone_away.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rg.phone_away.R;

/**
 * @author YangJun
 * @date 2013 2013-12-7 上午1:15:33
 */
public class AddWhiteListDialog extends AlertDialog implements
		android.view.View.OnClickListener, OnItemClickListener {

	OnClickListener onClickListenerForPositionButton;
	Context context;
	List<PackageInfo> sysApps;
	List<Boolean> sysAppsState;
	List<PackageInfo> userApps;
	List<Boolean> userAppsState;
	ListView appListView;

	WhiteListDialogAdapter userAppAdapter;
	WhiteListDialogAdapter sysAppAdapter;

	public AddWhiteListDialog(Context context, List<PackageInfo> sysApps,
			List<Boolean> sysAppsState, List<PackageInfo> userApps,
			List<Boolean> userAppsState,
			OnClickListener onClickListenerForPositionButton) {
		super(context);
		this.context = context;
		this.sysApps = sysApps;
		this.sysAppsState = sysAppsState;
		this.userApps = userApps;
		this.userAppsState = userAppsState;
		this.onClickListenerForPositionButton = onClickListenerForPositionButton;

		setDialogContent();

	}

	void setDialogContent() {

		setIcon(0);
		setTitle("选择要加入白名单的应用");
		setButton(BUTTON_POSITIVE, "确定", onClickListenerForPositionButton);
		setButton(BUTTON_NEGATIVE, "取消", (OnClickListener) null);

		LayoutInflater inflater = LayoutInflater.from(context);

		View view = inflater.inflate(R.layout.dialog_white_list, null);
		setView(view);

		appListView = (ListView) view.findViewById(R.id.applist);

		userAppAdapter = new WhiteListDialogAdapter(context, userApps,
				userAppsState);
		sysAppAdapter = new WhiteListDialogAdapter(context, sysApps,
				sysAppsState);

		appListView.setAdapter(userAppAdapter);
		view.findViewById(R.id.userAppButton).setOnClickListener(this);
		view.findViewById(R.id.sysAppButton).setOnClickListener(this);

		appListView.setOnItemClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.userAppButton:
			appListView.setAdapter(userAppAdapter);
			appListView.invalidate();
			break;
		case R.id.sysAppButton:
			appListView.setAdapter(sysAppAdapter);
			appListView.invalidate();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		WhiteListDialogAdapter whiteListAdapter = (WhiteListDialogAdapter) parent
				.getAdapter();
		List<PackageInfo> apps = whiteListAdapter.apps;

		PackageInfo Info = apps.get(position);
		String packagename = Info.packageName;
		Log.i("com.rg.phone_away.weight.AddWhiteListDialog.onItemClick",
				"position = " + position + "packagename = " + packagename
						+ "  " + whiteListAdapter.lockState.get(position));
		if (whiteListAdapter.lockState.get(position)) {
			whiteListAdapter.lockState.set(position, Boolean.valueOf(false));
		} else {
			whiteListAdapter.lockState.set(position, Boolean.valueOf(true));
		}
		Log.i("com.rg.phone_away.weight.AddWhiteListDialog.onItemClick",
				"position = " + position + "packagename = " + packagename
						+ "  " + whiteListAdapter.lockState.get(position));
		whiteListAdapter.notifyDataSetChanged();
	}

}

class WhiteListDialogAdapter extends BaseAdapter {

	final class ViewHolder {
		public ImageView AppIcon;
		public TextView AppName;
		public CheckBox AppLockState;
	}

	ViewHolder holder;
	LayoutInflater mInflater;
	List<PackageInfo> apps;
	List<Boolean> lockState;
	Context context;
	SharedPreferences AppIni;

	public WhiteListDialogAdapter(Context context, List<PackageInfo> apps,
			List<Boolean> lockState) {
		// TODO Auto-generated constructor stub

		holder = new ViewHolder();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.apps = new ArrayList<PackageInfo>(apps);
		AppIni = context
				.getSharedPreferences("configure", Context.MODE_PRIVATE);
		this.lockState = lockState;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return apps.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.item_white_list_dialog,
					null);

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

		if (lockState.get(position)) {
			holder.AppLockState.setChecked(true);
		} else {
			holder.AppLockState.setChecked(false);
		}
		return convertView;
	}

}
