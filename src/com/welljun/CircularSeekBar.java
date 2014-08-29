package com.welljun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author: YangJun
 * @date: 2014 2014-8-4 下午9:20:44
 */
public class CircularSeekBar extends View {

    public interface OnSeekChangeListener {

        void onChange(int newValue);
    }

    private OnSeekChangeListener listener;

    private double currentRad = 0;

    private int StandardValue = 60;

    private int MaxValue = Integer.MAX_VALUE;

    private final int R = 200, r = 150;

    private final int DeviationDistance = 20;

    private final double DeviationRad = 0.5;

    private Paint paint;

    private double twoPI = 2 * Math.PI;

    /**
     * @param context
     * @param attrs
     */
    public CircularSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (paint == null) {
            paint = new Paint();
        }
        paint.setColor(0xff007fff);
        paint.setStyle(Style.FILL); // 设置填充
        paint.setStrokeWidth(6);
        paint.setAntiAlias(true);
        final double deltaRad = (Math.PI) / 30;
        final int colorStart = 0xff1ee6cb, colorEnd = 0xffc13aee;
        int numOfColor = (int)(radStandardization(currentRad) / deltaRad) + 1;
        if (numOfColor <= 1) {
            numOfColor = 0;
        }

        for (int i = 0; i < 60; ++i) {
            double rad = deltaRad * i;
            if (i >= numOfColor) {
                paint.setColor(Color.WHITE);
            } else {
                int color = 0xff000000;

                double percent = (i / (double)(numOfColor));

                color |= (int)(((colorStart >> 16) & 0xff) + percent
                        * (((colorEnd >> 16) & 0xff) - ((colorStart >> 16) & 0xff))) << 16;

                color |= (int)(((colorStart >> 8) & 0xff) + percent
                        * (((colorEnd >> 8) & 0xff) - ((colorStart >> 8) & 0xff))) << 8;

                color |= (int)(((colorStart >> 0) & 0xff) + percent
                        * (((colorEnd >> 0) & 0xff) - ((colorStart >> 0) & 0xff))) << 0;

                paint.setColor(color);
            }
            Pair<Integer, Integer> p1 = getPointXYFromRad(this.r, rad);
            Pair<Integer, Integer> p2 = getPointXYFromRad(this.R + (i % 15 == 0 ? 20 : 0), rad);
            canvas.drawLine(p1.first, p1.second, p2.first, p2.second, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isEnabled())
            return false;

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {

            double distance = Math.sqrt(Math.pow((getWidth() / 2 - (int)event.getX()), 2)
                    + Math.pow((getHeight() / 2 - (int)event.getY()), 2));
            if (distance > R + DeviationDistance || distance < r - DeviationDistance) {
                return false;
            }

            double rad1 = getRad(getWidth() / 2, getHeight() / 2, event.getX(), event.getY());
            double deltaRad = getDeltaRad(this.currentRad, rad1);

            // 如果落点偏离 当前点 超过一定的角度
            if (deltaRad > DeviationRad) {
                return false;
            }

            return true;

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            double distance = Math.sqrt(Math.pow((getWidth() / 2 - (int)event.getX()), 2)
                    + Math.pow((getHeight() / 2 - (int)event.getY()), 2));
            if (distance > R + DeviationDistance || distance < r - DeviationDistance) {
                return false;
            }
            double rad = getRad(getWidth() / 2, getHeight() / 2, event.getX(), event.getY());
            double deltaRad = getDeltaRad(currentRad, rad);
            if ((deltaRad < 0 ? -deltaRad : deltaRad) > DeviationRad) {
                return false;
            }
            if (currentRad + deltaRad < 0) {
                setRad(0);
            } else {
                double maxRad = (MaxValue / (double)StandardValue) * (twoPI);
                if (currentRad + deltaRad > maxRad) {
                    setRad(maxRad);
                } else {
                    setRad(currentRad + deltaRad);
                }

            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            setRad(currentRad);
        }

        return true;
    }

    public Pair<Integer, Integer> getPointXYFromRad(double R, double rad) {
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;

        int Y = (int)((-R * Math.cos(rad)) + centerY);
        int X = (int)((R * Math.sin(rad)) + centerX);

        return new Pair<Integer, Integer>(X, Y);

    }

    private void setRad(double newRad) {
        if (newRad < 0) {
            return;
        }
        currentRad = newRad;
        if (this.listener != null) {
            this.listener.onChange(radToValue(currentRad));
        }
        invalidate();
    }

    private int radToValue(double rad) {

        if (rad < 0) {
            return 0;
        }

        int r = (int)(rad / (twoPI) * StandardValue);
        return r;
    }

    private double valueToRad(int value) {
        if (value < 0) {
            return 0;
        }
        return ((double)value) / StandardValue * (twoPI);
    }

    private double getDeltaRad(double lastRad, double newRad) {

        double deltaRad = newRad - lastRad;
        while (deltaRad > Math.PI) {
            deltaRad -= twoPI;
        }
        while (deltaRad <= -Math.PI) {
            deltaRad += twoPI;
        }
        return deltaRad;
    }

    private double getRad(float px1, float py1, float px2, float py2) {
        double x = px2 - px1;
        double y = py2 - py1;
        double xie = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double sinAngle = y / xie;
        double rad = Math.asin(sinAngle);
        // 坐标关系 rad的起点（屏幕正右方）和value的起点（屏幕正上方） 相差 4分之一个圆
        rad += Math.PI / 2;
        if (x < 0) {
            rad = twoPI - rad;
        }

        return rad;
    }

    private double radStandardization(double rad) {
        while (rad >= twoPI) {
            rad -= twoPI;
        }
        while (rad < 0) {
            rad += twoPI;
        }
        return rad;
    }

    public int getValue() {
        return radToValue(currentRad);
    }

    public void setValue(int value) {
        if (value < 0) {
            return;
        }
        currentRad = valueToRad(value);
        invalidate();
    }

    public void setStandardValue(int standardValue) {
        StandardValue = standardValue;
    }

    public void setListener(OnSeekChangeListener listener) {
        this.listener = listener;
    }

    public int getMaxValue() {
        return MaxValue;
    }

    public void setMaxValue(int maxValue) {
        MaxValue = maxValue;
    }

}
