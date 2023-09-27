package com.zpf.mysqlitedemo.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.zpf.modelsqlite.SQLiteInfo;
import com.zpf.modelsqlite.SqlColumnInfo;
import com.zpf.modelsqlite.constant.ColumnEnum;
import com.zpf.modelsqlite.SqlUtil;
import com.zpf.mysqlitedemo.R;
import com.zpf.mysqlitedemo.SqlDao;
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
    private RadioButton rbNull;
    private boolean isStudentType;
    private final Gson gson = new Gson();
    private Integer studentId, groupId, studentAge;
    private String studentName, studentSex, groupName;

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
        rbNull = findViewById(R.id.rb_null);
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
                etGroupName.setEnabled(!isStudentType);
                etStudentId.setEnabled(false);
                etStudentAge.setEnabled(true);
                etStudentName.setEnabled(true);
                setRadioGroupEnable(rgSex, true);
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
                    id = etGroupId.getText().toString().trim();
                }
                if (TextUtils.isEmpty(id)) {
                    toast("id 不能为空");
                    return;
                }
                int targetId = Integer.parseInt(id);
                if (AppConfig.checked) {
                    if (isStudentType) {
                        SqlDao.getApi().deleteStudent(targetId);
                    } else {
                        SqlDao.getApi().deleteGroup(targetId);
                    }
                } else {
                    SQLiteInfo sqLiteInfo = new SQLiteInfo(tb);
                    sqLiteInfo.getQueryInfoList().add(new SqlColumnInfo(ColumnEnum.COLUMN_INT_001, targetId));
                    SqlUtil.getDao().delete(sqLiteInfo);
                }
                toast("已删除");
                setResult(RESULT_OK);
                finish();
            }
        });
        //查找
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.checked) {
                    getEditValue();
                    if (isStudentType) {
                        String sName = null;
                        if (!TextUtils.isEmpty(studentName)) {
                            sName = studentName;
                        }
                        Boolean sex = null;
                        if (!TextUtils.isEmpty(studentSex)) {
                            sex = "女".equals(studentSex);
                        }
                        List<Student> studentList = SqlDao.getApi().queryStudent(studentId, sName, studentAge, sex, groupId);
                        setResult(studentList);
                    } else {
                        String gName = null;
                        if (!TextUtils.isEmpty(groupName)) {
                            gName = groupName;
                        }
                        List<Group> groupList = SqlDao.getApi().queryGroup(groupId, gName);
                        setResult(groupList);
                    }
                } else {
                    SQLiteInfo sqLiteInfo = initQueryInfo();
                    if (isStudentType) {
                        List<Student> studentList = SqlUtil.getDao().queryArray(Student.class, sqLiteInfo);
                        setResult(studentList);
                    } else {
                        List<Group> groupList = SqlUtil.getDao().queryArray(Group.class, sqLiteInfo);
                        setResult(groupList);
                    }
                }
            }
        });
        isStudentType = getIntent().getBooleanExtra(IS_STUDENT, false);
        llStudent.setVisibility(isStudentType ? View.VISIBLE : View.GONE);
        final int type = getIntent().getIntExtra(TYPE, TYPE_ADD);
        switch (type) {
            case TYPE_DETAIL:
                btnConfirm.setVisibility(View.GONE);
                btnModify.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnFind.setVisibility(View.GONE);
                rbNull.setVisibility(View.GONE);
                etGroupId.setEnabled(false);
                etGroupName.setEnabled(false);
                etStudentId.setEnabled(false);
                etStudentAge.setEnabled(false);
                etStudentName.setEnabled(false);
                if (isStudentType) {
                    setRadioGroupEnable(rgSex, false);
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
                setRadioGroupEnable(rgSex, true);
                if (isStudentType) {
                    llGroup.setVisibility(View.GONE);
                }
                btnConfirm.setVisibility(View.GONE);
                btnModify.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnFind.setVisibility(View.VISIBLE);
                rbNull.setVisibility(View.VISIBLE);
                tvTitle.setText("数据查询");
                tvFindResult.setVisibility(View.VISIBLE);
                break;
            default:
            case TYPE_ADD:
                btnConfirm.setVisibility(View.VISIBLE);
                btnModify.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnFind.setVisibility(View.GONE);
                rbNull.setVisibility(View.GONE);
                etGroupId.setEnabled(true);
                etGroupName.setEnabled(!isStudentType);
                etStudentId.setEnabled(true);
                etStudentAge.setEnabled(true);
                etStudentName.setEnabled(true);
                setRadioGroupEnable(rgSex, true);
                tvTitle.setText("新增");
                break;
        }
    }

    private void setResult(List<?> list) {
        if (list == null || list.size() == 0) {
            tvFindResult.setText("未查到结果");
        } else {
            tvFindResult.setText(gson.toJson(list));
        }
    }

    private void setRadioGroupEnable(RadioGroup radioGroup, boolean enable) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(enable);
        }
    }

    private void initWithData(Student student) {
        if (student == null) {
            return;
        }
        etStudentId.setText("" + student.getId());
        etStudentName.setText(student.getName());
        String sex = student.isFemale() ? "女" : "男";
        setChecked(rgSex, sex);
        etStudentAge.setText("" + student.getAge());
        initWithData(student.getGroup());
    }

    private void initWithData(Group group) {
        if (group == null) {
            return;
        }
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
        if (groupId == null) {
            toast("group id 不能为空");
            return;
        }
        Group group = new Group();
        group.setId(groupId);
        if (TextUtils.isEmpty(groupName)) {
            group.setName(null);
        } else {
            group.setName(groupName);
        }
        int result;
        if (isStudentType) {
            if (studentId == null) {
                toast("student id 不能为空");
                return;
            }
            if (TextUtils.isEmpty(studentName)) {
                toast("student name 不能为空");
                return;
            }
            if (studentAge == null) {
                toast("student age 不能为空");
                return;
            }
            if (TextUtils.isEmpty(studentSex)) {
                toast("请选择性别");
                return;
            }
            boolean female = "女".equals(studentSex);
            if (AppConfig.checked) {
                Student student = SqlDao.getApi().checkStudent(studentId);
                if (student != null) {
                    toast("id=" + studentId + "的条目已存在，更新条目内容");
                } else {
                    student = new Student();
                    toast("不存在id=" + studentId + "的条目，新建条目");
                }
                student.setId(studentId);
                student.setFemale(female);
                student.setAge(studentAge);
                student.setName(studentName);
                student.setGroup(group);
                result = SqlDao.getApi().saveStudent(student, studentId);
            } else {
                SQLiteInfo sqLiteInfo = new SQLiteInfo(AppConfig.TB_STUDENT).addQueryCondition(ColumnEnum.COLUMN_INT_001, studentId);
                Student student = SqlUtil.getDao().queryFirst(Student.class, sqLiteInfo);
                if (student != null) {
                    toast("id=" + studentId + "的条目已存在，更新条目内容");
                } else {
                    student = new Student();
                    toast("不存在id=" + studentId + "的条目，新建条目");
                }
                student.setId(studentId);
                student.setFemale(female);
                student.setAge(studentAge);
                student.setName(studentName);
                student.setGroup(group);
                result = SqlUtil.getDao().saveValue(student, sqLiteInfo);
            }
        } else {
            if (TextUtils.isEmpty(groupName)) {
                toast("group name 不能为空");
                return;
            }
            if (AppConfig.checked) {
                if (SqlDao.getApi().checkGroup(groupId) != null) {
                    toast("id=" + groupId + "的条目已存在，更新条目内容");
                } else {
                    toast("不存在id=" + groupId + "的条目，新建条目");
                }
                result = SqlDao.getApi().saveGroup(group, groupId);
            } else {
                SQLiteInfo sqLiteInfo = new SQLiteInfo(AppConfig.TB_GROUP)
                        .addQueryCondition(ColumnEnum.COLUMN_INT_001, group.getId());
                Cursor cursor = SqlUtil.getDao().queryCursor(sqLiteInfo);
                if (cursor.getCount() > 0) {
                    toast("id=" + groupId + "的条目已存在，更新条目内容");
                } else {
                    toast("不存在id=" + groupId + "的条目，新建条目");
                }
                cursor.close();
                result = SqlUtil.getDao().saveValue(group, sqLiteInfo);
            }
        }
        if (result >= 0) {
            toast("保存成功");
            setResult(RESULT_OK);
            finish();
        } else {
            toast("保存失败");
        }
    }

    private SQLiteInfo initQueryInfo() {
        SQLiteInfo sqLiteInfo;
        getEditValue();
        if (isStudentType) {
            sqLiteInfo = new SQLiteInfo(AppConfig.TB_STUDENT);
            if (studentId != null) {
                sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_INT_001, studentId);
            }
            if (studentAge != null) {
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
            if (groupId != null) {
                sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_INT_001, groupId);
            }
            if (!TextUtils.isEmpty(groupName)) {
                sqLiteInfo.addQueryCondition(ColumnEnum.COLUMN_STRING_001, groupName);
            }
        }
        return sqLiteInfo;
    }

    private void getEditValue() {
        String groupIdStr = etGroupId.getText().toString().trim();
        groupId = null;
        if (!TextUtils.isEmpty(groupIdStr)) {
            try {
                groupId = Integer.parseInt(groupIdStr);
            } catch (NumberFormatException e) {
                //
            }
        }
        groupName = etGroupName.getText().toString().trim();
        String studentIdStr = etStudentId.getText().toString().trim();
        studentId = null;
        if (!TextUtils.isEmpty(studentIdStr)) {
            try {
                studentId = Integer.parseInt(studentIdStr);
            } catch (NumberFormatException e) {
                //
            }
        }
        studentName = etStudentName.getText().toString().trim();
        String studentAgeStr = etStudentAge.getText().toString().trim();
        studentAge = null;
        if (!TextUtils.isEmpty(studentAgeStr)) {
            try {
                studentAge = Integer.parseInt(studentAgeStr);
            } catch (NumberFormatException e) {
                //
            }
        }
        int sexId = rgSex.getCheckedRadioButtonId();
        if (sexId == R.id.rb_man) {
            studentSex = "男";
        } else if (sexId == R.id.rb_woman) {
            studentSex = "女";
        } else {
            studentSex = null;
        }
    }

}