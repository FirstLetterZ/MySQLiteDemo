package com.zpf.mysqlitedemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zpf.mysqlitedemo.R;

/**
 * Created by ZPF on 2018/4/29.
 */

public class ListViewHolder extends RecyclerView.ViewHolder {
    TextView tvType,tvId,tvName,tvSex,tvAge,tvSexLabel,tvAgeLabel;
    public ListViewHolder(View itemView) {
        super(itemView);
        tvType=itemView.findViewById(R.id.tv_type);
        tvId=itemView.findViewById(R.id.tv_id);
        tvName=itemView.findViewById(R.id.tv_name);
        tvSex=itemView.findViewById(R.id.tv_sex);
        tvAge=itemView.findViewById(R.id.tv_age);
        tvSexLabel=itemView.findViewById(R.id.tv_sex_label);
        tvAgeLabel=itemView.findViewById(R.id.tv_age_label);
    }
}
