package com.zpf.modelsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作
 * Created by ZPF on 2018/1/29.
 */
public class CacheDao {
    private final String WHERE = " WHERE " + SQLiteConfig.FIRST_KEY_WORD + "=?";
    private final String SQL_SELECT = "SELECT * FROM " + SQLiteConfig.TB_CACHE + WHERE;
    private final String SQL_UPDATE = "UPDATE " + SQLiteConfig.TB_CACHE + " SET ";
    private final String SQL_INSERT = "INSERT INTO " + SQLiteConfig.TB_CACHE + "(";
    private final String SQL_DELETE = "DELETE FROM " + SQLiteConfig.TB_CACHE + WHERE;
    private SQLiteDatabase mSQLiteDatabase;
    private String TAG = "sqlite error";
    private static volatile CacheDao mDao;
    private static CacheInfo mInfo;

    //一定要先执行
    public static void init(CacheInfo info) {
        mInfo = info;
    }

    private CacheDao() {
        if (mInfo != null) {
            String path = mInfo.getDataBaseFolderPath();
            if (path == null) {
                path = SQLiteConfig.DB_USER_CACHE;
            } else if (path.endsWith("/")) {
                path = path + SQLiteConfig.DB_USER_CACHE;
            } else {
                path = path + "/" + SQLiteConfig.DB_USER_CACHE;
            }
            mSQLiteDatabase = new CacheSQLiteHelper(mInfo.getContext(), path).getWritableDatabase();
        } else {
            throw new IllegalStateException("uninitialized!Please complete the initialization first.");
        }
    }

    public static CacheDao instance() {
        if (mDao == null) {
            synchronized (CacheDao.class) {
                if (mDao == null) {
                    mDao = new CacheDao();
                }
            }
        }
        return mDao;
    }

    public static void closeDB() {
        if (mDao != null && mDao.mSQLiteDatabase != null && mDao.mSQLiteDatabase.isOpen()) {
            mDao.mSQLiteDatabase.close();
        }
        mDao = null;
    }

    //用于初始化
    public interface CacheInfo {
        Context getContext();//获取上下文

        String getDataBaseFolderPath();//数据库文件位置,可以为null

        String toJson(Object object);//将数据转成json格式的字符串，具体实现方式由使用者决定

        Object fromJson(String json, Class<?> cls);//将json格式转化成对应的对象，具体实现方式由使用者决定
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    /**
     * 单条查找获得光标
     */
    public Cursor getSelectCursor(SQLiteInfo info) {
        StringBuilder sql = new StringBuilder(120);
        sql.append(SQL_SELECT);
        String[] whereArg = appendWhere(sql, info);
        appendOrder(sql, info);
        if (!TextUtils.isEmpty(info.getOtherCondition())) {
            sql.append(info.getOtherCondition());
        }
        return mSQLiteDatabase.rawQuery(sql.toString(), whereArg);
    }

    public int saveValue(@NonNull Object value) {
        return saveValue(value, null);
    }

    public int saveValue(@NonNull Object value, SQLiteInfo whereClause) {
        int result = -1;
        if (whereClause == null) {
            SQLiteInfo insertInfo = null;
            SQLiteClassify classify = value.getClass().getAnnotation(SQLiteClassify.class);
            if (classify != null) {
                String firstKeyWord = classify.tableName();
                if (!TextUtils.isEmpty(firstKeyWord)) {
                    insertInfo = new SQLiteInfo(firstKeyWord);
                }
            }
            if (insertInfo == null) {
                Log.e(TAG, "未找到数据对应的表格信息：" + value.getClass().getName());
            } else {
                initChangeValueList(value, insertInfo);
                insertValue(insertInfo);
                result = 0;
            }
        } else {
            initChangeValueList(value, whereClause);
            Cursor cursor = getSelectCursor(whereClause);
            if (cursor.moveToFirst()) {
                updateValue(whereClause);
                result = 1;
            } else {
                insertValue(whereClause);
                result = 0;
            }
        }
        return result;
    }

