package com.zpf.modelsqlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解字段与其他表关联
 * 关联对象必须支持newInstance()
 * Created by ZPF on 2018/4/24.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLiteRelevance {
    ColumnEnum saveColumn();//保存的位置,应查找类型一致

    ColumnEnum targetColumn();//查找的关键字,应与保存类型一致
}
