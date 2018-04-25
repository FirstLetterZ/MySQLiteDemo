package com.zpf.mysqlitedemo;

import com.zpf.modelsqlite.ColumnEnum;
import com.zpf.modelsqlite.SQLiteClassify;
import com.zpf.modelsqlite.SQLiteColumn;
import com.zpf.modelsqlite.SQLiteRelevance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZPF on 2018/1/29.
 */

@SQLiteClassify(tableName = AppConfig.TB_TEST)
public class TestInfo extends BaseInfo {
    @SQLiteColumn(column = ColumnEnum.COLUMN_INT_001)
    private int testInt = 11;

    @SQLiteColumn(column = ColumnEnum.COLUMN_FLOAT_001)
    private float testFloat = 22f;

    @SQLiteColumn(column = ColumnEnum.COLUMN_BOOLEAN_001)
    private boolean testBoolean = false;

    @SQLiteColumn(column = ColumnEnum.COLUMN_LONG_001)
    private long testLong = 33L;

    @SQLiteColumn(column = ColumnEnum.COLUMN_SHORT_001)
    private short testShort = 44;

    @SQLiteColumn(column = ColumnEnum.COLUMN_DOUBLE_001)
    private double testDouble = 55d;

    @SQLiteColumn(column = ColumnEnum.COLUMN_STRING_004)
    private String testString = "";

    @SQLiteRelevance(saveColumn = ColumnEnum.COLUMN_STRING_002, targetColumn = ColumnEnum.COLUMN_STRING_001)
    private InsideBean testObject = new InsideBean();

    @SQLiteColumn(column = ColumnEnum.COLUMN_STRING_003)
    private List<Info> stringArray;

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    public float getTestFloat() {
        return testFloat;
    }

    public void setTestFloat(float testFloat) {
        this.testFloat = testFloat;
    }

    public boolean isTestBoolean() {
        return testBoolean;
    }

    public void setTestBoolean(boolean testBoolean) {
        this.testBoolean = testBoolean;
    }

    public long getTestLong() {
        return testLong;
    }

    public void setTestLong(long testLong) {
        this.testLong = testLong;
    }

    public short getTestShort() {
        return testShort;
    }

    public void setTestShort(short testShort) {
        this.testShort = testShort;
    }

    public double getTestDouble() {
        return testDouble;
    }

    public void setTestDouble(double testDouble) {
        this.testDouble = testDouble;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public InsideBean getTestObject() {
        return testObject;
    }

    public void setTestObject(InsideBean testObject) {
        this.testObject = testObject;
    }

    public List<Info> getStringArray() {
        return stringArray;
    }

    public void setStringArray(List<Info> stringArray) {
        this.stringArray = stringArray;
    }

}