    /**
     * 单条更新
     */
    private void updateValue(SQLiteInfo info) {
        int valuesSize = info.getChangeValueList().size();
        if (valuesSize == 0) {
            return;
        }
        StringBuilder sql = new StringBuilder(120);
        sql.append(SQL_UPDATE);
        int bindArgsSize = valuesSize + info.getQueryInfoList().size() + 1;
        String[] bindArgs = new String[bindArgsSize];
        for (int i = 0; i < valuesSize; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append(SQLiteConfig.COLUMN_NAME).append(info.getChangeValueList().get(i).getColumnName().getValue())
                    .append(SQLiteConfig.RELATION_EQUALITY);
            bindArgs[i] = toString(info.getChangeValueList().get(i).getColumnValue());
        }
        sql.append(WHERE);
        appendWhere(sql, info, bindArgs, valuesSize);
        mSQLiteDatabase.execSQL(sql.toString(), bindArgs);
    }

    /**
     * 单条插入
     */
    private void insertValue(SQLiteInfo info) {
        int valuesSize = info.getChangeValueList().size();
        if (valuesSize == 0) {
            return;
        }
        StringBuilder sql = new StringBuilder(120);
        sql.append(SQL_INSERT);
        sql.append(SQLiteConfig.FIRST_KEY_WORD);
        Object[] bindArgs = new Object[valuesSize + 1];
        bindArgs[0] = info.getTableName();
        for (int i = 0; i < valuesSize; i++) {
            sql.append(",").append(SQLiteConfig.COLUMN_NAME).append(info.getChangeValueList().get(i).getColumnName().getValue());
            bindArgs[i + 1] = toString(info.getChangeValueList().get(i).getColumnValue());
        }
        sql.append(") VALUES (");
        for (int i = 0; i < bindArgs.length; i++) {
            sql.append((i > 0) ? ",?" : "?");
        }
        sql.append(')');
        mSQLiteDatabase.execSQL(sql.toString(), bindArgs);
    }

    /**
     * 更新单列数据
     */
    public boolean updateColumn(ColumnInfo columnValue, ColumnInfo columnWhere) {
        ContentValues contentValues = new ContentValues();
        String columnName = SQLiteConfig.COLUMN_NAME + columnValue.getColumnName().getValue();
        Object data = columnValue.getColumnValue();
        if (data == null) {
            contentValues.put(columnName, "");
        } else if (data instanceof Boolean) {
            contentValues.put(columnName, (Boolean) data ? 1 : 0);
        } else if (data instanceof Integer) {
            contentValues.put(columnName, (Integer) data);
        } else if (data instanceof Float) {
            contentValues.put(columnName, (Float) data);
        } else if (data instanceof Short) {
            contentValues.put(columnName, (Short) data);
        } else if (data instanceof Long) {
            contentValues.put(columnName, (Long) data);
        } else if (data instanceof Double) {
            contentValues.put(columnName, (Double) data);
        } else if (data instanceof String) {
            contentValues.put(columnName, (String) data);
        } else {
            contentValues.put(columnName, toString(data));
        }
        String where = SQLiteConfig.COLUMN_NAME + columnWhere.getColumnName().getValue() + columnWhere.getRelation()
                + toString(columnWhere.getColumnValue());
        return mSQLiteDatabase.update(SQLiteConfig.TB_CACHE, contentValues, where, null) > 0;
    }

    /**
     * 保存多条
     */
    public void saveByArray(@NonNull HashMap<Object, SQLiteInfo> valueArray) {
        if (valueArray.size() > 0) {
            mSQLiteDatabase.beginTransaction();
            try {
                for (Map.Entry<Object, SQLiteInfo> entry : valueArray.entrySet()) {
                    saveValue(entry.getKey(), entry.getValue());
                }
                mSQLiteDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                mSQLiteDatabase.endTransaction();
            }
        }
    }

    /**
     * 删除单条
     */
    public void delete(SQLiteInfo info) {
        StringBuilder sql = new StringBuilder(120);
        sql.append(SQL_DELETE);
        String[] whereArgs = appendWhere(sql, info);
        mSQLiteDatabase.execSQL(sql.toString(), whereArgs);
    }

    /**
     * 删除多条
     */
    public void deleteByArray(@NonNull List<SQLiteInfo> infoList) {
        if (infoList.size() > 0) {
            mSQLiteDatabase.beginTransaction();
            try {
                for (SQLiteInfo info : infoList) {
                    delete(info);
                }
                mSQLiteDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                mSQLiteDatabase.endTransaction();
            }
        }
    }

