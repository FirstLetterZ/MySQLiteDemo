package com.zpf.modelsqlite;

import android.support.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ZPF on 2018/6/7.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@StringDef(value = {SQLiteRelation.RELATION_EQUALITY, SQLiteRelation.RELATION_LIKE,
        SQLiteRelation.RELATION_LESS_THAN, SQLiteRelation.RELATION_MORE_THAN,
        SQLiteRelation.RELATION_LESS_OR_EQUAL, SQLiteRelation.RELATION_MORE_OR_EQUAL,
})
public @interface SQLiteRelation {
    String RELATION_EQUALITY = " =? ";
    String RELATION_BETWEEN = " BETWEEN ";//暂不支持
    String RELATION_LIKE = " LIKE ? ";
    String RELATION_LESS_THAN = " <? ";
    String RELATION_MORE_THAN = " >? ";
    String RELATION_LESS_OR_EQUAL = " <=? ";
    String RELATION_MORE_OR_EQUAL = " >=? ";
}
