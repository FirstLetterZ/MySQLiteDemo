package com.zpf.mysqlitedemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Switch;

import com.google.gson.Gson;
import com.zpf.modelsqlite.interfaces.ISqlJsonUtil;
import com.zpf.modelsqlite.SqlUtil;
import com.zpf.mysqlitedemo.R;
import com.zpf.mysqlitedemo.data.AppConfig;

import java.lang.reflect.Type;

public class MainActivity extends BaseActivity {
    private Gson gson = new Gson();

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("ModelSQLite");
        tvLeft.setVisibility(View.GONE);
        SqlUtil.initConfig(getApplication(), "LOG_TAG", null);
        SqlUtil.setJsonUtil(new ISqlJsonUtil() {

            @Override
            public <T> T fromJson(String json, @NonNull Type type) {
                return gson.fromJson(json, type);
            }


            @NonNull
            @Override
            public String toJsonString(Object obj) {
                return gson.toJson(obj);
            }
        });
    }

    public void showGroupList(View view) {
        startActivity(makeIntent(ListActivity.class)
                .putExtra(ListActivity.IS_SELECT, false)
                .putExtra(ListActivity.IS_STUDENT, false));
    }

    public void findGroup(View view) {
        startActivity(makeIntent(DetailActivity.class)
                .putExtra(DetailActivity.TYPE, DetailActivity.TYPE_FIND)
                .putExtra(DetailActivity.IS_STUDENT, false));
    }

    public void addGroup(View view) {
        startActivity(makeIntent(DetailActivity.class)
                .putExtra(DetailActivity.TYPE, DetailActivity.TYPE_ADD)
                .putExtra(DetailActivity.IS_STUDENT, false));

    }

    public void showStudentList(View view) {
        startActivity(makeIntent(ListActivity.class)
                .putExtra(ListActivity.IS_SELECT, false)
                .putExtra(ListActivity.IS_STUDENT, true));
    }

    public void findStudent(View view) {
        startActivity(makeIntent(DetailActivity.class)
                .putExtra(DetailActivity.TYPE, DetailActivity.TYPE_FIND)
                .putExtra(DetailActivity.IS_STUDENT, true));
    }

    public void addStudent(View view) {
        startActivity(makeIntent(DetailActivity.class)
                .putExtra(DetailActivity.TYPE, DetailActivity.TYPE_ADD)
                .putExtra(DetailActivity.IS_STUDENT, true));
    }

    public void change(View view) {
        AppConfig.checked = ((Switch) view).isChecked();
    }

    private Intent makeIntent(Class<?> cls) {
        return new Intent(this, cls);
    }
}