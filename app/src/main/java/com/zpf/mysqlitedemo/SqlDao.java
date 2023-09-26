package com.zpf.mysqlitedemo;

import com.zpf.modelsqlite.SqlUtil;

public class SqlDao {

    public static SqlApi getApi() {
       return SqlApiInstance.sqlApi;
    }

    private static class SqlApiInstance {
        static final SqlApi sqlApi = SqlUtil.create(SqlApi.class);
    }
}
