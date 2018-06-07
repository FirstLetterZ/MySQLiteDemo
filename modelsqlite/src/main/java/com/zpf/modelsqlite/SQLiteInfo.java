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
    private StringBuilder groupCondition;//分组条件语句(暂时只支持单条)
    private String tableName;
    private boolean asc;
    private String limitCondition;//条目数量控制
    private String otherCondition;//其他条件

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

    boolean isAsc() {
        return asc;
    }

    String getOtherCondition() {
        if (otherCondition == null) {
            return "";
        } else {
            return otherCondition;
        }
    }

    String getLimitCondition() {
        if (limitCondition == null) {
            return "";
        } else {
            return limitCondition;
        }
    }

    String getGroupCondition() {
        if (groupCondition == null) {
            return "";
        } else {
            return groupCondition.toString();
        }
    }


    /**
     * 升序降序排列
     */
    public SQLiteInfo setAsc(boolean asc) {
        this.asc = asc;
        return this;
    }

    /**
     * 其他语句
     */
    public SQLiteInfo setOtherCondition(String otherCondition) {
        this.otherCondition = otherCondition;
        return this;
    }

    /**
     * 添加查询条件
     */
    public SQLiteInfo addQueryCondition(ColumnInfo info) {
        queryInfoList.add(info);
        return this;
    }

    public SQLiteInfo addQueryCondition(ColumnEnum columnName, String columnValue) {
        this.queryInfoList.add(new ColumnInfo(columnName, SQLiteRelation.RELATION_EQUALITY, columnValue));
        return this;
    }

    public SQLiteInfo addQueryCondition(ColumnEnum columnName, @SQLiteRelation String relation, String columnValue) {
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

    /**
     * 设置条目数量
     */
    public SQLiteInfo setLimitFromTo(int start, int end) {
        if (start >= 0 && end > start) {
            limitCondition = SQLiteConfig.LIMIT + start + "," + end;
        } else {
            limitCondition = null;
        }
        return this;
    }

    /**
     * 设置条目数量
     */
    public SQLiteInfo setLimitOffset(int size, int begin) {
        if (size > 0 && begin >= 0) {
            limitCondition = SQLiteConfig.LIMIT + size + SQLiteConfig.OFFSET + begin;
        } else {
            limitCondition = null;
        }
        return this;
    }

    /**
     * 添加分组条件
     *
     * @return
     */
    public SQLiteInfo addGroupCondition(ColumnEnum columnEnum) {
        return addGroupCondition(columnEnum, -1, SQLiteRelation.RELATION_EQUALITY);
    }

    public SQLiteInfo addGroupCondition(ColumnEnum columnEnum, int count, @SQLiteRelation String relation) {
        groupCondition = new StringBuilder(32);
        groupCondition.append(SQLiteConfig.GROUP)
                .append(SQLiteConfig.COLUMN_NAME)
                .append(columnEnum.getValue());
        if (count > 0) {
            groupCondition.append(SQLiteConfig.HAVING_COUNT)
                    .append(SQLiteConfig.COLUMN_NAME)
                    .append(columnEnum.getValue())
                    .append(")")
                    .append(relation)
                    .append(count);
        }
        return this;
    }

}
