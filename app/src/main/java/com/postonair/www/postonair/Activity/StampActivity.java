package com.postonair.www.postonair.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.postonair.www.postonair.R;

public class StampActivity extends Activity implements View.OnClickListener {

    private ImageView imageButton_stamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamp);

        ImageButton ImageButton_stamp_before = (ImageButton) findViewById(R.id.ImageButton_stamp_before);
        ImageButton_stamp_before.setOnClickListener(this);

        imageButton_stamp = (ImageView) findViewById(R.id.ImageView_stamp);

        ImageButton stamp_cardlist_rounded_1 = (ImageButton) findViewById(R.id.stamp_cardlist_rounded_1);
        stamp_cardlist_rounded_1.setOnClickListener(this);

        ImageButton stamp_cardlist_rounded_2 = (ImageButton) findViewById(R.id.stamp_cardlist_rounded_2);
        stamp_cardlist_rounded_2.setOnClickListener(this);

        ImageButton stamp_cardlist_rounded_3 = (ImageButton) findViewById(R.id.stamp_cardlist_rounded_3);
        stamp_cardlist_rounded_3.setOnClickListener(this);

        ImageButton stamp_cardlist_rounded_4 = (ImageButton) findViewById(R.id.stamp_cardlist_rounded_4);
        stamp_cardlist_rounded_4.setOnClickListener(this);

        ImageButton stamp_cardlist_rounded_5 = (ImageButton) findViewById(R.id.stamp_cardlist_rounded_5);
        stamp_cardlist_rounded_5.setOnClickListener(this);

        ImageButton stamp_cardlist_rounded_6 = (ImageButton) findViewById(R.id.stamp_cardlist_rounded_6);
        stamp_cardlist_rounded_6.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImageButton_stamp_before:
                finish();
                break;
            case R.id.stamp_cardlist_rounded_1:
                imageButton_stamp.setImageResource(R.drawable.stamp_cardlist_1);
                break;
            case R.id.stamp_cardlist_rounded_2:
                imageButton_stamp.setImageResource(R.drawable.stamp_cardlist_2);
                break;
            case R.id.stamp_cardlist_rounded_3:
                imageButton_stamp.setImageResource(R.drawable.stamp_cardlist_3);
                break;
            case R.id.stamp_cardlist_rounded_4:
                imageButton_stamp.setImageResource(R.drawable.stamp_cardlist_4);
                break;
            case R.id.stamp_cardlist_rounded_5:
                imageButton_stamp.setImageResource(R.drawable.stamp_cardlist_5);
                break;
            case R.id.stamp_cardlist_rounded_6:
                imageButton_stamp.setImageResource(R.drawable.stamp_cardlist_6);
                break;
        }
    }
}
