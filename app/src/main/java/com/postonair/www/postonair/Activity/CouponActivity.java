package com.postonair.www.postonair.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.postonair.www.postonair.R;

public class CouponActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        ImageButton ImageButton_coupon_before = (ImageButton) findViewById(R.id.ImageButton_coupon_before);
        ImageButton_coupon_before.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImageButton_coupon_before:
                finish();
                break;
        }
    }
}