    /**
     * 查询某列值，仅返回基本类型格式数据
     */
    public Object getColumnValue(int column, SQLiteInfo info) {
        Cursor cursor = getSelectCursor(info);
        Object result;
        if (cursor.moveToFirst()) {
            result = getValueByCursor(cursor, column);
        } else {
            result = getDefaultValue(column);
        }
        cursor.close();
        return result;
    }

    /**
     * 单条查询，保存至指定model
     */
    public boolean selectValueModel(Object receiver, SQLiteInfo info) {
        boolean result = false;
        Cursor cursor = getSelectCursor(info);
        if (cursor.moveToFirst()) {
            Field[] fields = receiver.getClass().getDeclaredFields();
            putValueToReceiver(fields, receiver, cursor);
            result = true;
        }
        cursor.close();
        return result;
    }

    /**
     * 多条查询，返回list
     */
    public <T> List<T> selectValueList(Class<T> cls, SQLiteInfo info) {
        return selectValueList(cls, info, 0, 100);
    }

    public <T> List<T> selectValueList(Class<T> cls, SQLiteInfo info, int startIndex, @IntRange(from = 0, to = 10000) int size) {
        SQLiteClassify classify = cls.getAnnotation(SQLiteClassify.class);
        List<T> list = new ArrayList<>();
        if (classify != null) {
            Cursor cursor = getSelectCursor(info);
            Field[] fields = cls.getDeclaredFields();
            int i = startIndex;
            while (cursor.moveToPosition(i) && size > 0) {
                try {
                    T receiver = cls.newInstance();
                    if (receiver != null) {
                        putValueToReceiver(fields, receiver, cursor);
                        list.add(receiver);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    i++;
                    size--;
                }
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 对receiver赋值
     */
    private void putValueToReceiver(Field[] fields, Object receiver, Cursor cursor) {
        for (Field field : fields) {
            SQLiteColumn note = field.getAnnotation(SQLiteColumn.class);
            if (note != null) {
                int column = note.column().getValue();
                try {
                    field.setAccessible(true);
                    Object value = getValueByCursor(cursor, column);
                    if (value != null && (value instanceof String)
                            && !TextUtils.equals(field.getType().getName(), String.class.getName())) {
                        if (mInfo != null) {
                            Object newValue = mInfo.fromJson(value.toString(), field.getType());
                            field.set(receiver, newValue);
                        }
                    } else {
                        field.set(receiver, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    /**
     * 将model值转转为查询条件
     * 同SQLiteInfo.addQueryCondition
     */
    public void initQueryInfoList(@NonNull Object model, SQLiteInfo info) {
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            SQLiteColumn note = field.getAnnotation(SQLiteColumn.class);
            if (note != null) {
                field.setAccessible(true);
                try {
                    Object value = field.get(model);
                    info.getQueryInfoList().add(new ColumnInfo(note.column(), SQLiteConfig.RELATION_EQUALITY, value));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    /**
     * 将model值转转为赋值条件
     */
    private void initChangeValueList(@NonNull Object model, @NonNull SQLiteInfo info) {
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            SQLiteColumn note = field.getAnnotation(SQLiteColumn.class);
            if (note != null) {
                field.setAccessible(true);
                try {
                    Object value = field.get(model);
                    info.getChangeValueList().add(new ColumnInfo(note.column(), "", value));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    /**
     * 添加查询条件语句
     */
    private String[] appendWhere(StringBuilder builder, SQLiteInfo info) {
        String[] whereArg = new String[info.getQueryInfoList().size() + 1];
        return appendWhere(builder, info, whereArg, 0);
    }

    private String[] appendWhere(StringBuilder builder, SQLiteInfo info, String[] whereArg, int index) {
        whereArg[index] = info.getTableName();
        for (int i = 0; i < info.getQueryInfoList().size(); i++) {
            if (checkRelation(info.getQueryInfoList().get(i).getRelation())) {
                builder.append(" AND ")
                        .append(SQLiteConfig.COLUMN_NAME)
                        .append(info.getQueryInfoList().get(i).getColumnName().getValue())
                        .append(info.getQueryInfoList().get(i).getRelation());
                whereArg[index + 1 + i] = toString(info.getQueryInfoList().get(i).getColumnValue());
            }
        }
        return whereArg;
    }

    /**
     * 添加排序条件语句
     */
    private void appendOrder(StringBuilder builder, SQLiteInfo info) {
        if (info.getOrderColumnList().size() > 0) {
            builder.append(SQLiteConfig.ORDER);
            for (int i = 0; i < info.getOrderColumnList().size(); i++) {
                builder.append(SQLiteConfig.COLUMN_NAME)
                        .append(info.getOrderColumnList().valueAt(i).getValue());
                if (i < info.getOrderColumnList().size() - 1) {
                    builder.append(",");
                }
            }
            if (info.isAsc()) {
                builder.append(SQLiteConfig.ASC);
            } else {
                builder.append(SQLiteConfig.DESC);
            }
        }
    }

    /**
     * 检查排序条件关系符号是否合法
     */
    private boolean checkRelation(String relation) {
        return (!TextUtils.isEmpty(relation))
                && (SQLiteConfig.RELATION_EQUALITY.equals(relation)
                || SQLiteConfig.RELATION_MORE_THAN.equals(relation)
                || SQLiteConfig.RELATION_MORE_OR_EQUAL.equals(relation)
                || SQLiteConfig.RELATION_LESS_THAN.equals(relation)
                || SQLiteConfig.RELATION_LESS_OR_EQUAL.equals(relation)
                || SQLiteConfig.RELATION_LIKE.equals(relation));
    }

    /**
     * 根据光标获取对应值
     */
    private Object getValueByCursor(Cursor cursor, int column) {
        int index = cursor.getColumnIndex(SQLiteConfig.COLUMN_NAME + column);
        Object result;
        if (index >= 0) {
            switch (getColumnType(column)) {
                case SQLiteConfig.TYPE_STRING:
                    result = cursor.getString(index);
                    break;
                case SQLiteConfig.TYPE_INT:
                    result = cursor.getInt(index);
                    break;
                case SQLiteConfig.TYPE_BOOLEAN:
                    result = cursor.getInt(index) == 1;
                    break;
                case SQLiteConfig.TYPE_FLOAT:
                    result = cursor.getFloat(index);
                    break;
                case SQLiteConfig.TYPE_LONG:
                    result = cursor.getLong(index);
                    break;
                case SQLiteConfig.TYPE_SHORT:
                    result = cursor.getShort(index);
                    break;
                case SQLiteConfig.TYPE_DOUBLE:
                    result = cursor.getDouble(index);
                    break;
                default:
                    result = cursor.getString(index);
                    break;
            }
        } else {
            result = getDefaultValue(column);
        }
        return result;
    }

    /**
     * 根据获取默认值
     */
    private Object getDefaultValue(int column) {

        switch (getColumnType(column)) {
            case SQLiteConfig.TYPE_STRING:
                return SQLiteConfig.DEF_STRING;
            case SQLiteConfig.TYPE_INT:
                return SQLiteConfig.DEF_NUMBER;
            case SQLiteConfig.TYPE_BOOLEAN:
                return SQLiteConfig.DEF_BOOLEAN;
            case SQLiteConfig.TYPE_FLOAT:
                return SQLiteConfig.DEF_NUMBER;
            case SQLiteConfig.TYPE_LONG:
                return SQLiteConfig.DEF_NUMBER;
            case SQLiteConfig.TYPE_SHORT:
                return SQLiteConfig.DEF_NUMBER;
            case SQLiteConfig.TYPE_DOUBLE:
                return SQLiteConfig.DEF_NUMBER;
            default:
                return SQLiteConfig.DEF_STRING;
        }
    }

    private int getColumnType(int column) {
        return column / 1000 * 1000;
    }

    private String toString(Object value) {
        String result = "";
        if (value != null) {
            if (value instanceof Boolean) {
                if ((boolean) value) {
                    result = "1";
                } else {
                    result = "0";
                }
            } else if (value instanceof String) {
                result = (String) value;
            } else {
                if (mInfo != null) {
                    result = mInfo.toJson(value);
                }
                if (result == null) {
                    result = value.toString();
                }
            }
        }
        if (result == null) {
            result = "";
        }
        return result;
    }

}
