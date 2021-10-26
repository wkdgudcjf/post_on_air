package com.postonair.www.postonair.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.postonair.www.postonair.R;

public class FilupActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filup);

        ImageButton ImageButton_account_before = (ImageButton) findViewById(R.id.ImageButton_filup_before);
        ImageButton_account_before.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImageButton_filup_before:
                finish();
                break;
        }
    }
}
