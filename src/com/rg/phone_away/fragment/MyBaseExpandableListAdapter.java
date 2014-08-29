package com.rg.phone_away.fragment;

import java.io.FileInputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.rg.phone_away.R;

/**
 * @author: YangJun
 * @date: 2014 2014-8-6 上午12:12:34
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;

    SharedPreferences friends;

    SharedPreferences at_list;

    int numOfFriends;

    FileInputStream inStream;

    LayoutInflater mInflater;

    public boolean[] listState;

    public String[] names;

    public Bitmap[] icons;

    Bitmap defaultIcon;

    public MyBaseExpandableListAdapter(Context context) {
        this.context = context;
        friends = context.getSharedPreferences("friends_info", Context.MODE_PRIVATE);
        at_list = context.getSharedPreferences("at_list", Context.MODE_PRIVATE);
        mInflater = LayoutInflater.from(context);
        defaultIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_icon);
        loadData();
    }

    void loadData() {
        numOfFriends = friends.getInt("total_number", 0);
        names = new String[numOfFriends];
        icons = new Bitmap[numOfFriends];
        listState = new boolean[numOfFriends];
        for (int i = 0; i < numOfFriends; i++) {
            names[i] = friends.getString("friend_name_" + i, "");
            listState[i] = at_list.getBoolean(names[i], false);
            try {
                inStream = context.openFileInput("friend_icon_" + i + ".jpg");
                icons[i] = BitmapFactory.decodeStream(inStream);
                inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public final class ViewHolder {
        public ImageView friendIcon;

        public TextView friendName;

        public CheckBox atListState;
    }

    String[] groupStrings = new String[] {
            "设置自定义模式", "设置互@好友名单"
    };

    // 子视图显示文字
    String[] childStrings = new String[] {
            "警示语句", "奖励语句", "惩罚语句"
    };

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groupStrings.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        if (groupPosition == 0)
            return 3;
        else
            return numOfFriends;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    TextView getTextView() {
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 50);
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setTextColor(Color.WHITE);
        return textView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {

        if (convertView == null || convertView.getId() != R.id.expandlist_groupitem) {
            convertView = mInflater.inflate(R.layout.expandlist_groupitem, null);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.foldImageView);
        imageView.setImageResource(isExpanded ? R.drawable.slide_left_expandlist_unfold
                : R.drawable.slide_left_expandlist_fold);

        TextView textView = (TextView)convertView.findViewById(R.id.groupNameTextView);
        textView.setText(groupStrings[groupPosition]);
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (groupPosition == 0) {
            TextView textView;
            if (convertView instanceof TextView) {
                textView = (TextView)convertView;
            } else {
                textView = getTextView();
                textView.setPadding(130, 0, 0, 0);
                textView.setTextSize(20);
            }

            textView.setText(childStrings[childPosition]);
            return textView;

        } else {
            if (convertView != null) {
                if (convertView.getTag() instanceof ViewHolder) {
                    holder = (ViewHolder)convertView.getTag();
                }
            }
            if (holder == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.at_list_item, null);

                holder.friendIcon = (ImageView)convertView.findViewById(R.id.friendIcon);
                holder.friendName = (TextView)convertView.findViewById(R.id.friendName);
                holder.atListState = (CheckBox)convertView.findViewById(R.id.atListState);

                convertView.setTag(holder);

            }

            holder.friendIcon.setImageBitmap(icons[childPosition]);
            holder.friendName.setText(names[childPosition]);

            if (listState[childPosition]) {
                holder.atListState.setChecked(true);
            } else {
                holder.atListState.setChecked(false);
            }

            return convertView;

        }

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
