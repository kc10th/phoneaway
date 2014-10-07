package com.rg.phone_away;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SRItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steady_run_item);

        findViewById(R.id.back_main_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        Intent intent = getIntent();
        int ItemNo = intent.getIntExtra("ItemNo", -1);
        init(ItemNo);
    }

    void init(int ItemNo) {
        TextView textView = (TextView)findViewById(R.id.steady_run_item_text);
        TextView title = (TextView)findViewById(R.id.steady_run_title);
        ImageView imgView = (ImageView)findViewById(R.id.steady_run_item_img);

        switch (ItemNo) {
            case 0:
                title.setText(R.string.steady_run_item0_title);
                textView.setText(R.string.steady_run_item0);
                imgView.setBackgroundResource(R.drawable.item0);
                break;

            case 1:
                title.setText(R.string.steady_run_item1_title);
                textView.setText(R.string.steady_run_item1);
                imgView.setBackgroundResource(R.drawable.item1);
                break;

            case 2:
                title.setText(R.string.steady_run_item2_title);
                textView.setText(R.string.steady_run_item2);
                imgView.setBackgroundResource(R.drawable.item2);
                break;

            case 3:
                title.setText(R.string.steady_run_item3_title);
                textView.setText(R.string.steady_run_item3);
                imgView.setBackgroundResource(R.drawable.item3);
                break;

            case 4:
                title.setText(R.string.steady_run_item4_title);
                textView.setText(R.string.steady_run_item4);
                imgView.setBackgroundResource(R.drawable.item4);
                break;

            case 5:
                title.setText(R.string.steady_run_item5_title);
                textView.setText(R.string.steady_run_item5);
                imgView.setBackgroundResource(R.drawable.item5);
                break;

            case 6:
                title.setText(R.string.steady_run_item6_title);
                textView.setText(R.string.steady_run_item6);
                imgView.setBackgroundResource(R.drawable.item6);
                break;

            case 7:
                title.setText(R.string.steady_run_item7_title);
                textView.setText(R.string.steady_run_item7);
                imgView.setBackgroundResource(R.drawable.item7);
                break;

            default:
                break;
        }
    }
}
