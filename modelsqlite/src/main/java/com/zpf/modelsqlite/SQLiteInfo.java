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

    /**
     * 升序降序排列
     */
    public SQLiteInfo setAsc(boolean asc) {
        this.asc = asc;
        return this;
    }

    public String getOtherCondition() {
        return otherCondition;
    }

    /**
     * 其他语句
     */
    public SQLiteInfo setOtherCondition(String otherCondition) {
        this.otherCondition = otherCondition;
        return this;
    }

    public SQLiteInfo addQueryCondition(ColumnInfo info) {
        queryInfoList.add(info);
        return this;
    }

    public SQLiteInfo addQueryCondition(ColumnEnum columnName, String columnValue) {
        this.queryInfoList.add(new ColumnInfo(columnName, SQLiteConfig.RELATION_EQUALITY, columnValue));
        return this;
    }

    public SQLiteInfo addQueryCondition(ColumnEnum columnName, String relation, String columnValue) {
        this.queryInfoList.add(new ColumnInfo(columnName, relation, columnValue));
        return this;
    }

    /**
     * 添加排序条件
     */
    public SQLiteInfo addOrderInfo(ColumnEnum columnName) {
        this.orderColumnList.put(columnName.getValue(), columnName);
        return this;
    }

}
