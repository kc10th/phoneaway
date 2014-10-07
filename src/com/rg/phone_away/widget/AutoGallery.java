package com.rg.phone_away.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;

import com.rg.phone_away.AutoGalleryAdapter;

public class AutoGallery extends Gallery {

    int size = 0;
    Context context;

    public AutoGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.startAutoSwitch();
    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {

        int cur_position;

        @Override
        public void run() {
            handler.postDelayed(runnable, 3000);
            cur_position = getSelectedItemPosition();
            setSelection(cur_position % size, false);
            AutoGallery.this.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
        }
    };

    public void stopAutoSwitch() {
        handler.removeCallbacks(runnable);
    }

    public void startAutoSwitch() {

        handler.postDelayed(runnable, 3000);

    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        // TODO Auto-generated method stub
        super.setAdapter(adapter);

        size = ((AutoGalleryAdapter)adapter).getSize();

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int kEvent;

        if (isScrollingLeft(e1, e2)) { // Check if scrolling left
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }

        onKeyDown(kEvent, null);
        return true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            stopAutoSwitch();
            if (this.getSelectedItemPosition() == 0)
                this.setSelection(size);
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            startAutoSwitch();
        }

        return super.onTouchEvent(event);
    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();

    }

}
