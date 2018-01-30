package com.zpf.modelsqlite;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * '列'命名限制，必须包含在ColumnEnum之内
 * Created by ZPF on 2018/1/29.
 */
@IntDef({ColumnEnum.COLUMN_STRING_001, ColumnEnum.COLUMN_STRING_002, ColumnEnum.COLUMN_STRING_003, ColumnEnum.COLUMN_STRING_004,
        ColumnEnum.COLUMN_STRING_005, ColumnEnum.COLUMN_STRING_006, ColumnEnum.COLUMN_STRING_007, ColumnEnum.COLUMN_STRING_008,
        ColumnEnum.COLUMN_STRING_009, ColumnEnum.COLUMN_STRING_010, ColumnEnum.COLUMN_STRING_011, ColumnEnum.COLUMN_STRING_012,
        ColumnEnum.COLUMN_STRING_013, ColumnEnum.COLUMN_STRING_014, ColumnEnum.COLUMN_STRING_015, ColumnEnum.COLUMN_STRING_016,
        ColumnEnum.COLUMN_STRING_017, ColumnEnum.COLUMN_STRING_018, ColumnEnum.COLUMN_STRING_019, ColumnEnum.COLUMN_STRING_020,
        ColumnEnum.COLUMN_STRING_021, ColumnEnum.COLUMN_STRING_022, ColumnEnum.COLUMN_STRING_023, ColumnEnum.COLUMN_STRING_024,
        ColumnEnum.COLUMN_INT_001, ColumnEnum.COLUMN_INT_002, ColumnEnum.COLUMN_INT_003, ColumnEnum.COLUMN_INT_004,
        ColumnEnum.COLUMN_INT_005, ColumnEnum.COLUMN_INT_006, ColumnEnum.COLUMN_INT_007, ColumnEnum.COLUMN_INT_008,
        ColumnEnum.COLUMN_BOOLEAN_001, ColumnEnum.COLUMN_BOOLEAN_002, ColumnEnum.COLUMN_BOOLEAN_003,
        ColumnEnum.COLUMN_BOOLEAN_004, ColumnEnum.COLUMN_BOOLEAN_005,
        ColumnEnum.COLUMN_LONG_001, ColumnEnum.COLUMN_LONG_002, ColumnEnum.COLUMN_LONG_003,
        ColumnEnum.COLUMN_LONG_004, ColumnEnum.COLUMN_LONG_005,
        ColumnEnum.COLUMN_FLOAT_001, ColumnEnum.COLUMN_FLOAT_002, ColumnEnum.COLUMN_FLOAT_003,
        ColumnEnum.COLUMN_FLOAT_004, ColumnEnum.COLUMN_FLOAT_005,
        ColumnEnum.COLUMN_DOUBLE_001, ColumnEnum.COLUMN_DOUBLE_002, ColumnEnum.COLUMN_DOUBLE_003,
        ColumnEnum.COLUMN_SHORT_001, ColumnEnum.COLUMN_SHORT_002, ColumnEnum.COLUMN_SHORT_003})
@Target({ElementType.METHOD,ElementType.LOCAL_VARIABLE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@interface ColumnLimit {
}
