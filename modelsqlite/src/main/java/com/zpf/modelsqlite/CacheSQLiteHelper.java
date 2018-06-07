package com.zpf.modelsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

/**
 * 数据库构建
 * Created by ZPF on 2018/1/29.
 */
public class CacheSQLiteHelper extends SQLiteOpenHelper {

    CacheSQLiteHelper(Context context, @NonNull String name) {
        super(context, name, null, SQLiteConfig.DB_CACHE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTbCache());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private String createTbCache() {
        StringBuilder builder = new StringBuilder();
        builder.append("create table if not exists ")
                .append(SQLiteConfig.TB_CACHE)
                .append("(_id integer primary key autoincrement,")
                .append(SQLiteConfig.FIRST_KEY_WORD)
                .append(" text");
        ColumnEnum[] columnEnums = ColumnEnum.values();
        for (ColumnEnum columnEnum : columnEnums) {
            int column = columnEnum.getValue();
            String str = getColumnType(column);
            if (str != null) {
                builder.append(",").append(SQLiteConfig.COLUMN_NAME).append(column).append(str);
            }
        }
        builder.append(",")
                .append(SQLiteConfig.OTHER)
                .append(" text")
                .append(")");
        return builder.toString();
    }

    private String getColumnType(int column) {
        String type;
        switch (column / 1000 * 1000) {
            case SQLiteConfig.TYPE_STRING:
                type = " text";
                break;
            case SQLiteConfig.TYPE_DOUBLE:
            case SQLiteConfig.TYPE_FLOAT:
                type = " real";
                break;
            case SQLiteConfig.TYPE_INT:
            case SQLiteConfig.TYPE_BOOLEAN:
            case SQLiteConfig.TYPE_LONG:
            case SQLiteConfig.TYPE_SHORT:
                type = " integer";
                break;
            default:
                type = null;
                break;
        }
        return type;
    }

}
