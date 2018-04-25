package com.zpf.mysqlitedemo;

import com.zpf.modelsqlite.ColumnEnum;
import com.zpf.modelsqlite.SQLiteColumn;

/**
 * Created by ZPF on 2018/4/25.
 */

public class BaseInfo {
    @SQLiteColumn(column = ColumnEnum.COLUMN_STRING_001)
    private String TestInfo = "fromBase";
}
