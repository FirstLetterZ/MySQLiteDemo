package com.zpf.modelsqlite;

/**
 * 数据库查询辅助信息（列信息）
 * Created by ZPF on 2018/1/29.
 */
public class ColumnInfo {
    private ColumnEnum columnName;
    private String relation = SQLiteRelation.RELATION_EQUALITY;
    private Object columnValue;

    public ColumnInfo(ColumnEnum columnName, Object columnValue) {
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

    public ColumnInfo(ColumnEnum columnName, @SQLiteRelation String relation, Object columnValue) {
        this.columnName = columnName;
        this.relation = relation;
        this.columnValue = columnValue;
    }

    public ColumnEnum getColumnName() {
        return columnName;
    }

    public String getRelation() {
        return relation;
    }

    public Object getColumnValue() {
        return columnValue;
    }

}
