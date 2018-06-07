package com.zpf.modelsqlite;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库查询辅助信息
 * Created by ZPF on 2018/1/24.
 */
public class SQLiteInfo {
    private SparseArray<ColumnEnum> orderColumnList = new SparseArray<>();
    private List<ColumnInfo> queryInfoList = new ArrayList<>();
    private List<ColumnInfo> changeValueList = new ArrayList<>();
    private String tableName;
    private boolean asc;
    private String otherCondition;
    private StringBuilder limitCondition = new StringBuilder();

    public SQLiteInfo(@NonNull String tableName) {
        this.tableName = tableName;
    }

    SparseArray<ColumnEnum> getOrderColumnList() {
        return orderColumnList;
    }

    List<ColumnInfo> getQueryInfoList() {
        return queryInfoList;
    }

    List<ColumnInfo> getChangeValueList() {
        return changeValueList;
    }

    String getTableName() {
        return tableName;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public String getOtherCondition() {
        return otherCondition;
    }

    public void setOtherCondition(String otherCondition) {
        this.otherCondition = otherCondition;
    }

    public SQLiteInfo addQueryCondition(ColumnInfo info) {
        queryInfoList.add(info);
        return this;
    }

    public SQLiteInfo addQueryCondition(ColumnEnum columnName, String columnValue) {
        this.queryInfoList.add(new ColumnInfo(columnName, SQLiteRelation.RELATION_EQUALITY, columnValue));
        return this;
    }

    public SQLiteInfo addQueryCondition(ColumnEnum columnName, String relation, String columnValue) {
        this.queryInfoList.add(new ColumnInfo(columnName, relation, columnValue));
        return this;
    }

    public SQLiteInfo addOrderInfo(ColumnEnum columnName) {
        this.orderColumnList.put(columnName.getValue(), columnName);
        return this;
    }

}
