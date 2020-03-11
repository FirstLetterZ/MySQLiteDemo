package com.zpf.mysqlitedemo;

import com.zpf.modelsqlite.retrofit.SqlRetrofit;

public class SqlDao {

    public static SqlApi getApi() {
       return SqlApiInstance.sqlApi;
    }

    private static class SqlApiInstance {
        static final SqlApi sqlApi = SqlRetrofit.INSTANCE.create(SqlApi.class);
    }
}
