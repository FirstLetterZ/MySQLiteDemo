package com.zpf.mysqlitedemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zpf.mysqlitedemo.R;

/**
 * Created by ZPF on 2018/4/29.
 */
public abstract class BaseActivity extends Activity {
    public abstract int getLayoutId();

    protected TextView tvLeft, tvRight, tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        FrameLayout contentLayout = findViewById(R.id.fl_content);
        getLayoutInflater().inflate(getLayoutId(), contentLayout, true);
        tvLeft = findViewById(R.id.tv_left);
        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);
        tvLeft.setText("返回");
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
