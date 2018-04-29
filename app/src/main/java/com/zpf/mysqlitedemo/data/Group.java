package com.zpf.mysqlitedemo.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.zpf.modelsqlite.ColumnEnum;
import com.zpf.modelsqlite.SQLiteClassify;
import com.zpf.modelsqlite.SQLiteColumn;

/**
 * Created by ZPF on 2018/4/29.
 */
@SQLiteClassify(tableName = AppConfig.TB_GROUP)
public class Group implements Parcelable {
    @SQLiteColumn(column = ColumnEnum.COLUMN_INT_001)
    private int id;

    @SQLiteColumn(column = ColumnEnum.COLUMN_STRING_001)
    private String name;

    public Group() {
    }

    protected Group(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
