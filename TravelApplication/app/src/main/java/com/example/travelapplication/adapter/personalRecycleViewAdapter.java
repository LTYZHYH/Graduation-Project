package com.example.travelapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.R;
import com.example.travelapplication.activity.details.TravelStrategyDetailsActivity;
import com.example.travelapplication.activity.register.PersonalCenterActivity;
import com.example.travelapplication.model.TravelStrategy;

import java.util.List;

public class personalRecycleViewAdapter extends RecyclerView.Adapter<personalRecycleViewAdapter.GridViewHolder> {
    private Context mContext;
    private List<TravelStrategy> travelStrategyBeanList;
    public personalRecycleViewAdapter(Context context){
        mContext = context;
    }

    public void setGridDataList(List<TravelStrategy> list) {
        travelStrategyBeanList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_publish_strategy_item,parent,false);
        return new personalRecycleViewAdapter.GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, final int position) {
        holder.title.setText(travelStrategyBeanList.get(position).getTheme());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalCenterActivity personalCenterActivity = (PersonalCenterActivity) mContext;
                Intent intent = new Intent(personalCenterActivity, TravelStrategyDetailsActivity.class);
                intent.putExtra("strategy_id", travelStrategyBeanList.get(position).getStrategyId());
                personalCenterActivity.startActivity(intent);
            }
        });
        Integer sa = travelStrategyBeanList.get(position).getStrategyAudit();
        if (sa == 0){
            holder.strategyAudit.setTextColor(Color.parseColor("#C7C7C7"));
            holder.strategyAudit.setText("待审核");
            holder.title.setClickable(false);
        } else if (sa == 1){
            holder.strategyAudit.setTextColor(Color.parseColor("#DC143C"));
            holder.strategyAudit.setText("未通过");
            holder.title.setClickable(false);
        } else {
            holder.strategyAudit.setTextColor(Color.parseColor("#00FF7F"));
            holder.strategyAudit.setText("已通过");
        }
    }

    @Override
    public int getItemCount() {
        return travelStrategyBeanList == null ? 0 : travelStrategyBeanList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView strategyAudit;

        public GridViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.my_title);
            strategyAudit = itemView.findViewById(R.id.audit_status);
        }

    }
}
