package com.zpf.mysqlitedemo.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zpf.modelsqlite.SQLiteInfo;
import com.zpf.modelsqlite.SqlOrderInfo;
import com.zpf.modelsqlite.constant.ColumnEnum;
import com.zpf.modelsqlite.SqlUtil;
import com.zpf.mysqlitedemo.R;
import com.zpf.mysqlitedemo.SqlDao;
import com.zpf.mysqlitedemo.data.AppConfig;
import com.zpf.mysqlitedemo.data.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZPF on 2018/4/29.
 */

public class StudentAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private List<Student> studentList = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    ExecutorService singleThreadPool;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.tvType.setText("Student");
        holder.tvId.setText("" + studentList.get(position).getId());
        holder.tvName.setText(studentList.get(position).getName());
        holder.tvAge.setText("" + studentList.get(position).getAge());
        holder.tvSex.setText(studentList.get(position).isFemale() ? "女" : "男");
        final int n = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.OnItemClick(n, studentList.get(n));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void refresh(boolean asc) {
        studentList.clear();
        List<Student> list;
        if (AppConfig.checked) {
            if (asc) {
                list = SqlDao.getApi().queryAllStudentsAsc();
            } else {
                list = SqlDao.getApi().queryAllStudentsDesc();
            }
        } else {
            SQLiteInfo sqLiteInfo = new SQLiteInfo(AppConfig.TB_STUDENT);
            SqlOrderInfo orderInfo = new SqlOrderInfo();
            orderInfo.getColumnArray().add(ColumnEnum.COLUMN_INT_001);
            orderInfo.setAsc(asc);
            sqLiteInfo.setOrderInfo(orderInfo);
            list = SqlUtil.getDao().queryArray(Student.class, sqLiteInfo);
        }
        if (list != null && list.size() > 0) {
            studentList.addAll(list);
        }
        handler.sendEmptyMessage(0);
    }

    public void asyncRefresh(final boolean asc) {
        if (singleThreadPool == null) {
            singleThreadPool = Executors.newSingleThreadExecutor();
        }
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                refresh(asc);
            }
        });
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position, Student student);
    }
}
