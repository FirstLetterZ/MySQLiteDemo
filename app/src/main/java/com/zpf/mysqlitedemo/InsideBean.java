package com.zpf.mysqlitedemo;

import com.zpf.modelsqlite.ColumnEnum;
import com.zpf.modelsqlite.SQLiteClassify;
import com.zpf.modelsqlite.SQLiteColumn;


/**
 * Created by ZPF on 2018/1/29.
 */
@SQLiteClassify(tableName = AppConfig.TB_INSERT)
public class InsideBean {
    private int insideTestInt;
    private float insideTestFloat;
    private boolean insideTestBoolean = true;
    private long insideTestLong;
    private short insideTestShort;
    private double insideTestDouble;
    @SQLiteColumn(column = ColumnEnum.COLUMN_STRING_001)
    private String insideTestString = "test bean";

    public int getInsideTestInt() {
        return insideTestInt;
    }

    public void setInsideTestInt(int insideTestInt) {
        this.insideTestInt = insideTestInt;
    }

    public float getInsideTestFloat() {
        return insideTestFloat;
    }

    public void setInsideTestFloat(float insideTestFloat) {
        this.insideTestFloat = insideTestFloat;
    }

    public boolean isInsideTestBoolean() {
        return insideTestBoolean;
    }

    public void setInsideTestBoolean(boolean insideTestBoolean) {
        this.insideTestBoolean = insideTestBoolean;
    }

    public long getInsideTestLong() {
        return insideTestLong;
    }

    public void setInsideTestLong(long insideTestLong) {
        this.insideTestLong = insideTestLong;
    }

    public short getInsideTestShort() {
        return insideTestShort;
    }

    public void setInsideTestShort(short insideTestShort) {
        this.insideTestShort = insideTestShort;
    }

    public double getInsideTestDouble() {
        return insideTestDouble;
    }

    public void setInsideTestDouble(double insideTestDouble) {
        this.insideTestDouble = insideTestDouble;
    }

    public String getInsideTestString() {
        return insideTestString;
    }

    public void setInsideTestString(String insideTestString) {
        this.insideTestString = insideTestString;
    }
}
