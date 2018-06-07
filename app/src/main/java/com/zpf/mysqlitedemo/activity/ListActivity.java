package com.zpf.mysqlitedemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zpf.mysqlitedemo.R;
import com.zpf.mysqlitedemo.adapter.GroupAdapter;
import com.zpf.mysqlitedemo.adapter.StudentAdapter;
import com.zpf.mysqlitedemo.data.Group;
import com.zpf.mysqlitedemo.data.Student;

/**
 * Created by ZPF on 2018/4/29.
 */
public class ListActivity extends BaseActivity {
    private StudentAdapter studentAdapter;
    private GroupAdapter groupAdapter;
    public static final String IS_STUDENT = "is_student_type";
    public static final String IS_SELECT = "is_select";
    private boolean isAsc = false;
    private final int jumpToDetail = 1234;

    @Override
    public int getLayoutId() {
        return R.layout.activity_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvRight.setText("升序排列");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAsc = !isAsc;
                if (isAsc) {
                    tvRight.setText("降序排列");
                } else {
                    tvRight.setText("升序排列");
                }
                refreshList();
            }
        });
        final boolean isStudentType = getIntent().getBooleanExtra(IS_STUDENT, false);
        final boolean isSelect = getIntent().getBooleanExtra(IS_SELECT, false);
        if (isStudentType) {
            studentAdapter = new StudentAdapter();
            studentAdapter.setItemClickListener(new StudentAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(int position, Student student) {
                    if (isSelect) {
                        Intent data = new Intent();
                        data.putExtra("data", student);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.IS_STUDENT, true);
                        intent.putExtra(DetailActivity.TYPE, DetailActivity.TYPE_DETAIL);
                        intent.putExtra(DetailActivity.DATA, student);
                        startActivityForResult(intent, jumpToDetail);
                    }
                }
            });
            recyclerView.setAdapter(studentAdapter);
            tvTitle.setText("Student列表");
        } else {
            groupAdapter = new GroupAdapter();
            recyclerView.setAdapter(groupAdapter);
            groupAdapter.setItemClickListener(new GroupAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(int position, Group group) {
                    if (isSelect) {
                        Intent data = new Intent();
                        data.putExtra("data", group);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.IS_STUDENT, false);
                        intent.putExtra(DetailActivity.TYPE, DetailActivity.TYPE_DETAIL);
                        intent.putExtra(DetailActivity.DATA, group);
                        startActivityForResult(intent, jumpToDetail);
                    }
                }
            });
            tvTitle.setText("Group列表");
        }
        refreshList();
    }

    private void refreshList() {
        if (studentAdapter != null) {
            studentAdapter.refresh(isAsc);
        } else if (groupAdapter != null) {
            groupAdapter.refresh(isAsc);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            refreshList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}