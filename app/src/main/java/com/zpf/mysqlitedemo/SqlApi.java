package com.zpf.mysqlitedemo;

import com.zpf.modelsqlite.anno.additional.DataBody;
import com.zpf.modelsqlite.anno.additional.ORDER;
import com.zpf.modelsqlite.anno.additional.Value;
import com.zpf.modelsqlite.anno.additional.Where;
import com.zpf.modelsqlite.anno.operation.DELETE;
import com.zpf.modelsqlite.anno.operation.QUERY;
import com.zpf.modelsqlite.anno.operation.SAVE;
import com.zpf.modelsqlite.anno.operation.UPDATE;
import com.zpf.modelsqlite.constant.ColumnEnum;
import com.zpf.mysqlitedemo.data.AppConfig;
import com.zpf.mysqlitedemo.data.Student;

import java.util.List;

public interface SqlApi {

    @DELETE(table = AppConfig.TB_STUDENT)
    boolean deleteStudent(@Where(column = ColumnEnum.COLUMN_INT_001) int id);

    @SAVE(table = AppConfig.TB_STUDENT)
    int saveStudent(@DataBody Student student, @Where(column = ColumnEnum.COLUMN_INT_001) int id);

    @UPDATE(table = AppConfig.TB_STUDENT)
    boolean updateStudent(
            @Value(column = ColumnEnum.COLUMN_STRING_001, ignoreOnNull = true) String name,
            @Value(column = ColumnEnum.COLUMN_INT_002) int age,
            @Value(column = ColumnEnum.COLUMN_BOOLEAN_001) boolean female,
            @Where(column = ColumnEnum.COLUMN_INT_001) int studentId,
            @Where(column = ColumnEnum.COLUMN_INT_003) int groupId
    );

    @QUERY(table = AppConfig.TB_STUDENT, endIndex = 10000)
    @ORDER(columns = {ColumnEnum.COLUMN_INT_001})
    List<Student> queryAllStudentsAsc();

    @QUERY(table = AppConfig.TB_STUDENT, endIndex = 10000)
    @ORDER(columns = {ColumnEnum.COLUMN_INT_001}, asc = false)
    List<Student> queryAllStudentsDesc();
}
