package com.zpf.mysqlitedemo.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zpf.modelsqlite.CacheUtil;
import com.zpf.modelsqlite.ColumnEnum;
import com.zpf.modelsqlite.SQLiteInfo;
import com.zpf.mysqlitedemo.R;
import com.zpf.mysqlitedemo.data.AppConfig;
import com.zpf.mysqlitedemo.data.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZPF on 2018/4/29.
 */

public class GroupAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private List<Group> groupList = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private ExecutorService singleThreadPool;
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
        holder.tvAgeLabel.setVisibility(View.GONE);
        holder.tvSexLabel.setVisibility(View.GONE);
        holder.tvAge.setVisibility(View.GONE);
        holder.tvSex.setVisibility(View.GONE);
        holder.tvType.setText("Student");
        holder.tvId.setText(groupList.get(position).getId());
        holder.tvName.setText(groupList.get(position).getName());
        final int n=position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null){
                    itemClickListener.OnItemClick(n,groupList.get(n));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void refresh(boolean asc) {
        groupList.clear();
        SQLiteInfo sqLiteInfo = new SQLiteInfo(AppConfig.TB_GROUP)
                .addOrderInfo(ColumnEnum.COLUMN_INT_001).setAsc(asc);
        List<Group> list = CacheUtil.instance().selectValueList(Group.class, sqLiteInfo);
        if (list != null && list.size() > 0) {
            groupList.addAll(list);
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
        void OnItemClick(int position,Group group);
    }
}
