package com.zpf.mysqlitedemo.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.zpf.modelsqlite.ColumnEnum;
import com.zpf.modelsqlite.SQLiteClassify;
import com.zpf.modelsqlite.SQLiteColumn;

/**
 * Created by ZPF on 2018/4/29.
 */
@SQLiteClassify(tableName = AppConfig.TB_STUDENT)
public class Student implements Parcelable {
    @SQLiteColumn(column = ColumnEnum.COLUMN_INT_001)
    private int id;

    @SQLiteColumn(column = ColumnEnum.COLUMN_STRING_001)
    private String name;

    @SQLiteColumn(column = ColumnEnum.COLUMN_INT_002)
    private int age;

    @SQLiteColumn(column = ColumnEnum.COLUMN_BOOLEAN_001)
    private boolean female;

    @SQLiteColumn(column = ColumnEnum.COLUMN_INT_003)
    private Group group;

    public Student() {
    }

    protected Student(Parcel in) {
        id = in.readInt();
        name = in.readString();
        age = in.readInt();
        female = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeByte((byte) (female ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
