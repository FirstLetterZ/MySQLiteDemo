package com.zpf.mysqlitedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.gson.Gson;
import com.zpf.modelsqlite.CacheInitInterface;
import com.zpf.modelsqlite.CacheUtil;
import com.zpf.mysqlitedemo.R;

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
        CacheUtil.init(new CacheInitInterface() {
            @NonNull
            @Override
            public Context getContext() {
                return getApplicationContext();
            }

            @Override
            public String getDataBaseFolderPath() {
                return null;
            }

            @Override
            public String toJson(Object object) {
                return gson.toJson(object);
            }

            @Override
            public Object fromJson(String json, Class<?> cls) {
                return gson.fromJson(json, cls);
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
                .putExtra(DetailActivity.TYPE,DetailActivity.TYPE_FIND)
                .putExtra(DetailActivity.IS_STUDENT, false));
    }

    public void addGroup(View view) {
        startActivity(makeIntent(DetailActivity.class)
                .putExtra(DetailActivity.TYPE,DetailActivity.TYPE_ADD)
                .putExtra(DetailActivity.IS_STUDENT, false));

    }

    public void showStudentList(View view) {
        startActivity(makeIntent(ListActivity.class)
                .putExtra(ListActivity.IS_SELECT, false)
                .putExtra(ListActivity.IS_STUDENT, true));
    }

    public void findStudent(View view) {
        startActivity(makeIntent(DetailActivity.class)
                .putExtra(DetailActivity.TYPE,DetailActivity.TYPE_FIND)
                .putExtra(DetailActivity.IS_STUDENT, true));
    }

    public void addStudent(View view) {
        startActivity(makeIntent(DetailActivity.class)
                .putExtra(DetailActivity.TYPE,DetailActivity.TYPE_ADD)
                .putExtra(DetailActivity.IS_STUDENT, true));
    }

    private Intent makeIntent(Class<?> cls) {
        return new Intent(this, cls);
    }
}