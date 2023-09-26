package com.zpf.mysqlitedemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.zpf.modelsqlite.interfaces.ISqlJsonUtil;
import com.zpf.modelsqlite.SqlUtil;
import com.zpf.modelsqlite.interfaces.ObjCreator;
import com.zpf.mysqlitedemo.R;
import com.zpf.mysqlitedemo.data.AppConfig;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.logging.Logger;

public class MainActivity extends BaseActivity {
    private final Gson gson = new Gson();

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("ModelSQLite");
        tvLeft.setVisibility(View.GONE);
        SqlUtil.initConfig(getApplication(), null);
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
        final ConstructorConstructor constructor = new ConstructorConstructor(Collections.emptyMap(), true, Collections.emptyList());
        SqlUtil.addTypeCreator(new ObjCreator<Object>() {
            @Override
            public Object create(@NonNull Type t) {
                ObjectConstructor<?> objectConstructor = constructor.get(TypeToken.get(t));
                if (objectConstructor != null) {
                    return objectConstructor.construct();
                }
                return null;
            }
        });
        SqlUtil.setLogger(Logger.getGlobal());
    }

    public void showGroupList(View view) {
        startActivity(makeIntent(ListActivity.class).putExtra(ListActivity.IS_SELECT, false).putExtra(ListActivity.IS_STUDENT, false));
    }

    public void findGroup(View view) {
        startActivity(makeIntent(DetailActivity.class).putExtra(DetailActivity.TYPE, DetailActivity.TYPE_FIND).putExtra(DetailActivity.IS_STUDENT, false));
    }

    public void addGroup(View view) {
        startActivity(makeIntent(DetailActivity.class).putExtra(DetailActivity.TYPE, DetailActivity.TYPE_ADD).putExtra(DetailActivity.IS_STUDENT, false));
    }

    public void showStudentList(View view) {
        startActivity(makeIntent(ListActivity.class).putExtra(ListActivity.IS_SELECT, false).putExtra(ListActivity.IS_STUDENT, true));
    }

    public void findStudent(View view) {
        startActivity(makeIntent(DetailActivity.class).putExtra(DetailActivity.TYPE, DetailActivity.TYPE_FIND).putExtra(DetailActivity.IS_STUDENT, true));
    }

    public void addStudent(View view) {
        startActivity(makeIntent(DetailActivity.class).putExtra(DetailActivity.TYPE, DetailActivity.TYPE_ADD).putExtra(DetailActivity.IS_STUDENT, true));
    }

    public void change(View view) {
        AppConfig.checked = ((Switch) view).isChecked();
    }

    private Intent makeIntent(Class<?> cls) {
        return new Intent(this, cls);
    }
}