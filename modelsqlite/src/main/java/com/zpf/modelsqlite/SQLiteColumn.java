package com.zpf.modelsqlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库表格‘列’的注解
 * Created by ZPF on 2018/1/29.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLiteColumn {
   @ColumnLimit int column();
}
