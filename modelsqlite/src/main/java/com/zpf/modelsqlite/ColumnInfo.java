package com.zpf.modelsqlite;

import android.support.annotation.ColorInt;

/**
 * 数据库查询辅助信息（列信息）
 * Created by ZPF on 2018/1/29.
 */

public class ColumnInfo {
    private int columnName;
    private String relation = SQLiteConfig.RELATION_EQUALITY;
    private Object columnValue;

    public ColumnInfo(@ColumnLimit int columnName, Object columnValue) {
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

    public ColumnInfo(@ColumnLimit int columnName, String relation, Object columnValue) {
        this.columnName = columnName;
        this.relation = relation;
        this.columnValue = columnValue;
    }

    public int getColumnName() {
        return columnName;
    }

    public String getRelation() {
        return relation;
    }

    public Object getColumnValue() {
        return columnValue;
    }

}
