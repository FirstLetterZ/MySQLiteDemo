package com.zpf.mysqlitedemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zpf.modelsqlite.CacheDao;
import com.zpf.modelsqlite.ColumnEnum;
import com.zpf.modelsqlite.SQLiteInfo;

public class MainActivity extends Activity {

    private EditText etInt01;
    private EditText etString01;
    private EditText etInt02;
    private TextView tvMsg;
    private TestInfo testInfo = new TestInfo();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.instance().addActivity(this);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        etInt01 = v(R.id.et_int);
        etString01 = v(R.id.tet_string);
        etInt02 = v(R.id.et_int2);
        tvMsg = v(R.id.tv_message);
        Button btnSave = v(R.id.btn_save);
        Button btnDelete = v(R.id.btn_delete);
        Button btnSearch = v(R.id.btn_search);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getValue(etInt01)) || TextUtils.isEmpty(getValue(etString01))) {
                    toast("请将用于保存的内容填写完整");
                } else {
                    testInfo.setTestInt(Integer.valueOf(getValue(etInt01)));
                    testInfo.setTestString(getValue(etString01));
                    int a;
                    if (TextUtils.isEmpty(getValue(etInt02))) {
                        a = CacheDao.instance().saveValue(testInfo);
                    } else {
                        SQLiteInfo sqLiteInfo = new SQLiteInfo(AppConfig.TB_TEST);
                        sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_INT_001, getValue(etInt02));
                        a = CacheDao.instance().saveValue(testInfo, sqLiteInfo);
                    }
                    if (a == -1) {
                        tvMsg.setText("保存失败！");
                    } else if (a == 0) {
                        tvMsg.setText("新增数据成功");
                    } else if (a == 1) {
                        tvMsg.setText("更新数据成功");
                    }
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getValue(etInt02))) {
                    toast("请将用于查找数据的条件填写完整");
                } else {
                    SQLiteInfo sqLiteInfo = new SQLiteInfo(AppConfig.TB_TEST);
                    sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_INT_001, getValue(etInt02));
                    CacheDao.instance().delete(sqLiteInfo);
                    tvMsg.setText("数据已删除");
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getValue(etInt02))) {
                    toast("请将用于查找数据的条件填写完整");
                } else {
                    SQLiteInfo sqLiteInfo = new SQLiteInfo(AppConfig.TB_TEST);
                    sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_INT_001, getValue(etInt02));
                    TestInfo info = new TestInfo();
                    boolean success = CacheDao.instance().selectValueModel(info, sqLiteInfo);
                    if (success) {
                        String text = gson.toJson(info);
                        tvMsg.setText("数据查询结果：" + text);
                    } else {
                        tvMsg.setText("没有对应的数据");
                    }
                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private String getValue(TextView view) {
        return view.getText().toString().replace(" ", "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.instance().removeActivity(this);
    }

    private <T extends View> T v(int resId) {
        return findViewById(resId);
    }
}