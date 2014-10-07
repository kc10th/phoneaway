package com.rg.phone_away;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class AutoGalleryAdapter extends BaseAdapter {

    private Context mContext;
    private Integer[] mps = {
            R.drawable.welcome_banner1, R.drawable.welcome_banner2
    };

    public AutoGalleryAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image = new ImageView(mContext);
        position = position % mps.length;
        image.setImageResource(mps[position]);
        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return image;
    }

    public int getSize() {
        return mps.length;
    }

}
