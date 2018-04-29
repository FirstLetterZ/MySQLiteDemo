package com.zpf.mysqlitedemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zpf.modelsqlite.CacheUtil;
import com.zpf.modelsqlite.ColumnEnum;
import com.zpf.modelsqlite.SQLiteInfo;
import com.zpf.mysqlitedemo.R;
import com.zpf.mysqlitedemo.data.AppConfig;
import com.zpf.mysqlitedemo.data.Group;
import com.zpf.mysqlitedemo.data.Student;

import java.util.List;

/**
 * Created by ZPF on 2018/4/29.
 */
public class DetailActivity extends BaseActivity {
    public static final String IS_STUDENT = "is_student_type";
    public static final String DATA = "data";
    public static final String TYPE = "type";

    public static final int TYPE_DETAIL = 0;
    public static final int TYPE_FIND = 1;
    public static final int TYPE_ADD = 2;

    private Button btnDelete, btnModify, btnConfirm, btnFind;
    private View llStudent, llGroup;
    private TextView tvFindResult;
    private EditText etStudentId, etStudentName, etStudentAge, etGroupId, etGroupName;
    private RadioGroup rgSex;
    private boolean isStudentType;
    private Gson gson = new Gson();
    private String studentId, studentName, studentAge, studentSex, groupId, groupName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        btnDelete = findViewById(R.id.btn_delete);
        btnModify = findViewById(R.id.btn_modify);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnFind = findViewById(R.id.btn_find);
        llStudent = findViewById(R.id.ll_student);
        llGroup = findViewById(R.id.ll_group);
        tvFindResult = findViewById(R.id.tv_find_result);
        etStudentId = findViewById(R.id.et_student_id);
        etStudentName = findViewById(R.id.et_student_name);
        etStudentAge = findViewById(R.id.et_student_age);
        etGroupId = findViewById(R.id.et_group_id);
        etGroupName = findViewById(R.id.et_group_name);
        rgSex = findViewById(R.id.rg_sex);
        //保存
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etGroupId.setEnabled(false);
                etGroupName.setEnabled(true);
                etStudentId.setEnabled(false);
                etStudentAge.setEnabled(true);
                etStudentName.setEnabled(true);
                rgSex.setEnabled(true);
                btnModify.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);
            }
        });
        //删除
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tb;
                String id;
                if (isStudentType) {
                    tb = AppConfig.TB_STUDENT;
                    id = etStudentId.getText().toString().trim();
                } else {
                    tb = AppConfig.TB_GROUP;
                    id = etStudentId.getText().toString().trim();
                }
                if (TextUtils.isEmpty(id)) {
                    toast("id 不能为空");
                    return;
                }
                SQLiteInfo sqLiteInfo = new SQLiteInfo(tb).addQueryCondition(ColumnEnum.COLUMN_INT_001, id);
                CacheUtil.instance().delete(sqLiteInfo);
            }
        });
        //查找
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteInfo sqLiteInfo = initQueryInfo();
                if (isStudentType) {
                    List<Student> studentList = CacheUtil.instance().selectValueList(Student.class, sqLiteInfo);
                    if (studentList == null || studentList.size() == 0) {
                        tvFindResult.setText("未查到结果");
                    } else {
                        tvFindResult.setText(gson.toJson(studentList));
                    }
                } else {
                    List<Group> groupList = CacheUtil.instance().selectValueList(Group.class, sqLiteInfo);
                    if (groupList == null || groupList.size() == 0) {
                        tvFindResult.setText("未查到结果");
                    } else {
                        tvFindResult.setText(gson.toJson(groupList));
                    }
                }
            }
        });
        isStudentType = getIntent().getBooleanExtra(IS_STUDENT, false);
        llStudent.setVisibility(isStudentType ? View.VISIBLE : View.GONE);
        final int type = getIntent().getIntExtra(TYPE, TYPE_ADD);
        switch (type) {
            case TYPE_DETAIL:
                if (isStudentType) {
                    btnConfirm.setVisibility(View.GONE);
                    btnModify.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.VISIBLE);
                    btnFind.setVisibility(View.GONE);
                    etGroupId.setEnabled(false);
                    etGroupName.setEnabled(false);
                    etStudentId.setEnabled(false);
                    etStudentAge.setEnabled(false);
                    etStudentName.setEnabled(false);
                    rgSex.setEnabled(false);
                    Student student = getIntent().getParcelableExtra(DATA);
                    if (student != null) {
                        initWithData(student);
                        tvTitle.setText("详情");
                    } else {
                        toast("详情数据为空");
                    }
                } else {
                    Group group = getIntent().getParcelableExtra(DATA);
                    if (group != null) {
                        initWithData(group);
                        tvTitle.setText("详情");
                    } else {
                        toast("详情数据为空");
                    }
                }
                break;
            case TYPE_FIND:
                etGroupId.setEnabled(true);
                etGroupName.setEnabled(true);
                etStudentId.setEnabled(true);
                etStudentAge.setEnabled(true);
                etStudentName.setEnabled(true);
                rgSex.setEnabled(true);
                if (isStudentType) {
                    llGroup.setVisibility(View.GONE);
                }
                btnConfirm.setVisibility(View.GONE);
                btnModify.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnFind.setVisibility(View.VISIBLE);
                tvTitle.setText("数据查询");
                tvFindResult.setVisibility(View.VISIBLE);
                break;
            default:
            case TYPE_ADD:
                btnConfirm.setVisibility(View.VISIBLE);
                btnModify.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnFind.setVisibility(View.GONE);
                etGroupId.setEnabled(true);
                etGroupName.setEnabled(true);
                etStudentId.setEnabled(true);
                etStudentAge.setEnabled(true);
                etStudentName.setEnabled(true);
                rgSex.setEnabled(true);
                tvTitle.setText("新增");
                break;
        }
    }


    private void initWithData(Student student) {
        etStudentId.setText("" + student.getId());
        etStudentName.setText(student.getName());
        String sex = student.isFemale() ? "女" : "男";
        setChecked(rgSex, sex);
        etStudentAge.setText("" + student.getAge());
        initWithData(student.getGroup());
    }

    private void initWithData(Group group) {
        etGroupId.setText("" + group.getId());
        etGroupName.setText(group.getName());
    }

    private void setChecked(RadioGroup rg, String str) {
        if (rg == null || rg.getChildCount() == 0 || TextUtils.isEmpty(str)) {
            return;
        }
        RadioButton rbt;
        for (int i = 0, len = rg.getChildCount(); i < len; i++) {
            rbt = (RadioButton) rg.getChildAt(i);
            String content = rbt.getText().toString().replace(" ", "");
            if (content.equals(str)) {
                rbt.setChecked(true);
                break;
            }
        }
    }

    private void saveData() {
        getEditValue();
        if (TextUtils.isEmpty(groupId)) {
            toast("group id 不能为空");
            return;
        }

        if (TextUtils.isEmpty(groupId)) {
            toast("group name 不能为空");
            return;
        }
        Group group = new Group();
        group.setId(Integer.valueOf(groupId));
        group.setName(groupName);
        if (isStudentType) {
            if (TextUtils.isEmpty(studentId)) {
                toast("student id 不能为空");
                return;
            }
            if (TextUtils.isEmpty(studentName)) {
                toast("student name 不能为空");
                return;
            }
            if (TextUtils.isEmpty(studentAge)) {
                toast("student age 不能为空");
                return;
            }
            if (TextUtils.isEmpty(studentSex)) {
                toast("请选择性别");
                return;
            }
            boolean female = "女".equals(studentSex);
            Student student = new Student();
            SQLiteInfo sqLiteInfo = new SQLiteInfo(AppConfig.TB_STUDENT).addQueryCondition(ColumnEnum.COLUMN_INT_001, studentId);
            if (CacheUtil.instance().selectValueModel(student, sqLiteInfo)) {
                toast("id=" + studentId + "的条目已存在，更新条目内容");
            } else {
                toast("不存在id=" + studentId + "的条目，新建条目");
            }
            student.setId(Integer.valueOf(studentId));
            student.setFemale(female);
            student.setAge(Integer.valueOf(studentAge));
            student.setName(studentName);
            student.setGroup(group);
            CacheUtil.instance().saveValue(student, sqLiteInfo);
        } else {
            SQLiteInfo sqLiteInfo = new SQLiteInfo(AppConfig.TB_GROUP).addQueryCondition(ColumnEnum.COLUMN_INT_001, groupId);
            if (CacheUtil.instance().selectValueModel(new Group(), sqLiteInfo)) {
                toast("id=" + groupId + "的条目已存在，更新条目内容");
            } else {
                toast("不存在id=" + groupId + "的条目，新建条目");
            }
            CacheUtil.instance().saveValue(group, sqLiteInfo);
        }
    }

    private SQLiteInfo initQueryInfo() {
        SQLiteInfo sqLiteInfo;
        getEditValue();
        if (isStudentType) {
            sqLiteInfo = new SQLiteInfo(AppConfig.TB_STUDENT);
            if (!TextUtils.isEmpty(studentId)) {
                sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_INT_001, studentId);
            }
            if (!TextUtils.isEmpty(studentAge)) {
                sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_INT_002, studentAge);
            }
            if (!TextUtils.isEmpty(studentName)) {
                sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_STRING_001, studentName);
            }
            if (!TextUtils.isEmpty(studentSex)) {
                sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_BOOLEAN_001, "女".equals(studentSex) ? "1" : "0");
            }
        } else {
            sqLiteInfo = new SQLiteInfo(AppConfig.TB_GROUP);
            if (!TextUtils.isEmpty(groupId)) {
                sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_INT_001, groupId);
            }
            if (!TextUtils.isEmpty(groupName)) {
                sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_STRING_001, groupName);
            }
        }
        return sqLiteInfo;
    }

    private void getEditValue() {
        groupId = etGroupId.getText().toString().trim();
        groupName = etGroupName.getText().toString().trim();
        studentId = etStudentId.getText().toString().trim();
        studentName = etStudentName.getText().toString().trim();
        studentAge = etStudentAge.getText().toString().trim();
        int sexId = rgSex.getCheckedRadioButtonId();
        if (sexId < 0) {
            studentSex = null;
        } else {
            studentSex = sexId == R.id.rb_man ? "男" : "女";
        }
    }


}